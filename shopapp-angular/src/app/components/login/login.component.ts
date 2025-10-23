import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { LoginDTO } from '../../dtos/user/login.dto';
import { LoginResponse } from '../../responses/user/login.response';
import { TokenService } from '../../services/token.service';
import { RoleService } from '../../services/role.service';
import { Role } from '../../models/role';

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

  roles : Role[] = [];
  rememberMe : boolean = true;
  rememberLabel : string = "Remember Me";
  selectedRole : Role | undefined;
  
  constructor(
    private router:Router, 
    private userService:UserService,
    private tokenService:TokenService,
    private roleService:RoleService
  ) {}

  ngOnInit() {
    console.log('Login component initialized'); // Add this line
    debugger;
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        console.log('Roles received:', roles); // Add this line
        debugger;
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      },
      error: (error:any) => {
        console.error('Error fetching roles:', error); // Add this line
        debugger;
        console.log(error);
      }
    });
  }

  onPhoneChange() {
    console.log("phone changed" + this.phone);
  }

  onRememberChange() {
    // optional: update label or perform logic when checkbox changes
    this.rememberLabel = this.rememberMe ? 'Remember me' : 'Do not remember me';
    console.log('rememberMe:', this.rememberMe);
  }

  onLogIn() {
    const message = 
    `phone : ${this.phone}, 
    password : ${this.password},`;
    // alert(message);
    console.log("log in");
    
    // debugger
    const loginDTO:LoginDTO = {
      phone_number: this.phone,
      password: this.password,
      role_id: this.selectedRole?.id ?? 1
    };
    
    this.userService.login(loginDTO).subscribe(
      {
        next: (response : LoginResponse) => {
          debugger
          const {token} = response;
          if (this.rememberMe) {
            this.tokenService.setToken(token);
          }
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
