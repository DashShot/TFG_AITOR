import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth/auth.service';
import { WebSocketService } from './services/websocket/web-socket.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})


export class AppComponent implements OnInit{

  authService = inject(AuthService);
  webSocketService = inject(WebSocketService);
  router = inject(Router);

  currentUser: any = null; // Variable para almacenar el usuario actual
  isLoggedIn: boolean = false; // Estado de autenticación

  ngOnInit(): void {
    // Suscribirse a los cambios del usuario actual
    this.authService.getCurrentUser().subscribe(user => {
      this.currentUser = user ? user : 'Sesión no Iniciada';
    });

    // Suscribirse a los cambios de estado de autenticación
    this.authService.isLoggedIn().subscribe(isLogged => {
      this.isLoggedIn = isLogged;
    });
  }

  logOut(): void {
    this.authService.onLogOut().subscribe(
      (response: any) => {
        if (response.status == 'SUCCESS') {
          this.authService.dropLogged();  // Actualiza el estado de autenticación
          this.router.navigateByUrl('/login');
          alert(response.message);
        } else {
          alert(response.message);
        }
      },
      error => {
        console.log('No se pudo cerrar sesión');
      }
    );

    this.webSocketService.closeConnection();
  }
}