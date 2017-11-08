import {Component, OnInit} from '@angular/core';
import {UserRepository} from "./modules/+user/user.repository";
import {UserHolder} from "./modules/+user/user.holder";
import { TranslateService } from '@ngx-translate/core';
import EventBusService, {SIGNED_IN_FROM_SESSION_EVENT} from "./app.events";
import {PageChangeEventsRouter} from "./app.routes";

@Component({
    selector: 'my-app',
    template: `

        <main>
            <div class="center-in-frame">
                <router-outlet></router-outlet>
            </div>
        </main>

    `
})
export class AppComponent implements OnInit {

    constructor(
        private readonly eventBusService: EventBusService,
        private readonly userRepository: UserRepository,
        private readonly userHolder: UserHolder,
        private readonly pageChangeRouter: PageChangeEventsRouter,
        private readonly translateService: TranslateService
    ) {
        pageChangeRouter.bind();
        translateService.setDefaultLang('en');
    }

    switchLanguage(language: string) {
        this.translateService.use(language);
    }

    ngOnInit(): void {
        this.userRepository.getMe().subscribe((user) => {
            this.userHolder.setUser(user);
            if(user.entity.isKnown()) {
                this.eventBusService.publish(SIGNED_IN_FROM_SESSION_EVENT, user)
            }
        });
    }

}