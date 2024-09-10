import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './services/auth/auth.guard';
import { RoomComponent } from './pages/room/room.component';
import { HomeComponent } from './pages/home/home.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'home',
        component: HomeComponent,
        canActivate: [authGuard],

    },
    {
        path: 'room/:room',
        component: RoomComponent,
        canActivate: [authGuard],
    },
    {
        path: '**',
        component: LoginComponent
    }
];
