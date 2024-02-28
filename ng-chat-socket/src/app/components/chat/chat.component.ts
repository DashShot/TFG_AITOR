import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChatMessage } from '../../models/chat-message';
import { ChatService } from '../../services/chat.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  messageInput: string = '';
  userId: string="";
  messageList: any[] = [];

  constructor(private chatService: ChatService,
    private route: ActivatedRoute
    ){

  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params["userId"];
    this.chatService.joinRoom("ABC");

  }

  sendMessage() {
    const chatMessage = {
      message: this.messageInput,
      user: this.userId
    }as ChatMessage
    this.chatService.sendMessage("ABC", chatMessage);
    this.messageInput = '';
  }

  
}