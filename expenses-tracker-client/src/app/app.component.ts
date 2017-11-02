/**
 * Angular 2 decorators and services
 */
import {Component, OnInit, ViewEncapsulation} from "@angular/core";
import {AppState} from "./app.service";
import {RoleModelAware} from "./models/Role.model";
import {UserService} from "./+user/user.service";
import {UserHolder} from "./+user/user.holder";

/**
 * App Component
 * Top Level Component
 */
@Component({
    selector: 'app',
    encapsulation: ViewEncapsulation.None,
    styleUrls: [
        './app.component.css'
    ],
    template: `

        <navbar></navbar>

        <main>
            <router-outlet></router-outlet>
        </main>

    `
})
@RoleModelAware
export class AppComponent implements OnInit {
    public angularclassLogo = 'assets/img/angularclass-avatar.png';
    public name = 'Angular 2 Webpack Starter';
    public url = 'https://twitter.com/AngularClass';

    constructor(
        private appState: AppState,
        private readonly userService: UserService,
        private readonly securityContext: UserHolder
    ) {}

    public ngOnInit() {
        console.log('Initial App State', this.appState.state);
        this.userService.tryFetchUser()
    }

}
