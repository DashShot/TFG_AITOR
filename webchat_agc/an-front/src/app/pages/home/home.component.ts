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

export class HomeComponent  {

  router = inject(Router);
  webSocketService = inject(WebSocketService);
  authService = inject(AuthService);

  constructor(){
    this.webSocketService.initConnectionSocket()
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
  
  createRoom(roomId: string ){
    if(roomId){
      this.webSocketService.createRoom(roomId).subscribe(response => {
        console.log('Room created successfully:', response);
        // Refresh the list of rooms after creating a new room
        this.getListRooms();
       
      }, error => {
        if (error.status === 409) {
          console.error('Room already exists:', error.error);
          alert('Room already exists: ' + roomId);
        } else {
          console.error('Error creating room:', error);
        }
      });

    }else{
      console.error('Room name is required');
      alert('Room name is required');
    }
  }
  
}

export interface RoomsMessage{
  room: string;
  status: string;
}