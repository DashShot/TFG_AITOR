import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WebSocketService } from '../../services/web-socket.service';
import { ChatMessage } from '../../message-models/chat-message';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-room',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './room.component.html',
  styleUrl: './room.component.css'
})

export class RoomComponent implements OnInit {

  userId: string | null = null
  currentRoom: string | null = null
  messagesSesion: any[] = []

  msgList: Array<String> = []

  

  applyForm = new FormGroup({
    username: new FormControl(''),
    content: new FormControl('')
  });

  constructor(private route: ActivatedRoute, private webSocketService: WebSocketService){}
  
  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.currentRoom = params.get('room') // room ID 
    });
    this.listenerMessages();
    this.webSocketService.joinRoom(this.currentRoom || '')
    //this.userId = "user1"
  
  }

  listenerMessages(){
    this.webSocketService.getMessageSubject().subscribe((messages: any) => {
      console.log(messages)
      this.messagesSesion = messages.map((item: any) =>  ({
        ...item,
        message_side: item.username === this.userId ? 'sender': 'receiver'
        //(RESERVAR PARA LOGIN)
      }))
    });
    }

  sendMessage(){
    var username = this.applyForm.value.username ?? '';
    var content = this.applyForm.value.content ?? '';

    const message: ChatMessage = {content: content, username: username, room: this.currentRoom || ''}
    console.log("Mandando mensaje...")
    this.webSocketService.sendMessage(this.currentRoom || '', message)
        .then(response => {
          console.log(response)
        })
        .catch(error => {
          console.error("Error en el envio:", error);
          // Handle any errors during login
        });
  }

  getMessages(){
    this.webSocketService.getMessages(this.currentRoom || '')
      .then(msgList2 => {
        console.log("Mensages recividosComponent: ",msgList2)
        this.msgList = msgList2
      })
      .catch(error =>{
        console.error('Error al obtener las rooms');
      })
  }

}


