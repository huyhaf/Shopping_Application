import { UserService } from '../../services/user.service';
import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { RegisterDTO } from '../../dtos/user/register.dto';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!:NgForm;
  phone:string;
  password:string;
  retypePassword:string;
  fullname:string;
  address:string;
  isAccepted:boolean;
  dob: Date;
  
  constructor(private router:Router, private userService:UserService) {
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.fullname = '';
    this.address = '';
    this.isAccepted = false;
    this.dob = new Date();
  }
  onPhoneChange() {
    console.log("phone changed" + this.phone);

  }
  onRegister() {
    const today = new Date().getFullYear();
    const birthdayDate = new Date(this.dob).getFullYear();
    console.log(today-birthdayDate);
    const message = 
    `phone : ${this.phone}, 
    password : ${this.password}, 
    retypePassword : ${this.retypePassword}, 
    fullname : ${this.fullname}, 
    address : ${this.address}, 
    isAccepted : ${this.isAccepted},
    dob : ${this.dob}`;
    // alert(message);
    console.log("register");
    
    debugger
    const registerDTO:RegisterDTO = {
      "fullname":this.fullname,
      "phone_number": this.phone,
      "address": this.address,
      "password": this.password,
      "retype_password" : this.retypePassword,
      "date_of_birth": this.dob,
      "facebook_account_id": 0,
      "google_account_id": 0,
      "role_id":2
    };
    
    this.userService.register(registerDTO).subscribe(
      {
        next: (response : any) => {
          debugger
          this.router.navigate(['/login']);
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
  onDobChange() {
  }
  checkPasswordMatch(){
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({'passwordMismatch': true});
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }
  checkAge() {
    if (this.dob) {
      const today = new Date().getFullYear();
      const birthdayDate = new Date(this.dob).getFullYear();
      const age = today - birthdayDate;
      
      if (age < 18) {
        this.registerForm.form.controls['dob'].setErrors({'ageUnder18': true});
      } else {
        this.registerForm.form.controls['dob'].setErrors(null);
      }
    }
  }
}
