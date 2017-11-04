import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import UserModel from "../models/User.model";
import {LOGIN_URL} from "../app.api";

@Injectable()
export class LoginService {

    constructor(private http: HttpClient) {}

    public signIn(username: string, password: string): Observable<UserModel> {
        return this.http.post(LOGIN_URL, `username=${username}&password=${password}`, {
            headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
        }).catch(() => Observable.throw("Could not sign in"));
    }

}