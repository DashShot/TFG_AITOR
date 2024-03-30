import { Component, OnInit , ViewChild, ElementRef} from '@angular/core';
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

  currentRoom: string | null = null
  msgList: Array<String> = []

  messagesSesion: any[] = []

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

  sendMessage(){
    var username = this.applyForm.value.username ?? '';
    var content = this.applyForm.value.content ?? '';

    const message: ChatMessage = {content: content, username: username, room: this.currentRoom || ''}
    console.log("Mandando mensaje...")
    this.webSocketService.sendMessage(message)
        .then(response => {
          console.log(response)
        })
        .catch(error => {
          console.error("Error en el envio:", error);
          // Handle any errors during login
        });
  }

  
  @ViewChild('messageArea') messageAreaRef!: ElementRef<any>
  listenerMessages() {
    this.webSocketService.getMessageSubject().subscribe((messages: any) =>
      this.messagesSesion = messages.map((item: any) => ({
        ...item,
        //lado
        //
        //
      }))
      );

    
    //const message = JSON.parse(payload.body);

    // const messageElement = document.createElement('li');

    // if (message.type === 'JOIN') {
    //   messageElement.classList.add('event-message');
    //   message.content = message.sender + ' joined!';
    // } else if (message.type === 'LEAVE') {
    //   messageElement.classList.add('event-message');
    //   message.content = message.sender + ' left!';
    // } else {
    //   messageElement.classList.add('chat-message');

    //   const avatarElement = document.createElement('i');
    //   avatarElement.textContent = message.sender[0]; // Use textContent instead of createTextNode
    //   //avatarElement.style.backgroundColor = getAvatarColor(message.sender);
    //   messageElement.appendChild(avatarElement);

    //   const usernameElement = document.createElement('span');
    //   usernameElement.textContent = message.sender; // Use textContent instead of createTextNode
    //   messageElement.appendChild(usernameElement);
    // }

    // const textElement = document.createElement('p');
    // textElement.textContent = message.content; // Use textContent instead of createTextNode
    // messageElement.appendChild(textElement);

    // // Access the messageArea element using @ViewChild
    // this.messageAreaRef.nativeElement.appendChild(messageElement);
    // this.messageAreaRef.nativeElement.scrollTop = this.messageAreaRef.nativeElement.scrollHeight;
  }

}
