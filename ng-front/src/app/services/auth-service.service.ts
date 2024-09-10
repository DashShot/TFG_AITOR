import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from  'rxjs';
import * as Stomp   from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private serverUrl = 'http://localhost:8080/api/auth'; // Replace with your server URL

  constructor(private http: HttpClient) {}

  login(loginRequest: any): Observable<any> {
    return this.http.post(`${this.serverUrl}/login`, loginRequest);
  }

  refreshToken(refreshToken: string): Observable<any> {
    return this.http.post(`${this.serverUrl}/refresh`, { refreshToken });
  }

  logout(): Observable<any> {
    return this.http.post(`${this.serverUrl}/logout`, {});
  }
}


// @Injectable({
//   providedIn: 'root'
// })
// export class AuthServicesService {

//   constructor() { }

  // login(authMessage: AuthMessage): Observable<any>{
  //   console.log(`Usuario en servicio -> Usuario: ${authMessage.username}, password: ${authMessage.password}`);
  //   return this.http.post("http://0.0.0.0:8080/app/auth/login", authMessage);
  //  }

  //  register(authMessage: AuthMessage): Observable<any>{
  //   console.log(`Usuario en servicio -> Usuario: ${authMessage.username}, password: ${authMessage.password}`);
  //   return this.http.post("http://0.0.0.0:8080/app/auth/register", authMessage);
  //  }

    // this.webSocketService.loginService(loginMessage)
    //     .then(responseMessage => {
    //       console.log("Login response:", responseMessage);
    //       loginResponse = responseMessage;
    //     })
    //     .catch(error => {
    //       console.error("Login error:", error);
    //       // Handle any errors during login
    //     });
    // return loginResponse;
// }





