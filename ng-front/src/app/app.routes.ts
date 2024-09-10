import { Routes } from '@angular/router';

import { AuthComponent } from './components/auth/auth.component';
import { RoomsComponent } from './components/rooms/rooms.component';
import { RoomComponent } from './components/room/room.component';

export const routes: Routes = [
    {path: 'login', component: AuthComponent},
    {path: '', redirectTo: 'login', pathMatch : 'full'},
    {path:'roomSelection', component: RoomsComponent },
    {path:'room/:room', component: RoomComponent}
    //{ path: '**', component: PageNotFoundComponent } // 404 page
];
