import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserDetailsService {
  private url: string="https://jsonplaceholder.typicode.com/users";
  constructor(private http: HttpClient) { }
  getUserDetails(name : string){
    // console.log("requested url: "+ )
    return this.http.get(`${this.url}?name_like=${name}`);
  }
  getUserNames(prefix: string): Observable<string[]> {
    if (!prefix.trim()) {
      return of([]);
    }
    return this.http.get<any[]>(this.url).pipe(
      map(users =>
        users
          .filter(user => user.name.toLowerCase().startsWith(prefix.toLowerCase()))
          .map(user => user.name)
      )
    );
  }

}
