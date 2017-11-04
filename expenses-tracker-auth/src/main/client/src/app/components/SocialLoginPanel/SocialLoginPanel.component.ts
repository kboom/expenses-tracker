import {Component} from "@angular/core";

@Component({
    selector: 'socialLoginPanel',
    providers: [],
    template: `
        
        <div fxLayout="column" fxLayoutGap="5px">
            <a mat-button href="/login/facebook" role="button">Login with Facebook</a>
            <a mat-button href="/login/google" role="button">Login with Google</a>
            <a mat-button href="/login/github" role="button">Login with GitHub</a>
        </div>
    
    `
})
export class SocialLoginPanelComponent {

}
