import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { ChatMessage } from '../models/chat-message';

@Injectable({
  providedIn: 'root'
})

export class WebSocketService {
  
  private stompClient: any 

  constructor() { 
    this.initConnectionSocket();
  }

  initConnectionSocket(){
    const url = "//localhost:8081/ws"
    const socket = new SockJS(url)
    this.stompClient = Stomp.over(socket);
  }

  joinRoom(roomId: string){
    this.stompClient.connect({}  , ()=>{
      this.stompClient.subscribe(`/topic/${roomId}` , (messages: any) => {
        const message = JSON.parse(messages.body);
        console.log('Message received ',message);
      }) //revisarWEBSOCKETCOntroller 
    })
  }
  
  sendMessage(roomId :string, chatMessage: ChatMessage){
    this.stompClient.send(`/app/chat/${roomId}`,{}, JSON.stringify(chatMessage));
    } 

}
