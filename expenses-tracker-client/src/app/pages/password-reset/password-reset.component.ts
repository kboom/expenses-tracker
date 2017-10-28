import {Component, OnDestroy, OnInit} from "@angular/core";
import {Title} from "./title";
import {XLargeDirective} from "./x-large";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {SecurityService} from "../../+security/security.service";
import {SignInDialogComponent} from "../../components/SignInDialog/SignInDialog.component";
import {MdDialog} from "@angular/material";
import {RegistrationDialogComponent} from "../../components/RegistrationDialog/RegistrationDialog.component";

enum PasswordResetStatus {
    FILL_IN,
    PENDING,
    SUCCESSFUL,
    FAILED
}


@Component({
    selector: 'password-reset-page',
    styleUrls: ['password-reset-page.scss'],
    template: `

        <div [ngSwitch]="confirmationStatus" class="password-reset-page" fxLayout="column" fxLayoutAlign="center center">
            <md-card *ngSwitchCase="PasswordResetStatus.FILL_IN" fxFlex="nogrow">
                <md-card-header>
                    <md-card-title>Reset your password</md-card-title>
                    <md-card-subtitle>Please fill in the new password</md-card-subtitle>
                </md-card-header>
                <md-card-content>
                    <new-password-form (onSubmit)="resetPassword($event)"></new-password-form>
                </md-card-content>
            </md-card>
            <div *ngSwitchCase="PasswordResetStatus.PENDING"
                 fxLayout='column'
                 fxLayoutAlign="center center">
                <md-spinner></md-spinner>
                <h3>Changing password...</h3>
            </div>
            <md-card *ngSwitchCase="PasswordResetStatus.SUCCESSFUL" fxFlex="nogrow">
                <md-card-header>
                    <md-card-title>Success</md-card-title>
                    <md-card-subtitle>Your password has been successfully changed. 
                        You may now sign in with the new password.</md-card-subtitle>
                </md-card-header>
                <md-card-actions>
                    <button class="mat-primary" (click)="openSignInDialog()" md-button>Sign in</button>
                </md-card-actions>
            </md-card>
            <md-card *ngSwitchCase="PasswordResetStatus.FAILED" fxFlex="nogrow">
                <md-card-header>
                    <md-card-title>Failure</md-card-title>
                    <md-card-subtitle>The password could not be changed.</md-card-subtitle>
                </md-card-header>
                <md-card-actions>
                    <button (click)="openSignInDialog()" md-button>I already have account</button>
                    <button class="mat-primary" (click)="openRegistrationDialog()" md-button>I am a new user</button>
                </md-card-actions>
            </md-card>
        </div>

    `
})
export class PasswordResetPage implements OnInit, OnDestroy {

    PasswordResetStatus = PasswordResetStatus;

    confirmationStatus: PasswordResetStatus = PasswordResetStatus.FILL_IN;

    private resetPassword$$: Subscription;

    private queryParams$$: Subscription;

    private resetCode: string;

    constructor(private route: ActivatedRoute,
                private securityService: SecurityService,
                private dialog: MdDialog,
                private router: Router) {

    }

    public ngOnInit() {
        this.queryParams$$ = this.route.queryParams
            .subscribe(params => {
                this.resetCode = params['resetcode'];
            });
    }

    resetPassword({ password }) {
        this.handleResetting();
        this.resetPassword$$ = this.securityService.setNewPasswordAfterReset(password, this.resetCode)
            .subscribe(this.handleResetSuccess, this.handleResetFailure)
    }

    openSignInDialog = () => {
        this.dialog.open(SignInDialogComponent).afterClosed()
            .subscribe(result => {
                this.router.navigate(['home']);
            });
    };

    openRegistrationDialog = () => {
        this.dialog.open(RegistrationDialogComponent).afterClosed()
            .subscribe();
    };

    handleResetting = () => {
        this.confirmationStatus = PasswordResetStatus.PENDING;
    };

    handleResetSuccess = () => {
        this.confirmationStatus = PasswordResetStatus.SUCCESSFUL;
    };

    handleResetFailure = () => {
        this.confirmationStatus = PasswordResetStatus.FAILED;
    };

    ngOnDestroy() {
        this.queryParams$$.unsubscribe();
        if (this.resetPassword$$) {
            this.resetPassword$$.unsubscribe()
        }
    }

}
