import {Component, EventEmitter, Output} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {extend, includes, map, pick, transform} from "lodash-es";
import {PASSWORD_REGEX} from "../../validators/validation.rules";
import {validatorFor} from "../../validators/common.validators";
import {fieldsAreEqual} from "../../validators/composite.validators";

@Component({
    selector: 'new-password-form',
    template: `

        <form id="newPasswordForm" [formGroup]="newPasswordForm" (ngSubmit)="submit($event)" fxLayout='column'>

            <md-input-container>
                <input mdInput formControlName="password" type="password" placeholder="Password">
            </md-input-container>
            <control-messages [control]="newPasswordForm.controls.password"></control-messages>

            <md-input-container>
                <input mdInput formControlName="passwordRepeated" type="password" placeholder="Repeat password">
            </md-input-container>
            <control-messages [control]="newPasswordForm.controls.passwordRepeated"></control-messages>

            <div>
                <button type="submit" fxFlex="end" class="mat-primary" md-button [disabled]="!newPasswordForm.valid">Reset password</button>
            </div>
            
        </form>

    `
})
export class SetNewPasswordForm {

    private newPasswordForm: FormGroup;

    @Output()
    private onSubmit: EventEmitter<any> = new EventEmitter();

    constructor(private fb: FormBuilder) {
        this.newPasswordForm = this.fb.group({
            password: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            passwordRepeated: ["", Validators.required],
        }, {
            validator: fieldsAreEqual("password", "passwordRepeated", "password.equality")
        });
    }

    submit(event): void {
        const formData = this.newPasswordForm.value;
        this.onSubmit.emit(formData);
    }

}
