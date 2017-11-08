import {Component, Input, OnInit} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {ServerMessage} from "../../modules/+server/server.messages";

@Component({
    selector: 'message-panel',
    styleUrls: ['MessagePanel.scss'],
    template: `

        <div class="control-messages">
            <div *ngFor="let message of message$ | async" 
                 class="control-messages__message">{{ message.text | translate }}</div>
        </div>

    `
})
export class MessagePanelComponent {

    @Input()
    private message$: Observable<ServerMessage[]>

}
