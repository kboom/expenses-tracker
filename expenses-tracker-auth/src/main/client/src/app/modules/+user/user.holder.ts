import {Injectable} from "@angular/core";
import "rxjs/add/observable/interval";
import "rxjs/add/operator/filter";
import {Observable} from "rxjs/Observable";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import Entity from "../../models/hateoas/Entity.model";
import UserModel, {unknownUser} from "../../models/User.model";

@Injectable()
export class UserHolder {

    private readonly userSubject = new BehaviorSubject(Entity.empty(unknownUser()));

    public getUser$(): Observable<Entity<UserModel>> {
        return this.userSubject;
    }

    public getUser() {
        return this.userSubject.value;
    }

    clearUser() {
        this.setUser(Entity.empty(unknownUser()));
    }

    public setUser(user: Entity<UserModel>) {
        this.userSubject.next(user);
    }

}

export enum RoleModel {

    ROLE_ADMIN, ROLE_USER, ROLE_MANAGER

}

export function RoleModelAware(constructor: Function) {
    constructor.prototype.RoleModel = RoleModel;
}