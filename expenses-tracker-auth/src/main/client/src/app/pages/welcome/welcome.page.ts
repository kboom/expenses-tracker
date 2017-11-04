import {Component, Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from "@angular/router";
import {UserHolder} from "../../modules/+user/user.holder";

@Component({
    selector: 'welcomePage',
    providers: [],
    template: `

        <div>
            Welcome
        </div>
    
    `
})
export class WelcomePage {

    constructor() {}

}

@Injectable()
export class CanActivateWelcomePage implements CanActivate {

    constructor(private userHolder: UserHolder) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.userHolder.getUser().entity.isKnown()
    }

}

export const WELCOME_PAGE_ROUTE = {
    path: 'welcome',
    component: WelcomePage,
    canActivate: [CanActivateWelcomePage]
};

