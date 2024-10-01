import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from  '../../../environments/enviroment'



// const headers = new HttpHeaders({
//   'Content-Type': 'application/json',  // Set Content-Type for JSON data
//   'Access-Control-Allow-Credentials': 'true',
//   'Set-Cookie': 'your_authorization_token' // Replace with your token if required
// });

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private isLoggedSubject = new BehaviorSubject<boolean>(this.getInitialAuthState());
  private currentUserSubject = new BehaviorSubject<any>(this.getInitialUser());

  private API_URL = environment.apiUrl + "/auth";

  constructor(private http: HttpClient) { }

  // Inicializa el estado de autenticación basado en el LocalStorage
  private getInitialAuthState(): boolean {
    return localStorage.getItem('IsLogged ') === 'true';
  }

  // Inicializa el usuario actual basado en el LocalStorage
  private getInitialUser(): any {
    return localStorage.getItem('UserLogged ') || null;
  }

  // Devuelve un Observable para saber si el usuario está logueado
  isLoggedIn(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  // Devuelve el usuario actual como un Observable
  getCurrentUser(): Observable<any> {
    return this.currentUserSubject.asObservable();
  }

  // Método para el login
  onLogin(obj: any) {
    return this.http.post(this.API_URL + "/login", obj, { observe: 'response' });
  }

  // Método para el logout
  onLogOut() {
    return this.http.post(this.API_URL + "/logout", { observe: 'response' });
  }

  // Método para el registro
  onRegister(obj: any) {
    return this.http.post(this.API_URL + "/register", obj, { observe: 'response' });
  }

  // Actualiza el estado de autenticación y guarda en el LocalStorage
  setLogged(obj: any) {
    this.isLoggedSubject.next(true);  // Notifica que el usuario está logueado
    this.currentUserSubject.next(obj.username);  // Notifica el usuario actual
    localStorage.setItem("UserLogged ", obj.username);
    localStorage.setItem("IsLogged ", 'true');
  }

  // Desloguea al usuario y actualiza el estado
  dropLogged() {
    this.isLoggedSubject.next(false);  // Notifica que el usuario ha cerrado sesión
    this.currentUserSubject.next(null);  // Limpia el usuario actual
    localStorage.removeItem("UserLogged ");
    localStorage.removeItem("IsLogged ");
  }
}
