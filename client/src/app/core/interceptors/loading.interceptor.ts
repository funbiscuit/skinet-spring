import {Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BusyService} from "../services/busy.service";
import {delay, finalize} from "rxjs/operators";

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {

  constructor(private busyService: BusyService) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (request.method == 'POST' && request.url.includes('orders')) {
      return next.handle(request)
    }
    if (!request.url.includes('email-exists')) {
      return next.handle(request)
    }
    this.busyService.busy()
    return next.handle(request).pipe(
      delay(1000),
      finalize(() => {
        this.busyService.idle()
      })
    );
  }
}
