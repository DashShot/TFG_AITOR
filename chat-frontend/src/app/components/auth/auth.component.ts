import { Component } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { AuthMessage } from '../../models/auth-message';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})

export class AuthComponent {
  loginUsername: string = '';
  loginPassword: string  = '';
  registerUsername: string = '';
  registerPassword: string = '';
  showRegister: boolean = true;

  loginResponse: string ='';
  loginResponse2: string ='';

  constructor (private webSocketService: WebSocketService
  ){}

    login() {
      this.loginResponse2 = 'patata';
      console.log("LLAMANDO A WEB SERVICE");
      
      const loginMessage: AuthMessage = {username: this.loginUsername, password: this.loginPassword};
      this.loginResponse = this.webSocketService.loginService(loginMessage);
      
    }
  
  

  register() {
    // const registerMessage: AuthMessage = {username: this.registerUsername, password: this.registerPassword};
    // this.webSocketService.registryService(registerMessage);
    this.loginResponse2 = 'patata';
  }

}