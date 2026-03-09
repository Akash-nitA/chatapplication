import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ChatMessage, ChatService, ChatUser } from '../service/chat.service';
import { LoginServiceService } from '../service/login-service.service';
import { Subscription, interval, of } from 'rxjs';
import { catchError, startWith, switchMap, tap } from 'rxjs/operators';

interface SocketMessageEvent {
  type: string;
  sender: string;
  receiver: string;
  message: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  router = inject(Router);
  private readonly chatService = inject(ChatService);
  private readonly loginService = inject(LoginServiceService);
  private conversationPollingSub?: Subscription;
  private readonly pollingIntervalMs = 2000;
  private chatSocket?: WebSocket;
  private reconnectTimeoutId: ReturnType<typeof setTimeout> | null = null;
  private readonly socketReconnectDelayMs = 2000;
  private isSocketConnected = false;

  users: ChatUser[] = [];
  messages: ChatMessage[] = [];
  selectedUser: ChatUser | null = null;
  newMessage = '';
  currentUsername = '';
  isLoadingUsers = false;
  isLoadingMessages = false;
  errorMessage = '';

  ngOnInit(): void {
    this.currentUsername = this.loginService.getCurrentUsername() ?? '';
    this.connectChatSocket();
    this.loadUsers();
  }

  ngOnDestroy(): void {
    this.stopConversationPolling();
    this.disconnectChatSocket();
  }

  logout() {
    this.stopConversationPolling();
    this.disconnectChatSocket();
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  selectUser(user: ChatUser): void {
    if (this.selectedUser?.name === user.name) {
      return;
    }
    this.selectedUser = user;
    this.errorMessage = '';
    this.messages = [];
    this.loadConversationOnce();
    if (this.isSocketConnected) {
      this.stopConversationPolling();
      return;
    }
    this.startConversationPolling();
  }

  sendMessage(): void {
    if (!this.selectedUser) {
      return;
    }
    const trimmedMessage = this.newMessage.trim();
    if (!trimmedMessage) {
      return;
    }

    this.errorMessage = '';
    this.chatService.sendMessage(this.selectedUser.name, trimmedMessage).subscribe({
      next: () => {
        this.newMessage = '';
        this.loadConversationOnce();
      },
      error: () => {
        this.errorMessage = 'Unable to send message. Please try again.';
      }
    });
  }

  isSentMessage(message: ChatMessage): boolean {
    return message.sender === this.currentUsername;
  }

  private loadUsers(): void {
    this.isLoadingUsers = true;
    this.chatService.getChatUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.isLoadingUsers = false;
        if (!this.selectedUser && users.length > 0) {
          this.selectUser(users[0]);
        }
      },
      error: () => {
        this.users = [];
        this.isLoadingUsers = false;
        this.errorMessage = 'Unable to load chat users.';
      }
    });
  }

  private startConversationPolling(): void {
    this.stopConversationPolling();
    if (this.isSocketConnected) {
      return;
    }
    if (!this.selectedUser) {
      this.messages = [];
      this.isLoadingMessages = false;
      return;
    }

    this.isLoadingMessages = true;
    this.conversationPollingSub = interval(this.pollingIntervalMs)
      .pipe(
        startWith(0),
        switchMap(() => {
          if (!this.selectedUser) {
            return of(this.messages);
          }
          return this.chatService.getConversation(this.selectedUser.name).pipe(
            tap(() => {
              this.errorMessage = '';
            }),
            catchError(() => {
              this.errorMessage = 'Unable to load messages for this user.';
              return of(this.messages);
            })
          );
        })
      )
      .subscribe((messages) => {
        this.messages = messages;
        this.isLoadingMessages = false;
      });
  }

  private loadConversationOnce(): void {
    if (!this.selectedUser) {
      return;
    }
    this.chatService.getConversation(this.selectedUser.name).subscribe({
      next: (messages) => {
        this.messages = messages;
        this.errorMessage = '';
      }
    });
  }

  private stopConversationPolling(): void {
    this.conversationPollingSub?.unsubscribe();
    this.conversationPollingSub = undefined;
  }

  private connectChatSocket(): void {
    this.disconnectChatSocket();
    const token = localStorage.getItem('token');
    if (!token) {
      return;
    }
    const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
    this.chatSocket = new WebSocket(
      `${protocol}://${window.location.host}/ws/chat?token=${encodeURIComponent(token)}`
    );

    this.chatSocket.onopen = () => {
      this.isSocketConnected = true;
      this.stopConversationPolling();
      if (this.reconnectTimeoutId) {
        clearTimeout(this.reconnectTimeoutId);
        this.reconnectTimeoutId = null;
      }
      this.loadConversationOnce();
    };

    this.chatSocket.onmessage = (event) => {
      this.handleSocketMessage(event.data);
    };

    this.chatSocket.onclose = () => {
      this.chatSocket = undefined;
      this.isSocketConnected = false;
      if (this.selectedUser) {
        this.startConversationPolling();
      }
      if (localStorage.getItem('token')) {
        this.reconnectTimeoutId = setTimeout(() => {
          this.connectChatSocket();
        }, this.socketReconnectDelayMs);
      }
    };
  }

  private disconnectChatSocket(): void {
    if (this.reconnectTimeoutId) {
      clearTimeout(this.reconnectTimeoutId);
      this.reconnectTimeoutId = null;
    }
    if (this.chatSocket && this.chatSocket.readyState !== WebSocket.CLOSED) {
      this.chatSocket.close();
    }
    this.chatSocket = undefined;
    this.isSocketConnected = false;
  }

  private handleSocketMessage(rawMessage: string): void {
    try {
      const event = JSON.parse(rawMessage) as SocketMessageEvent;
      if (event.type !== 'NEW_MESSAGE' || !this.selectedUser) {
        return;
      }
      const otherParticipant =
        event.sender === this.currentUsername ? event.receiver : event.sender;
      if (otherParticipant === this.selectedUser.name) {
        this.loadConversationOnce();
      }
    } catch {
      return;
    }
  }
}
