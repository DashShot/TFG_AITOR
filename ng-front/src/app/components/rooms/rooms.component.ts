import { Component } from '@angular/core';

import { WebSocketService } from '../../services/web-socket.service';
import { RouterLink } from '@angular/router';
import { RoomsMessage } from '../../message-models/rooms-message';

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.css'
})
export class RoomsComponent {

  rooms:Array<RoomsMessage> = [];

  constructor(private webSocketService: WebSocketService){
  }

  listRooms(){
    this.webSocketService.listRooms()
      .then(roomsList => {

        console.log("Rooms recividasComponent: ",roomsList)

        this.rooms = roomsList.map(message => {
          const [room, status] = message.split(": ");
          return { room, status };
        });
        
      })
      .catch(error =>{
        console.error('Error al obtener las rooms');
      })
  }
  
}
