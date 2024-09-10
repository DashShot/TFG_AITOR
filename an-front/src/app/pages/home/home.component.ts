import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { WebSocketService } from '../../services/websocket/web-socket.service';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})

export class HomeComponent {


  router = inject(Router);
  webSocketService = inject(WebSocketService);
  authService = inject(AuthService);

  constructor(){
    this.webSocketService.initConnectionSocket()
  }
  
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


  rooms:Array<RoomsMessage> = [];

  getListRooms() {
    this.webSocketService.getListRooms().subscribe((response: any) => {
      console.log(response.body);
  
      const roomsList: RoomsMessage[] = response.body.map((message: string) => {
        const [room, status] = message.split(': ');
        return { room, status };
      });
  
      this.rooms = roomsList;
    });
  }
  
}

export interface RoomsMessage{
  room: string;
  status: string;
}