import {Router, Routes} from "@angular/router";
import {LoginPage} from "./pages/login/login.page";
import {RegisterPage} from "./pages/register/register.page";
import {WELCOME_PAGE_ROUTE} from "./pages/welcome/welcome.page";
import {Injectable} from "@angular/core";
import EventBusService, {SIGNED_IN, SIGNED_IN_FROM_SESSION_EVENT} from "./app.events";

export const ROUTES: Routes = [
    {path: 'login', component: LoginPage},
    {path: 'register', component: RegisterPage},
    WELCOME_PAGE_ROUTE,
    {path: '', redirectTo: 'login', pathMatch: 'full'}
];

@Injectable()
export class PageChangeEventsRouter {

    constructor(
        private readonly router: Router,
        private readonly eventBusService: EventBusService
    ) {}

    bind() {
        this.eventBusService.observeEvents(SIGNED_IN_FROM_SESSION_EVENT, SIGNED_IN).subscribe(
            () => this.router.navigate([WELCOME_PAGE_ROUTE.path])
        )
    }

}