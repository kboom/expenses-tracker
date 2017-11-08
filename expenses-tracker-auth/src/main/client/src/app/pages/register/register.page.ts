import {Component} from "@angular/core";

@Component({
    selector: 'registerPage',
    providers: [],
    styleUrls: ['register.page.scss'],
    template: `

        <div>
            <h1 class="register-page__header">Register to ExTrackerApp!</h1>
            <localRegistrationForm></localRegistrationForm>
            
            <p>Want to create an account via social profile instead?</p>
            <socialLoginPanel></socialLoginPanel>
        </div>
        
    `
})
export class RegisterPage {

    constructor() {
    }

}
