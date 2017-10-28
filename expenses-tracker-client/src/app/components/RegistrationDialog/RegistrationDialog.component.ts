import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef, MdSnackBar} from "@angular/material";
import {SecurityService} from "../../+security/security.service";
import {omit} from "lodash-es";
import {validatorFor} from "../../validators/common.validators";
import {EMAIL_REGEX, USERNAME_REGEX, PASSWORD_REGEX} from "../../validators/validation.rules";
import {fieldsAreEqual} from "../../validators/composite.validators";

@Component({
    selector: 'signInDialog',
    template: `

        <form [formGroup]="registrationForm" (ngSubmit)="this.doRegister($event)">

            <h2 md-dialog-title>Please sign in</h2>

            <md-dialog-content fxLayout='column'>

                <md-input-container>
                    <input mdInput formControlName="username" type="text" placeholder="Username">
                </md-input-container>
                <control-messages [control]="registrationForm.controls.username"></control-messages>

                <md-input-container>
                    <input mdInput formControlName="email" type="email" placeholder="E-mail">
                </md-input-container>
                <control-messages [control]="registrationForm.controls.email"></control-messages>

                <md-input-container>
                    <input mdInput formControlName="password" type="password" placeholder="Password">
                </md-input-container>
                <control-messages [control]="registrationForm.controls.password"></control-messages>

                <md-input-container>
                    <input mdInput formControlName="passwordRepeated" type="password" placeholder="Repeat password">
                </md-input-container>
                <control-messages [control]="registrationForm.controls.passwordRepeated"></control-messages>

            </md-dialog-content>

            <md-dialog-actions fxLayout='row' fxLayoutAlign='space-between center'>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" class="mat-primary" md-button [disabled]="!registrationForm.valid">Create account</button>
            </md-dialog-actions>

        </form>

    `
})
// https://codecraft.tv/courses/angular/forms/model-driven-validation/
export class RegistrationDialogComponent {

    registrationForm: FormGroup;

    constructor(private fb: FormBuilder,
                private dialogRef: MdDialogRef<RegistrationDialogComponent>,
                private authService: SecurityService,
                private snackBar: MdSnackBar) {
        this.registrationForm = this.fb.group({
            username: ["", Validators.required, validatorFor(USERNAME_REGEX)],
            email: ["", Validators.required, validatorFor(EMAIL_REGEX)],
            password: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            passwordRepeated: ["", Validators.required],
        }, {
            validator: fieldsAreEqual("password", "passwordRepeated", "password.equality")
        });
    }

    doRegister(event): void {
        let formData = this.registrationForm.value;
        this.authService.registerAccount(omit(formData, ['passwordRepeated']))
            .subscribe((x) => {
                const snackBarRef = this.snackBar.open("Please check your mailbox", "Close", {
                    duration: 5000,
                });
                snackBarRef.onAction().subscribe(() => {
                    snackBarRef.dismiss()
                });
                this.dialogRef.close(x)
            })
    }

}
