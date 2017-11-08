import {Component} from "@angular/core";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {validatorFor} from "../../services/validation/common.validators";
import {
    EMAIL_REGEX, FIRST_NAME_REGEX, LAST_NAME_REGEX,
    PASSWORD_REGEX
} from "../../services/validation/validation.rules";
import {fieldsAreEqual} from "../../services/validation/composite.validators";

@Component({
    selector: 'localRegistrationForm',
    providers: [],
    template: `

        <form #f [formGroup]="registrationForm" role="form" method="POST" action="api/registration" fxLayout='column'>

            <mat-input-container>
                <input matInput formControlName="username" name="username" type="text" placeholder="E-mail">
            </mat-input-container>
            <control-messages [control]="registrationForm.controls.username"></control-messages>

            <mat-input-container>
                <input matInput formControlName="password" name="password" type="password" placeholder="Password">
            </mat-input-container>
            <control-messages [control]="registrationForm.controls.password"></control-messages>

            <mat-input-container>
                <input matInput formControlName="repeatedPassword" name="repeatedPassword" type="password"
                       placeholder="Repeat password">
            </mat-input-container>
            <control-messages [control]="registrationForm.controls.repeatedPassword"></control-messages>

            <mat-input-container>
                <input matInput formControlName="firstName" name="first_name" type="text" placeholder="First name">
            </mat-input-container>
            <control-messages [control]="registrationForm.controls.firstName"></control-messages>

            <mat-input-container>
                <input matInput formControlName="lastName" name="last_name" type="text" placeholder="Last name">
            </mat-input-container>
            <control-messages [control]="registrationForm.controls.lastName"></control-messages>

            <button type="submit" mat-raised-button class="mat-primary" (click)="f.submit()"
                    [disabled]="!registrationForm.valid">Register
            </button>

        </form>

    `
})
export class LocalRegistrationFormComponent {

    public registrationForm: FormGroup;

    constructor(private fb: FormBuilder) {
        this.registrationForm = this.fb.group({
            username: ["", Validators.required],
            password: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            repeatedPassword: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            firstName: ["", Validators.required, validatorFor(FIRST_NAME_REGEX)],
            lastName: ["", Validators.required, validatorFor(LAST_NAME_REGEX)]
        }, {
            validator: fieldsAreEqual("password", "repeatedPassword", "password.equality")
        });
    }

}
