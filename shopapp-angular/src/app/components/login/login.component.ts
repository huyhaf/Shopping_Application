import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { LoginDTO } from '../../dtos/user/login.dto';
import { LoginResponse } from '../../responses/user/login.response';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!:NgForm;
  phone:string = '';
  password:string = '';
  
  constructor(
    private router:Router, 
    private userService:UserService,
    private tokenService:TokenService
  ) {
  }
  onPhoneChange() {
    console.log("phone changed" + this.phone);

  }
  onLogIn() {
    const message = 
    `phone : ${this.phone}, 
    password : ${this.password},`;
    // alert(message);
    console.log("log in");
    
    // debugger
    const loginDTO:LoginDTO = {
      "phone_number": this.phone,
      "password": this.password
    };
    
    this.userService.login(loginDTO).subscribe(
      {
        next: (response : LoginResponse) => {
          debugger
          const {token} = response;
          this.tokenService.setToken(token);
          // this.router.navigate(['/login']);
        },
        complete: () => {
          debugger
        },
        error: (error:any) => {
          alert(error.error);
        }
      }
    );
  }
}
