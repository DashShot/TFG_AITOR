import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WebSocketService } from '../../services/web-socket.service';
import { AuthMessage } from '../../models/auth-message';


@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [FormsModule],
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
  constructor (private webSocketService: WebSocketService){

  }

  login() {
    const loginMessage: AuthMessage = {username: this.loginUsername, password: this.loginPassword};
    this.loginResponse = this.webSocketService.loginService(loginMessage);
    this.loginResponse2 = 'patat';
  }
  
  

  register() {
    const registerMessage: AuthMessage = {username: this.registerUsername, password: this.registerPassword};
    this.webSocketService.registryService(registerMessage);
  }

}