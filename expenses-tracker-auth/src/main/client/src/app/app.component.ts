import {Component, OnInit} from '@angular/core';
import {UserRepository} from "./modules/+user/user.repository";
import {UserHolder} from "./modules/+user/user.holder";

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
        private readonly userRepository: UserRepository,
        private readonly userHolder: UserHolder
    ) {

    }

    ngOnInit(): void {
        this.userRepository.getMe().subscribe((user) => {
            this.userHolder.setUser(user)
        });
    }

}