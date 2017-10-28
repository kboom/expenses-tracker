import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/filter'
import 'rxjs/add/operator/map'
import {Observable} from "rxjs/Observable";

export interface Message {
    type: string;
    payload: any;
}

@Injectable()
export class EventBusService {

    private handler = new Subject<Message>();

    publish(type: string, payload?: any) {
        this.handler.next({ type, payload });
    }

    observeEvents(...types: string[]): Observable<Message> {
        return this.handler
            .filter(message => types.indexOf(message.type) >= 0);
    }

}