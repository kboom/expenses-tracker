import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {SecurityService} from "../../+security/security.service";
import {validatorFor} from "../../validators/common.validators";
import {PASSWORD_REGEX, USERNAME_REGEX} from "../../validators/validation.rules";

@Component({
    selector: 'signInDialog',
    template: `

        <form [formGroup]="loginForm" (ngSubmit)="this.doLogin($event)">

            <h2 md-dialog-title>Please sign in</h2>

            <md-dialog-content fxLayout='column'>

                <md-input-container>
                    <input mdInput formControlName="username" type="text" placeholder="Username">
                </md-input-container>
                <control-messages [control]="loginForm.controls.username"></control-messages>

                <md-input-container>
                    <input mdInput formControlName="password" type="password" placeholder="Password">
                </md-input-container>
                <control-messages [control]="loginForm.controls.password"></control-messages>

            </md-dialog-content>

            <md-dialog-actions fxLayout='row' fxLayoutAlign='space-between center'>
                <button md-button md-dialog-close>Cancel</button>
                <button md-button md-dialog-close="forgot">Forgot password</button>
                <button type="submit" md-button class="mat-primary" [disabled]="!loginForm.valid">Sign in</button>
            </md-dialog-actions>

        </form>

    `
})
export class SignInDialogComponent {

    loginForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private dialogRef: MdDialogRef<SignInDialogComponent>,
        private authService: SecurityService
    ) {
        this.loginForm = this.fb.group({
            username: ["", Validators.required, validatorFor(USERNAME_REGEX)],
            password: ["", Validators.required, validatorFor(PASSWORD_REGEX)]
        });
    }

    doLogin(event): void {
        let formData = this.loginForm.value;
        this.authService.authenticate(formData)
            .subscribe((x) => this.dialogRef.close(x))
    }

}
