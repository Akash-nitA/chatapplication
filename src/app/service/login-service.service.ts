import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginServiceService {
  private url: string= "http://localhost:8082/auth";
  constructor(private http: HttpClient) { }
  registerStudent(data: any) : Observable<any>{
    return this.http.post(`${this.url}/register`,data);
  }
}
