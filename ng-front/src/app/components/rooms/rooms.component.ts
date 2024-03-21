import { Component } from '@angular/core';

import { WebSocketService } from '../../services/web-socket.service';
import { error } from 'console';
@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.css'
})
export class RoomsComponent {

  rooms:Array<string> = [];

  constructor(private webSocketService: WebSocketService){
  }

  listRooms(){
    this.webSocketService.listRooms()
      .then(roomsList => {
        console.log("Rooms recividas: ",roomsList)
        this.rooms= roomsList
      })
      .catch(error =>{
        console.error('Error al obtener las rooms');
      })
      
  }
}
