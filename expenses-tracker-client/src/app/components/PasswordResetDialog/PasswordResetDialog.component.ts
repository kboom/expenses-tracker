import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef, MdSnackBar} from "@angular/material";
import {SecurityService} from "../../+security/security.service";
import {omit} from "lodash-es";
import {validatorFor} from "../../validators/common.validators";
import {EMAIL_REGEX} from "../../validators/validation.rules";

@Component({
    selector: 'password-reset',
    template: `

        <div>

            <form [formGroup]="passwordResetForm" (ngSubmit)="this.doRegister($event)">

                <h2 md-dialog-title>Reset password</h2>

                <md-dialog-content fxLayout='column'>

                    <md-input-container>
                        <input mdInput formControlName="email" type="email" placeholder="Email">
                    </md-input-container>
                    <control-messages [control]="passwordResetForm.controls.email"></control-messages>

                </md-dialog-content>

                <md-dialog-actions fxLayout='row' fxLayoutAlign="space-between">
                    <button md-button md-dialog-close>Cancel</button>
                    <button type="submit" class="mat-primary" md-button [disabled]="!passwordResetForm.valid">
                        Reset
                    </button>
                </md-dialog-actions>

            </form>


        </div>

    `
})
export class PasswordResetDialog {

    passwordResetForm: FormGroup;

    constructor(private fb: FormBuilder,
                private dialogRef: MdDialogRef<PasswordResetDialog>,
                private authService: SecurityService,
                private snackBar: MdSnackBar) {
        this.passwordResetForm = this.fb.group({
            email: ["", Validators.required, validatorFor(EMAIL_REGEX)],
        });
    }

    doRegister(event): void {
        let formData = this.passwordResetForm.value;
        this.authService.resetPassword(formData)
            .subscribe((x) => {
                const snackBarRef = this.snackBar.open("Follow instructions in the e-mail", "Close", {
                    duration: 5000,
                });
                snackBarRef.onAction().subscribe(() => {
                    snackBarRef.dismiss()
                });
                this.dialogRef.close(x)
            })
    }

}
