import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WebSocketService } from '../../services/web-socket.service';
import { AuthMessage } from '../../models/auth-message';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';


@Component({
  selector: 'app-auth',
  imports: [],
  standalone: true,
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})


export class AuthComponent {
  loginUsername: string = '';
  loginPassword: string  = '';
  registerUsername: string = '';
  registerPassword: string = '';
  showRegister: boolean = true;

  loginResponse: string ='';
  loginResponse2: string ='';

  constructor (private webSocketService: WebSocketService){}

    login() {
      //const loginMessage: AuthMessage = {username: this.loginUsername, password: this.loginPassword};
      const loginMessage: AuthMessage = {username: "prueba", password: "prueba"};
      console.log("loginMessage");
      this.loginResponse = this.webSocketService.loginService(loginMessage);

      this.loginResponse2 = 'patata';
    }
  
  

  register() {
    const registerMessage: AuthMessage = {username: this.registerUsername, password: this.registerPassword};
    this.webSocketService.registryService(registerMessage);
    this.loginResponse2 = 'patata';
  }

}