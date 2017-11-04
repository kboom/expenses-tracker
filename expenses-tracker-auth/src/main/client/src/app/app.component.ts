import {Component} from '@angular/core';

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
export class AppComponent {
}