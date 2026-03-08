import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ChatUser {
  id: number;
  name: string;
  email: string;
}

export interface ChatMessage {
  id: number;
  message: string;
  status: string;
  sender: string;
  receiver: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly url = '/chat';

  constructor(private http: HttpClient) {}

  getChatUsers(): Observable<ChatUser[]> {
    return this.http.get<ChatUser[]>(`${this.url}/users`, {
      headers: this.getAuthHeaders()
    });
  }

  getConversation(otherUsername: string): Observable<ChatMessage[]> {
    const params = new HttpParams().set('with', otherUsername);
    return this.http.get<ChatMessage[]>(`${this.url}/messages`, {
      headers: this.getAuthHeaders(),
      params
    });
  }

  sendMessage(receiver: string, message: string): Observable<unknown> {
    return this.http.post(
      `${this.url}/send`,
      { reciever: receiver, message },
      { headers: this.getAuthHeaders() }
    );
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }
}
