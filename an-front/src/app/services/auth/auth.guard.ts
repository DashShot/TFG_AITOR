import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { map, take } from 'rxjs/operators';


export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Usar isLoggedSubject para determinar si el usuario está logueado
  return authService.isLoggedIn().pipe(
    take(1),  // Solo tomar el primer valor emitido
    map(isLogged => {
      if (isLogged) {
        return true;
      } else {
        router.navigate(['/login']);
        return false;
      }
    })
  );
};