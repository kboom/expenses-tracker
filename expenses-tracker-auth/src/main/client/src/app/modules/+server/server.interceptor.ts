import {Injectable} from "@angular/core";
import {
    HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,
    HttpResponse
} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/do";
import {Router} from "@angular/router";


@Injectable()
export class DefaultContentTypeInterceptor implements HttpInterceptor {

    constructor(private router: Router) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!req.headers.has('Content-Type')) {
            req = req.clone({headers: req.headers.set('Content-Type', 'application/json')});
        }
        if (!req.headers.has('Accept')) {
            req = req.clone({headers: req.headers.set('Accept', 'application/json')});
        }

        req = req.clone({
            headers: req.headers
                .set('X-Requested-At', new Date().toISOString())
                .set('X-Requested-With', "XMLHttpRequest")
        });

        return next.handle(req).do((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {}
        }, (err: any) => {
            if (err instanceof HttpErrorResponse) {
                if (err.status === 401) {
                    console.log("UNAUTHORIZED");
                    this.router.navigate(['login']);
                }
            }
        });
    }

}