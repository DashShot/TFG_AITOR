import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { BehaviorSubject } from 'rxjs';

const API = "https://localhost:8443/api/auth"

// const headers = new HttpHeaders({
//   'Content-Type': 'application/json',  // Set Content-Type for JSON data
//   'Access-Control-Allow-Credentials': 'true',
//   'Set-Cookie': 'your_authorization_token' // Replace with your token if required
// });

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private isLoogged = false


  constructor(private http: HttpClient) { 
    // NECESARIO =?=?
    // const xsrfTokenRequest = this.http.get(API + '/xsrf-token', { withCredentials: true }).subscribe( (response : any) =>
    //   this.cookieService.set("X-XSRF-TOKEN",response.body)
    // )

  }

  onLogin(obj: any){
    return this.http.post(API + "/login", obj, { observe: 'response' })
  }

  onLogOut(){
    return this.http.post(API + "/logout" ,{observe: 'response'})
  }

  onRegister(obj: any){
    return this.http.post(API + "/register", obj, { observe: 'response' })
  }

  isLoggedIn(){
    return this.isLoogged 
  }

  //MANEJO DE  SESION POR LocalStorage
  setLogged(obj: any){
    this.isLoogged = true
    localStorage.setItem("UserLogged ",obj.username);
    localStorage.setItem("IsLogged ", 'true')
  }

  dropLogged(){
    this.isLoogged = false
    localStorage.removeItem("UserLogged ");
    localStorage.removeItem("IsLogged ");
  }

}