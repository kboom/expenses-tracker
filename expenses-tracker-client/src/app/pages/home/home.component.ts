import {Component} from "@angular/core";

import {AppState} from "../../app.service";
import {Title} from "./title";

@Component({
    selector: 'home',
    providers: [
        Title
    ],
    styleUrls: ['./home.component.scss'],
    templateUrl: './home.component.html'
})
export class HomeComponent {

    constructor(public appState: AppState,
                public title: Title) {
    }


    public submitState(value: string) {
        console.log('submitState', value);
        this.appState.set('value', value);
    }
}
