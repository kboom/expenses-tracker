import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import UserModel, {unknownUser} from "../../models/User.model";
import {USER_URL} from "../../app.api";
import Entity from "../../models/hateoas/Entity.model";
import {UserFactory} from "../../models/index";


@Injectable()
export class UserRepository {

    constructor(private http: HttpClient,
                private userFactory: UserFactory) {}

    public getMe(): Observable<Entity<UserModel>> {
        return this.http.get(USER_URL)
            .map((body: any) => this.userFactory.constructEntity(body))
            .catch(() => Observable.of(Entity.empty(unknownUser())));
    }

}