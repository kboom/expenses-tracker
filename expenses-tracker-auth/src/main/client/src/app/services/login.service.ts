import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import UserModel from "../models/User.model";
import {USER_URL} from "../app.api";
import {UserHolder} from "../modules/+user/user.holder";
import {UserFactory} from "../models/index";
import EventBusService, {SIGNED_IN} from "../app.events";

@Injectable()
export class LoginService {

    constructor(private readonly http: HttpClient,
                private readonly userFactory: UserFactory,
                private readonly userHolder: UserHolder,
                private readonly eventBus: EventBusService) {
    }

    public signIn(username: string, password: string): Observable<UserModel> {
        const getUser$ = this.http.get(USER_URL, {
            headers: new HttpHeaders().set('Authorization', `Basic ${btoa(`${username}:${password}`)}`)
        }).map((body: any) => this.userFactory.constructEntity(body))
            .catch(() => Observable.throw("Could not sign in"));

        getUser$.subscribe((userEntity) => {
            this.userHolder.setUser(userEntity);
            this.eventBus.publish(SIGNED_IN, userEntity);
        });

        return getUser$;
    }

}