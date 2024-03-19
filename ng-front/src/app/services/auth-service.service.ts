import { Injectable } from '@angular/core';
import { AuthMessage } from '../message-models/auth-message';
import { WebSocketService } from './web-socket.service';

@Injectable({
  providedIn: 'root'
})
export class AuthServicesService {

  constructor(private webSocketService: WebSocketService) { }


  login(username: string, password: string){
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
}
