import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WebSocketService } from '../../services/websocket/web-socket.service';
import { ChatMessage } from '../../message-models/chat-message';
import { FormsModule } from '@angular/forms';
import { FileMessage } from '../../message-models/file-message';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-room',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './room.component.html',
  styleUrl: './room.component.css'
})

export class RoomComponent implements OnInit, OnDestroy {

  route = inject(ActivatedRoute);
  router = inject(Router);
  webSocketService = inject(WebSocketService);
  authService = inject(AuthService);  

  userId: string = '';  
  currentRoom: string | null = null;

  messageSuscription: any;
  messagesSesion: any[] = [];
  
  msgList: Array<String> = [];

  messageObj: any = {
    content: ''
  };

  ngOnInit(): void {
    // Suscribirse a los cambios del usuario actual
    this.authService.getCurrentUser().subscribe(user => {
      this.userId = user ? user : '';  // Si hay usuario, lo asignamos; si no, dejamos una cadena vacía
    });

    this.route.paramMap.subscribe(params => {
      this.currentRoom = params.get('room'); // room ID 
    });

    this.listenerMessages();
    this.webSocketService.joinRoom(this.currentRoom || '');
  }

  // Se ejecuta cuando el componente está a punto de ser destruido
  ngOnDestroy(): void {
    this.leftRoom();
  }

  leftRoom(): void {
    this.webSocketService.leftRoom(this.currentRoom || '');
    this.messageSuscription.unsubscribe();
    this.router.navigateByUrl('/home');
  }

  listenerMessages(): void {
    this.messageSuscription = this.webSocketService.getMessageSubject().subscribe((messages: any) => {
      console.log(messages);
      this.messagesSesion = messages.map((item: any) => ({
        message_username: item.username,
        message_room: item.room,
        message_type: (item.fileType === 'image/png' || item.fileType === 'image/jpeg') ? 'img' : (item.fileType === 'text/plain' ? 'txt' : 'chatMessage'),
        message_side: item.username === 'Server' ? 'Server' : (item.username === this.userId ? 'Yours' : 'Others'),
        message_content: item.filename
          ? `data:${item.fileType};base64,${item.content}` // Si es un archivo, formatea el contenido como una imagen base64
          : item.content, // Si es texto, simplemente usa el contenido
        message_timeStamp: new Date(item.timeStamp).toUTCString()
      }));
      console.log(this.messagesSesion);
    });
  }

  sendMessage(): void {
    if (!this.userId) {
      console.error("No hay usuario autenticado");
      return;
    }

    const message: ChatMessage = {
      room: this.currentRoom || '',
      username: this.userId,
      content: this.messageObj.content,
      timeStamp: new Date()
    };

    console.log("Mandando mensaje...");
    this.webSocketService.sendMessage(this.currentRoom || '', message)
      .then(response => {
        console.log(response);
      })
      .catch(error => {
        console.error("Error en el envio:", error);
      });
  }

  getMessages(): void {
    this.webSocketService.getMessages(this.currentRoom || '')
      .then(response => {
        console.log(response);
      })
      .catch(error => {
        console.error("Error en el envio:", error);
      });
  }

  file: File | null = null;  // Inicializamos el archivo como null

  // Manejador de eventos para el cambio en el input de archivo
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.file = input.files[0];  // Obtenemos el archivo seleccionado
    } else {
      this.file = null;  // Reseteamos el archivo si no se selecciona nada
    }
  }

  sendFile(): void {
    if (!this.file) {
      console.error("No hay archivo seleccionado");
      return;
    }

    const fileToSend = this.file;
    const reader = new FileReader();

    // Convertir el archivo a Base64 cuando se carga
    reader.onload = () => {
      const base64FileData = btoa(reader.result as string);
      const fileMessage: FileMessage = {
        room: this.currentRoom || '',
        username: this.userId,
        filename: fileToSend.name,
        fileType: fileToSend.type,
        content: base64FileData,
        timeStamp: new Date()
      };

      this.webSocketService.sendFile(this.currentRoom || '', fileMessage)
        .then(response => {
          console.log(response);
        })
        .catch(error => {
          console.error("Error en el envio:", error);
        });
    };

    reader.onerror = (error) => {
      console.error("Error al leer el archivo: ", error);
    };

    // Leer el archivo como cadena de texto (para convertirlo a Base64)
    reader.readAsBinaryString(this.file); 
  }
}