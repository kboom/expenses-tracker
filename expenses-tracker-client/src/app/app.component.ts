/**
 * Angular 2 decorators and services
 */
import {Component, OnInit, ViewEncapsulation} from "@angular/core";
import {AppState} from "./app.service";
import {SecurityService} from "./+security/security.service";
import {RoleModelAware} from "./models/Role.model";
import {SecurityContextHolder} from "./+security/security.context";

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
        private readonly securityService: SecurityService,
        private readonly securityContext: SecurityContextHolder
    ) {}

    public ngOnInit() {
        console.log('Initial App State', this.appState.state);
    }

}
