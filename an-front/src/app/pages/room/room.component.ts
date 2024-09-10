import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WebSocketService } from '../../services/websocket/web-socket.service';
import { ChatMessage } from '../../message-models/chat-message';
import { FormsModule } from '@angular/forms';
import { FileMessage } from '../../message-models/file-message';

@Component({
  selector: 'app-room',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './room.component.html',
  styleUrl: './room.component.css'
})

export class RoomComponent implements OnInit{

  route = inject(ActivatedRoute)
  router = inject(Router)
  webSocketService = inject(WebSocketService)

  userId = localStorage.getItem("UserLogged ") ||  ''
  currentRoom: string | null = null

  messageSuscription: any
  messagesSesion: any[] = []
  
  msgList: Array<String> = []

  messageObj:  any ={
    content: ''
  }
    
  
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.currentRoom = params.get('room') // room ID 
    });
    this.listenerMessages();
    this.webSocketService.joinRoom(this.currentRoom || '') 
    //this.userId = "user1"
  }

  leftRoom(){
    this.webSocketService.leftRoom(this.currentRoom || '')
    this.messageSuscription.unsubscribe()
    this.router.navigateByUrl('/home');
  }

  listenerMessages(){
    this.messageSuscription = this.webSocketService.getMessageSubject().subscribe((messages: any) => {
      console.log(messages)
      this.messagesSesion = messages.map((item: any) =>  ({
        message_username: item.username,
        message_room: item.room,
        message_type: (item.fileType === 'image/png' || item.fileType === 'image/jpeg') ? 'img' : (item.fileType === 'text/plain' ? 'txt' : 'chatMessage'),
        message_side: item.username === 'Server' ? 'Server' : (item.username === this.userId ? 'sender': 'receiver'),
        message_content: item.filename
        ? `data:${item.fileType};base64,${item.content}` // If it's a file, format content as a base64 image
        : item.content // If it's text, just use the content
      }))
      console.log(this.messagesSesion)
    });
    
    }

  sendMessage(){
    const message: ChatMessage = {content: this.messageObj.content, username: this.userId, room: this.currentRoom || ''}

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


  file: File | null = null;  // Properly initialize file as null

  // Event handler for file input change
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.file = input.files[0];  // Get the selected file
    } else {
      this.file = null;  // Reset file if no file is selected
    }
  }

  sendFile(): void {
    if (!this.file) {
      console.error("No file selected");
      return;
    }else{
      const fileToSend = this.file
      const reader = new FileReader();

      // Convertir el archivo a Base64 cuando se carga
      reader.onload = () => {
        const base64FileData = btoa(reader.result as string);
        const fileMessage: FileMessage = {
          room: this.currentRoom || '',
          username: this.userId,
          filename: fileToSend.name,
          fileType: fileToSend.type,
          content: base64FileData
        }

        this.webSocketService.sendFile(this.currentRoom || '', fileMessage)
          .then(response => {
            console.log(response);
          })
          .catch(error => {
            console.error("Error en el envio:", error);
          });
    
      }

      reader.onerror = (error) => {
        console.error("Error al leer el archivo: ", error);
      };

    // Leer el archivo como una cadena de texto (para convertirlo a Base64)
    reader.readAsBinaryString(this.file); // Alternativamente, puedes usar readAsArrayBuffer y luego convertirlo a Base64
    }
    
  }

}