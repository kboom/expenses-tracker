import {EventEmitter, Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/defer";
import {AuthenticationModel} from "../models/Authentication.model";
import "rxjs/add/operator/map";
import "rxjs/add/operator/mergeMap";
import "rxjs/add/operator/catch";
import "rxjs/add/observable/throw";
import "rxjs/add/observable/of";
import {HttpClient} from "@angular/common/http";
import {UserHolder} from "./user.holder";
import {pick} from 'lodash-es';
import {UserFactory} from "../models/factory/index";
import {Entity} from "../models/hateoas/Entity.model";
import {UserModel} from "../models/User.model";

enum AuthenticationEvent {
    SIGN_IN_FAILED
}

@Injectable()
export class UserService {

    private authenticationEventsEmitter: EventEmitter<AuthenticationEvent> = new EventEmitter();

    constructor(private userHolder: UserHolder,
                private http: HttpClient,
                private userFactory: UserFactory) {

    }

    public getAuthenticationEvents$() {
        return this.authenticationEventsEmitter.asObservable();
    }

    public tryFetchUser(): Observable<any> {
        const user$ = this.fetchUser();
        user$.subscribe((user) => {
            console.log(`Assuming user ${user}`);
            this.userHolder.setUser(user);
        });
        return user$;
    }

    public changePassword({oldPassword, newPassword}) {
        return this.http.post("api/account/password", JSON.stringify({oldPassword, newPassword}))
            .catch((error: any) => Observable.throw(error));
    }

    public signOut() {
        const logout$ = this.http.post("logout", {});
        logout$.subscribe(() => this.userHolder.clearUser());
        return logout$;
    }

    private fetchUser(): Observable<any> {
        return this.http.get("api/auth/users/me")
            .map((body: any) => this.userFactory.constructEntity(body))
            .catch(() => Observable.of(this.userFactory.constructEntity(UserModel.unknownUser())));
    }

    updateProfile(entity: Entity<UserModel>): Observable<any> {
        return null;
    }
}
