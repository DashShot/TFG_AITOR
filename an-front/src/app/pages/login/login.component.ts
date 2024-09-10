import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  
  isLoginView: boolean = true;

  userRegisterObj: any = {
    username: '',
    password: ''
  }

  userLoginObj: any = {
    username: '',
    password: ''
  }

  router = inject(Router);

  constructor(private authService: AuthService){}

  login(){

    this.authService.onLogin(this.userLoginObj).subscribe((response: any) => {
      
      if(response.body.status == 'SUCCESS'){

        this.authService.setLogged(this.userLoginObj)
        alert(response.body.message)
        this.router.navigateByUrl('/home');

        

      }else{
        alert(response.body.message);   
      }
    }, error => "Worng Credentials")
  }

  register(){

    this.authService.onRegister(this.userRegisterObj).subscribe((response: any) => {
      
      if(response.body.status == 'SUCCESS'){

        this.authService.setLogged(this.userRegisterObj)
        alert(response.body.message)
        this.router.navigateByUrl('/home');
        

      }else{
        alert(response.body.message);   
      }
    }, error => "Worng Credentials")
    
  }
  
}

