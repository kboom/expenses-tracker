import {Component} from "@angular/core";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {validatorFor} from "../../services/validation/common.validators";
import {EMAIL_REGEX, PASSWORD_REGEX} from "../../services/validation/validation.rules";
import {LoginService} from "../../services/login.service";

@Component({
    selector: 'localLoginForm',
    providers: [],
    template: `

        <form [formGroup]="loginForm" (ngSubmit)="this.onSubmit($event)" fxLayout='column'>

            <mat-input-container>
                <input matInput formControlName="username" type="text" placeholder="E-mail">
            </mat-input-container>
            <control-messages [control]="loginForm.controls.username"></control-messages>

            <mat-input-container>
                <input matInput formControlName="password" type="password" placeholder="Password">
            </mat-input-container>
            <control-messages [control]="loginForm.controls.password"></control-messages>

            <button type="submit" mat-raised-button
                    [disabled]="!loginForm.valid">Sign in</button>

        </form>
    
    `
})
export class LocalLoginFormComponent {

    public loginForm: FormGroup;

    constructor(private fb: FormBuilder, private readonly loginService: LoginService) {
        this.loginForm = this.fb.group({
            username: ["", Validators.required],
            password: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
        });
    }

    onSubmit(event: Event): void {
        const { username, password } = this.loginForm.value;
        this.loginService.signIn(username, password).subscribe()
    }

}
