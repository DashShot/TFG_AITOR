import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
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


export class AppComponent {

  title = 'an-front';
  authService = inject(AuthService)
  webSocketService = inject(WebSocketService)
  router = inject(Router)
  
  logOut(){

    this.authService.onLogOut().subscribe((response: any) => {
      if(response.status == 'SUCCESS'){
        this.authService.dropLogged()
        this.router.navigateByUrl('/login');
        alert(response.message)
    }else{
      alert(response.message);   
    }
    }, error => "Not able to LogOut") 

    this.webSocketService.closeConnection()
  }

}
