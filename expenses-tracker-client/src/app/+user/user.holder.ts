import {Injectable} from "@angular/core";
import "rxjs/add/observable/interval";
import "rxjs/add/operator/filter";
import {Observable} from "rxjs/Observable";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {UserModel} from "../models/User.model";
import {Entity} from "../models/hateoas/Entity.model";

@Injectable()
export class UserHolder {

    private readonly userSubject = new BehaviorSubject(Entity.empty(UserModel.unknownUser()));

    public getUser$(): Observable<Entity<UserModel>> {
        return this.userSubject;
    }

    public getUser() {
        return this.userSubject.value;
    }

    clearUser() {
        this.setUser(Entity.empty(UserModel.unknownUser()));
    }

    public setUser(user: Entity<UserModel>) {
        this.userSubject.next(user);
    }

}