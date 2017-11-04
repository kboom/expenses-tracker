import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import UserModel, {unknownUser} from "../models/User.model";
import {LOGIN_URL, USER_URL} from "../app.api";

@Injectable()
export class LoginService {

    constructor(private http: HttpClient) {}

    public getUser(): Observable<UserModel> {
        return  this.http.get(USER_URL).catch(() => Observable.of(unknownUser()));
    }

    public signIn(username: string, password: string): Observable<UserModel> {
        return this.http.post(LOGIN_URL, `username=${username}&password=${password}`, {
            headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
        }).flatMap(() => {
            return this.http.get(USER_URL)
        }).catch(() => Observable.throw("Could not sign in"));
    }

}