import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {

  //  SI LA ACTUAL IMPLEMENTACIÓN NO REQUIERE  DE  JWT  TOKEN  BORRAR
  //const token = localStorage.getItem('TokenData');

  // const cookieService = inject(CookieService)
  // const xsrf = cookieService.get("X-XSRF-TOKEN");
  
  //localStorage.setItem(req.headers.)

    const cloneReq = req.clone({
      setHeaders:{
        'Content-Type': 'application/json',
       // 'Authorization': `Bearer ${token}`,
        //'X-XSRF-TOKEN': xsrf,
        //'Access-Control-Allow-Credentials': 'true'
      },
      withCredentials: true,

    });
  

  return next(cloneReq)

}
