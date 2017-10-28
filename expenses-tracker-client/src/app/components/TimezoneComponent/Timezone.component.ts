import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import * as moment from 'moment';
import {Observable} from "rxjs/Observable";

interface Timezone {
    name: string
    differenceToGMT: number
    city: string
}

@Component({
    selector: 'timezone',
    styleUrls: ['Timezone.scss'],
    template: `

        <md-card>
            <md-card-header>
                <md-card-title-group>
                    <md-card-title>{{ timezone.name }}</md-card-title>
                    <md-card-subtitle><strong>{{ timezone.locationName }}</strong>, GMT
                        {{ (timezone.differenceToGMT >= 0 ? "+" : "") + timezone.differenceToGMT }}
                    </md-card-subtitle>
                </md-card-title-group>
            </md-card-header>
            <md-card-content>
                <analog-clock [dateTime]="dateTime.add(timezone.differenceToGMT, 'hours').utc(false)"></analog-clock>
                <div class="timezone__time">{{ time$ | async | amDateFormat: 'dddd' }}</div>
            </md-card-content>
            <md-card-actions fxLayout='row' fxLayoutAlign='space-between center'>
                <button md-button (click)="onEdit.emit()">Edit</button>
                <button md-button (click)="onDelete.emit()">Delete</button>
            </md-card-actions>
        </md-card>

    `
})
export class TimezoneComponent implements OnInit {

    private time$ = Observable.interval(1000)
        .map((minute) => moment().add(this.timezone.differenceToGMT, "hours").utc(false));

    private dateTime = moment();

    @Input()
    private timezone: Timezone;

    @Output()
    private onEdit: EventEmitter<any> = new EventEmitter();

    @Output()
    private onDelete: EventEmitter<any> = new EventEmitter();

    ngOnInit(): void {
        // this.dateTime = moment().add(this.timezone.differenceToGMT, "hours");
    }

}