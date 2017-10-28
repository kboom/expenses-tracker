import {Injectable} from "@angular/core";
import {AuthenticationModel} from "../models/Authentication.model";
import "rxjs/add/observable/interval";
import "rxjs/add/operator/filter";
import {Observable} from "rxjs/Observable";
import {LocalStorageService} from "angular-2-local-storage";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {TokenCodesModel} from "../models/Token.model";

const ACCESS_TOKEN_LOCAL_STORAGE_KEY = "security::accessToken";

@Injectable()
export class SecurityContextHolder {

    private readonly authenticationSubject = new BehaviorSubject(AuthenticationModel.noAuthentication());

    constructor(private localStorageService: LocalStorageService) {
        this.restoreAuthentication();
        this.authenticationSubject
            .filter((authentication) => authentication.isAuthenticated())
            .subscribe((authentication) => this.storeAuthentication(authentication))
    }

    public getAuthentication$(): Observable<AuthenticationModel> {
        return this.authenticationSubject;
    }

    public getAuthentication() {
        return this.authenticationSubject.value;
    }

    clearAuthentication() {
        this.setAuthentication(AuthenticationModel.noAuthentication());
        this.localStorageService.clearAll("security::*")
    }

    public setAuthentication(authentication: AuthenticationModel) {
        this.authenticationSubject.next(authentication);
    }

    private restoreAuthentication() {
        const storedToken = this.localStorageService.get<string>(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
        if (storedToken) {
            this.authenticationSubject.next(AuthenticationModel.authenticated(new TokenCodesModel(storedToken)))
        } else {
            this.authenticationSubject.next(AuthenticationModel.noAuthentication());
        }
    }

    private storeAuthentication(authentication: AuthenticationModel) {
        this.localStorageService.set(ACCESS_TOKEN_LOCAL_STORAGE_KEY, authentication.tokenCodes.accessToken);
    }

}