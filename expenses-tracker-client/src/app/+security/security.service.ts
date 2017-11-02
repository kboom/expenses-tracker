import {EventEmitter, Injectable, Injector} from "@angular/core";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/defer";
import {AuthenticationModel} from "../models/Authentication.model";
import {ResponseMappingService} from "./responseMapping.service";
import "rxjs/add/operator/map";
import "rxjs/add/operator/mergeMap";
import "rxjs/add/operator/catch";
import "rxjs/add/observable/throw";
import "rxjs/add/observable/of";
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {UserModel} from "src/app/models/User.model";
import {SecurityContextHolder} from "./security.context";
import {Router} from "@angular/router";
import {Entity} from "../models/hateoas/Entity.model";
import {pick} from 'lodash-es';
import {UserProfile} from "../models/UserProfile.model";
import {UserProfileFactory} from "../models/factory/index";

enum AuthenticationEvent {
    SIGN_IN_FAILED
}

@Injectable()
export class SecurityService {

    private authenticationEventsEmitter: EventEmitter<AuthenticationEvent> = new EventEmitter();

    constructor(private securityContextHolder: SecurityContextHolder,
                private http: HttpClient,
                private responseMapper: ResponseMappingService,
                private userProfileFactory: UserProfileFactory) {

    }

    public getAuthenticationEvents$() {
        return this.authenticationEventsEmitter.asObservable();
    }

    public registerAccount({username, password, email}): Observable<any> {
        return this.http.post("api/registration", JSON.stringify({username, password, email}))
            .catch((error: any) => Observable.throw(error));
    }

    public confirmAccount(confirmationCode): Observable<any> {
        return this.http.post("api/registration/confirmation", JSON.stringify({code: confirmationCode}))
            .catch((error: any) => Observable.throw(error));
    }

    public authenticate({username, password}): Observable<any> {
        const authentication$ = this.doAuthenticate(username, password);
        authentication$.subscribe((authentication) => {
            console.log(`Authenticated user ${authentication}`);
            this.securityContextHolder.setAuthentication(authentication);
        });
        return authentication$;
    }

    public changePassword({oldPassword, newPassword}) {
        return this.http.post("api/account/password", JSON.stringify({oldPassword, newPassword}))
            .catch((error: any) => Observable.throw(error));
    }

    public getProfile(): Observable<Entity<UserProfile>> {
        return this.http.get("api/account")
            .map((body: any) => this.userProfileFactory.constructEntity(body))
            .catch(() => Observable.throw("Could not get profile"));
    }

    public updateProfile(entity: Entity<UserProfile>): Observable<any> {
        return this.http.put("api/account", entity.entity);
    }

    public signOut() {
        this.securityContextHolder.clearAuthentication();
        return Observable.of(null);
    }

    private doAuthenticate(username, password): Observable<any> {
        return this.http.post("api/auth", JSON.stringify({username, password}))
            .map((response: any) => this.responseMapper.mapIntoTokenCodes(response))
            .map(AuthenticationModel.authenticated)
            .catch((error: any) => Observable.throw(error));
    }

    public resetPassword(details: any): Observable<any> {
        return this.http.post("api/account/password/reset", details)
            .catch((error: any) => Observable.throw(error));
    }

    public setNewPasswordAfterReset(newPassword: string, code: string): Observable<any> {
        return this.http.post("api/account/password/reset/confirmation", JSON.stringify({newPassword, code}))
            .catch((error: any) => Observable.throw(error));
    }

}


@Injectable()
export class TokenAddingInterceptor implements HttpInterceptor {

    constructor(private injector: Injector) {

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const securityContext = this.injector.get(SecurityContextHolder).getAuthentication();
        if (securityContext.isAuthenticated()) {
            return next.handle(req.clone({
                headers: req.headers.set('Authorization', securityContext.tokenCodes.accessToken)
            }));
        } else {
            return next.handle(req);
        }
    }

}
