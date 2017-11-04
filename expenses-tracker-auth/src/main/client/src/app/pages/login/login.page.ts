import {Component} from "@angular/core";

@Component({
    selector: 'loginPage',
    providers: [],
    styleUrls: [ 'login.page.scss' ],
    template: `

        <div class="center-in-frame" >
            <h1 class="login-page__header">Welcome to ExTrackerApp!</h1>
            <div fxLayout="row" fxLayoutGap="30px" fxFlexAlign="space-around center" fxFill>
                <div class="login-page__local-login-box">
                    <localLoginForm></localLoginForm>
                </div>
                <div class="login-page__or">or</div>
                <div class="login-page__social-login-box">
                    <socialLoginPanel></socialLoginPanel>
                </div>
            </div>
        </div>        
    
    `
})
export class LoginPage {

}
