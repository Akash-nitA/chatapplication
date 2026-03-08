import { Component, OnInit } from '@angular/core';
import { UserDetailsService } from '../service/user-details.service';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-user-info',
  standalone: false,
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.css'
})
export class UserInfoComponent implements OnInit{
  name:string="";
  res : Array<string> =[];
  searchControl : FormControl = new FormControl();
  constructor(private userdetails: UserDetailsService){}
  ngOnInit(): void {

    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value=>this.userdetails.getUserNames(value))
    ).subscribe({
      next: (users) =>{this.res=users},
      error: (err) =>{
        console.error("An error occured while fetching details");
        this.res=[];
      }
    });




  }
  getUserDetails(input : HTMLInputElement){
    this.name=input.value;
    this.userdetails.getUserDetails(this.name).subscribe({
      next : (res)=>{
        console.log(" successfull response recieved ",res);
      },
      error : (err)=>{
        console.error("Data not found for this user "+ this.name, err);
      }
    });
  }

  selectSuggestion(name : string){
    this.searchControl.setValue(name);
    this.res=[];
  }
}
