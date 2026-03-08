import { Component, OnInit } from '@angular/core';
import { LoginServiceService } from '../service/login-service.service';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-login-page',
  standalone: false,
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent implements OnInit{
  username: string = '';
  password: string ='';
  isLoading: boolean =false;
  
  loginForm : FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
    password : new FormControl('',Validators.required),
    errorMessage: new FormControl('')
  });
  constructor(private api:LoginServiceService, private router:Router){}
  ngOnInit(): void {
      
  }

  saveData(){
    if (this.loginForm.invalid || this.isLoading) {
      return;
    }

    this.isLoading = true;
    this.loginForm.get('name')?.setErrors(null);
    const {name,password} = this.loginForm.value;
    this.username=name;
    this.password=password;


    this.api.loginStudent({"name":this.username,"password":this.password}).subscribe({
      next: (res) => {
        const token = res?.token;
        if(!token){
          this.loginForm.get('name')?.setErrors({invalidCreds: true});
          this.isLoading = false;
          return;
        }
        localStorage.setItem('token', token);
        this.isLoading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('❌ Error response:', err);
        this.loginForm.get('name')?.setErrors({invalidCreds: true});
        this.isLoading = false;
      },
    });

  }
}
