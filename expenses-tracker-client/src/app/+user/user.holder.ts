import {Injectable} from "@angular/core";
import "rxjs/add/observable/interval";
import "rxjs/add/operator/filter";
import {Observable} from "rxjs/Observable";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {UserModel} from "../models/User.model";

@Injectable()
export class UserHolder {

    private readonly userSubject = new BehaviorSubject(UserModel.unknownUser());

    public getUser$(): Observable<UserModel> {
        return this.userSubject;
    }

    public getUser() {
        return this.userSubject.value;
    }

    clearUser() {
        this.setUser(UserModel.unknownUser());
    }

    public setUser(user: UserModel) {
        this.userSubject.next(user);
    }

}