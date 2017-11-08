import {Component, Input, OnInit} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {ServerMessage, ServerMessageAware} from "../../modules/+server/server.messages";

@Component({
    selector: 'message-panel',
    styleUrls: ['MessagePanel.scss'],
    template: `

        <div class="message-panel">
            <div *ngFor="let message of message$ | async" 
                 class="message-panel__message message-panel__message--{{ ServerMessageType[message.type].toLowerCase() }}">{{ message.text | translate }}</div>
        </div>

    `
})
@ServerMessageAware
export class MessagePanelComponent {

    @Input()
    private message$: Observable<ServerMessage[]>

}
