import { Routes } from '@angular/router';

import { RoomsComponent } from './components/rooms/rooms.component';
import { AuthComponent } from './components/auth/auth.component';

export const routes: Routes = [
    {path: '', component: AuthComponent},
    {path:'roomSelection', component: RoomsComponent }
    //{ path: '**', component: PageNotFoundComponent } // 404 page
];
