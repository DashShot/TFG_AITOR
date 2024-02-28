import { Component } from '@angular/core';


@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})

export class AuthComponent {
  loginUsername = '';
  loginPassword = '';
  registerUsername = '';
  registerPassword = '';
  showRegister = true;

  login() {
    // Handle login action
  }

  register() {
    // Handle register action
  }

  showRegisterForm() {
    this.showRegister = true;
  }

  showLoginForm() {
    this.showRegister = false;
  }
}