import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginServiceService {
  private url: string= "http://localhost:8082/auth";
  public isLoggedIn: boolean = false;
  constructor(private http: HttpClient) { }
  registerStudent(data: any) : Observable<any>{
    return this.http.post(`${this.url}/register`,data);
  }
  isAuthenticated() : boolean {
    if(localStorage.getItem('token')==null){
      console.log("line 17 is the problem");
      return false;
    } 
    const token=localStorage.getItem('token');
    const parts= token?.split('.');
    if(parts?.length!=3) return false;
    let payloadBase64 = parts[1];
    let payloadJson=atob(payloadBase64);
    let payload = JSON.parse(payloadJson);

    const currentTime= Date.now()/1000;
    if(payload.exp && payload.exp<currentTime){
      console.log("line 29 is the problem");
      localStorage.removeItem('token');
      return false;
    }


    return true;
  }
  loginStudent(data: any) : Observable<any>{
    const request : Observable<Object> = this.http.post(`${this.url}/login`,data);

    return this.http.post(`${this.url}/login`,data);
  }
}
