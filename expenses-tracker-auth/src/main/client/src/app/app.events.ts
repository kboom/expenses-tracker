import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/filter'
import 'rxjs/add/operator/map'
import {Observable} from "rxjs/Observable";

export interface Message {
    type: string;
    payload: any;
}

export class ApplicationEvent implements Message {

    constructor(readonly type: string,
                readonly payload: any) {
    }

}

export const USER_CREATED_EVENT = "USER_ADDED_EVENT";
export const USER_REMOVED_EVENT = "USER_REMOVED_EVENT";
export const USER_CHANGED_EVENT = "USER_CHANGED_EVENT";

export const SIGNED_IN_FROM_SESSION_EVENT = "SIGNED_IN_FROM_SESSION_EVENT";
export const SIGNED_IN = "SIGNED_IN";

@Injectable()
export default class EventBusService {

    private handler = new Subject<Message>();

    publish(type: string, payload?: any) {
        this.handler.next({ type, payload });
    }

    observeEvents(...types: string[]): Observable<Message> {
        return this.handler
            .filter(message => types.indexOf(message.type) >= 0);
    }

}
