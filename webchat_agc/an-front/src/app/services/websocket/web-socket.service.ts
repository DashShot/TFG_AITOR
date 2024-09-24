import { inject, Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';
import SockJS from 'sockjs-client';
import { ChatMessage } from '../../message-models/chat-message';
import { HttpClient } from '@angular/common/http';
import { FileMessage } from '../../message-models/file-message';

@Injectable({
  providedIn: 'root'
})


export class WebSocketService {

  private stompClient: Client | undefined =  undefined
  private http = inject(HttpClient)

  private messageSubject: BehaviorSubject<ChatMessage[]> = new BehaviorSubject<ChatMessage[]>([]);

  roomSuscription: any
  
  constructor() {}

  initConnectionSocket(){
    // const xsrf = this.cookieService.get("XSRF-TOKEN");
    this.stompClient = new Client({
      brokerURL: "wss://localhost:8443/socket",
      webSocketFactory: mySocketFactory,
      // connectHeaders: {
      //   "X-XSRF-TOKEN": xsrf
      // },
      debug: function (str) {
        console.log(str);
      },
      onConnect: function (){
        console.log("CLIENTE CREADOO")
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      // Increase WebSocket Frame Size
      maxWebSocketChunkSize: 128 * 1024 * 1024 // Increase chunk size 

    });

    this.stompClient.onStompError = function (frame) {
      // Will be invoked in case of error encountered at Broker
      // Bad login/passcode typically will cause an error
      // Complaint brokers will set `message` header with a brief message. Body may contain details.
      // Compliant brokers will terminate the connection after any error
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };
    this.stompClient.activate();
    if(this.stompClient.connected) console.log("STOMP CONNECTED");

  }

  closeConnection(){
    if(this.stompClient){
      this.stompClient.deactivate()
    }
  }


  getListRooms(){
      return this.http.get("https://localhost:8443/listRooms", { observe: 'response' })
  }
  
  createRoom(roomId: string){
      return  this.http.post("https://localhost:8443/createRoom",  roomId )
  }

  joinRoom(roomId: string)  {
    if(this.stompClient){
  
      this.roomSuscription = this.stompClient.subscribe(`/topic/${roomId}`, (messages: any) => {

        const messageContent = JSON.parse(messages.body);
        const currentMessage = this.messageSubject.getValue();

        currentMessage.push(messageContent);
        this.messageSubject.next(currentMessage);
  
      })

      const newUser: ChatMessage = {room: roomId, content: `${localStorage.getItem("UserLogged ")} ha  entrado en la sala`, username: 'Server', timeStamp: new Date()}
      this.stompClient.publish({destination: `/app/${roomId}`, body: JSON.stringify(newUser)});

    }
  }

  leftRoom(roomId: string){
    if(this.stompClient){
      const disconectUser: ChatMessage = {content: `${localStorage.getItem("UserLogged ")} ha abandonado la sala`, username: 'Server', room: roomId, timeStamp: new Date()}
      this.stompClient.publish({destination: `/app/${roomId}`, body: JSON.stringify(disconectUser)});

      this.roomSuscription.unsubscribe();
    }
  }

  sendMessage(roomId: string, message: ChatMessage): Promise<string>{
    return new Promise<string>( (resolve) => {
      if(this.stompClient)
        this.stompClient.publish({destination: `/app/${roomId}`,body: JSON.stringify(message)});
        console.log("Mensaje enviado a /app/"+roomId);
        resolve("Mensaje Enviado");
    });
  }

  sendFile(roomId: string, file: FileMessage) {
    return new Promise<string>( (resolve) => {
  if(this.stompClient)
      this.stompClient.publish({destination: `/app/${roomId}/files`,body: JSON.stringify(file)});
      console.log("Archivo enviado a /app/"+roomId);
      resolve("Archivo Enviado");
    });
  }

  getMessageSubject(){
    return this.messageSubject.asObservable();
  }

  getMessages(roomId: string): Promise<string>{
    return new Promise<string>((resolve) => {

      if(this.stompClient){

        this.stompClient.publish({destination: `/app/${roomId}/getMessages`, body: '5'});
        console.log("Mensaje enviado para obtener mensajes de la sala", roomId);

        resolve("Mensages pedidos");
      }
    });
   
  }

  

}

export function mySocketFactory() {
  return new SockJS('https://localhost:8443/socket');
}
