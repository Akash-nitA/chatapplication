import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginServiceService {
  private url: string= "/auth";
  public isLoggedIn: boolean = false;
  constructor(private http: HttpClient) { }
  registerStudent(data: any) : Observable<any>{
    return this.http.post(`${this.url}/register`,data);
  }
  isAuthenticated() : boolean {
    if(localStorage.getItem('token')==null){
      return false;
    } 
    const token=localStorage.getItem('token');
    const parts= token?.split('.');
    if(parts?.length!=3) return false;
    try {
      const payloadBase64 = parts[1]
        .replace(/-/g, '+')
        .replace(/_/g, '/');
      const padded = payloadBase64.padEnd(Math.ceil(payloadBase64.length / 4) * 4, '=');
      const payloadJson=atob(padded);
      const payload = JSON.parse(payloadJson);

      const currentTime= Date.now()/1000;
      if(payload.exp && payload.exp<currentTime){
        localStorage.removeItem('token');
        return false;
      }

      return true;
    } catch {
      localStorage.removeItem('token');
      return false;
    }
  }
  loginStudent(data: any) : Observable<any>{
    return this.http.post(`${this.url}/login`,data);
  }
}
