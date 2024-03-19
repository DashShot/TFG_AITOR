import { Component, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';


import { AuthServicesService } from '../../services/auth-service.service';
import { WebSocketService } from '../../services/web-socket.service';
import { AuthMessage } from '../../message-models/auth-message';



@Component({
  selector: 'app-auth',
  imports: [CommonModule, ReactiveFormsModule ],
  standalone: true,
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})

export class AuthComponent {
  
  applyForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

 
  showRegister: boolean = false;

  @Input() loginResponse: string ='Prueba1';
  loginResponse2: string ='';

  constructor (private  authService: AuthServicesService, private webSocketService: WebSocketService) {}

    submitLogin(){
      //console.log("Username:" + this.applyForm.value.username);
      // this.loginResponse = this.authService.login(
      //     this.applyForm.value.username ?? '', 
      //     this.applyForm.value.password ?? '', 
      // )   
      var username = this.applyForm.value.username ?? '';
      var password = this.applyForm.value.password ?? '';

      console.log(`Usuario recivido -> Usuario: ${username}, password: ${password}`);
      const loginMessage: AuthMessage = {username: username, password: password};

      console.log("Iniciando Login.....");
      var loginResponse = '';

      this.webSocketService.loginService(loginMessage)
          .then(responseMessage => {
            console.log("Login response:", responseMessage);
            loginResponse = responseMessage;
          })
          .catch(error => {
            console.error("Login error:", error);
            // Handle any errors during login
          });
      return loginResponse;
    }


    //https://www.bezkoder.com/angular-17-jwt-auth/
   
    
}