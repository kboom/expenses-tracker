import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef, MdSnackBar} from "@angular/material";
import {UserService} from "../../+user/user.service";
import {omit} from "lodash-es";
import {validatorFor} from "../../validators/common.validators";
import {PASSWORD_REGEX} from "../../validators/validation.rules";
import {fieldsAreEqual} from "../../validators/composite.validators";

@Component({
    selector: 'password-change',
    template: `

        <div>
            
            <form [formGroup]="passwordChangeForm" (ngSubmit)="this.doRegister($event)">

                <h2 md-dialog-title>Change password</h2>
                
                <md-dialog-content  fxLayout='column'>

                    <md-input-container>
                        <input mdInput formControlName="oldPassword" type="password" placeholder="Old password">
                    </md-input-container>
                    <control-messages [control]="passwordChangeForm.controls.oldPassword"></control-messages>

                    <md-input-container>
                        <input mdInput formControlName="newPassword" type="password" placeholder="New password">
                    </md-input-container>
                    <control-messages [control]="passwordChangeForm.controls.newPassword"></control-messages>

                    <md-input-container>
                        <input mdInput formControlName="newPasswordRepeated" type="password"
                               placeholder="Repeat new password">
                    </md-input-container>
                    <control-messages [control]="passwordChangeForm.controls.newPasswordRepeated"></control-messages>

                </md-dialog-content>

                <md-dialog-actions fxLayout='row' fxLayoutAlign="space-between">
                    <button md-button md-dialog-close>Cancel</button>
                    <button type="submit" class="mat-primary" md-button [disabled]="!passwordChangeForm.valid">
                        Update
                    </button>
                </md-dialog-actions>

            </form>


        </div>

    `
})
export class PasswordChangeDialog {

    passwordChangeForm: FormGroup;

    constructor(private fb: FormBuilder,
                private dialogRef: MdDialogRef<PasswordChangeDialog>,
                private userService: UserService,
                private snackBar: MdSnackBar) {
        this.passwordChangeForm = this.fb.group({
            oldPassword: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            newPassword: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            newPasswordRepeated: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
        }, {
            validator: fieldsAreEqual("newPassword", "newPasswordRepeated", "password.equality")
        });
    }

    doRegister(event): void {
        let formData = this.passwordChangeForm.value;
        this.userService.changePassword(omit(formData, ['passwordRepeated']))
            .subscribe((x) => {
                const snackBarRef = this.snackBar.open("Password changed", "Close", {
                    duration: 5000,
                });
                snackBarRef.onAction().subscribe(() => {
                    snackBarRef.dismiss()
                });
                this.dialogRef.close(x)
            })
    }

}
