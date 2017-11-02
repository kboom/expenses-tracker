import {Injectable} from "@angular/core";
import {
    HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,
    HttpResponse
} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/do";
import {Router} from "@angular/router";
import {UserHolder} from "../+user/user.holder";
import {MdSnackBar} from "@angular/material";

@Injectable()
export class Server {
    public baseURL = 'http://localhost:3000/api';
}

@Injectable()
export class DefaultContentTypeInterceptor implements HttpInterceptor {

    constructor(private router: Router,
                private userHolder: UserHolder,
                private snackBar: MdSnackBar) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!req.headers.has('Content-Type')) {
            req = req.clone({headers: req.headers.set('Content-Type', 'application/json')});
        }
        if (!req.headers.has('Accept')) {
            req = req.clone({headers: req.headers.set('Accept', 'application/json')});
        }
        return next.handle(req).do((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {}
        }, (err: any) => {
            if (err instanceof HttpErrorResponse) {
                if (err.status === 401) {
                    console.log("UNAUTHORIZED");
                    this.userHolder.clearUser();
                    const snackBarRef = this.snackBar.open(err.url.endsWith("/auth")
                        ? "Incorrect credentials. Try again."
                        : "You've been logged out due to inactivity",
                        "Close", {
                        duration: 5000,
                    });
                    snackBarRef.onAction().subscribe(() => {
                        snackBarRef.dismiss()
                    });
                    this.router.navigate(['home']);
                }
            }
        });
    }

}