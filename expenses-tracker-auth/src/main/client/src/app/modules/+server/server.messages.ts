import {Injectable, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {ActivatedRoute, Params} from '@angular/router';
import "rxjs/add/operator/filter";
import {Subject} from "rxjs/Subject";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

export interface ServerMessage {
    readonly type: ServerMessageType,
    readonly text: String
}

export enum ServerMessageType {
    ERROR
}

export function ServerMessageAware(constructor: Function) {
    constructor.prototype.ServerMessageType = ServerMessageType;
}

@Injectable()
export default class ServerMessages {

    private readonly serverMessages$ = new BehaviorSubject<ServerMessage[]>([]);

    constructor(private activatedRoute: ActivatedRoute) {
        this.activatedRoute.queryParams.subscribe((params: Params) => {
            const message = params['error'];
            this.serverMessages$.next([{
                type: ServerMessageType.ERROR,
                text: message
            }])
        });
    }

    public get messages$(): Observable<ServerMessage[]> {
        return this.serverMessages$.asObservable()
    }

}