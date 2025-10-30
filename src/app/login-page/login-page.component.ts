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
  email: string ='';
  
  loginForm : FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
    email : new FormControl('',Validators.required),
    errorMessage: new FormControl('')
  });
  constructor(private api:LoginServiceService, private router:Router){}
  ngOnInit(): void {
      
  }

  saveData(){
    const {name,email} = this.loginForm.value;



    this.api.registerStudent({"name":this.username,"email":this.email}).subscribe({
      next: (res) => {
        console.log('✅ Backend response:', res); // 👈 logs the response from API
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('❌ Error response:', err);
        this.loginForm.get('name')?.setErrors({userExists: true});
        // this.loginForm.get('errorMessage')?.setValue(" User Exists");
        
      },
    });

  }
}
