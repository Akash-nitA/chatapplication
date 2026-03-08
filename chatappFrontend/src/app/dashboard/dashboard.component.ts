import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ChatMessage, ChatService, ChatUser } from '../service/chat.service';
import { LoginServiceService } from '../service/login-service.service';


@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  router = inject(Router);
  private readonly chatService = inject(ChatService);
  private readonly loginService = inject(LoginServiceService);

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
    this.loadUsers();
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  selectUser(user: ChatUser): void {
    this.selectedUser = user;
    this.errorMessage = '';
    this.loadConversation();
  }

  sendMessage(): void {
    if (!this.selectedUser) {
      return;
    }
    const trimmedMessage = this.newMessage.trim();
    if (!trimmedMessage) {
      return;
    }

    this.chatService.sendMessage(this.selectedUser.name, trimmedMessage).subscribe({
      next: () => {
        this.newMessage = '';
        this.loadConversation();
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
      },
      error: () => {
        this.users = [];
        this.isLoadingUsers = false;
        this.errorMessage = 'Unable to load chat users.';
      }
    });
  }

  private loadConversation(): void {
    if (!this.selectedUser) {
      this.messages = [];
      return;
    }
    this.isLoadingMessages = true;
    this.chatService.getConversation(this.selectedUser.name).subscribe({
      next: (messages) => {
        this.messages = messages;
        this.isLoadingMessages = false;
      },
      error: () => {
        this.messages = [];
        this.isLoadingMessages = false;
        this.errorMessage = 'Unable to load messages for this user.';
      }
    });
  }

}
