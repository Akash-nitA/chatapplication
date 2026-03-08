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
    const {name,password} = this.loginForm.value;
    this.username=name;
    this.password=password;


    this.api.loginStudent({"name":this.username,"password":this.password}).subscribe({
      next: (res) => {
        setTimeout(()=>{
          console.log('✅ Backend response:', res); // 👈 logs the response from API
          localStorage.setItem('token',res?.token);
          console.log("token is ",localStorage.getItem('token'));
          this.router.navigate(['/dashboard']);
        },5000);
        
      },
      error: (err) => {
        console.error('❌ Error response:', err);
        this.loginForm.get('name')?.setErrors({invalidCreds: true});
        // this.loginForm.get('errorMessage')?.setValue(" User Exists");
        
      },
    });

  }
}
