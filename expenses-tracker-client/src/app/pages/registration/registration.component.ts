import {Component, OnDestroy, OnInit} from "@angular/core";
import {Title} from "./title";
import {XLargeDirective} from "./x-large";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {SecurityService} from "../../+security/security.service";
import {Observable} from "rxjs/Observable";
import {SignInDialogComponent} from "../../components/SignInDialog/SignInDialog.component";
import {MdDialog} from "@angular/material";
import {RegistrationDialogComponent} from "../../components/RegistrationDialog/RegistrationDialog.component";

enum ConfirmationStatus {
    PENDING,
    SUCCESSFUL,
    FAILED
}


@Component({
    selector: 'registrationPage',
    styleUrls: ['./registration.page.scss'],
    template: `        

        <div [ngSwitch]="confirmationStatus" class="registration-page" fxLayout="column" fxLayoutAlign="center center">
            <div *ngSwitchCase="ConfirmationStatus.PENDING"
                 fxLayout='column'
                 fxLayoutAlign="center center">
                <md-spinner></md-spinner>
                <h3>Registration request is being verified.</h3>
            </div>
            <md-card *ngSwitchCase="ConfirmationStatus.SUCCESSFUL" fxFlex="nogrow">
                <md-card-header>
                    <md-card-title>Success</md-card-title>
                    <md-card-subtitle>Your account has been successfully confirmed!</md-card-subtitle>
                </md-card-header>
                <md-card-actions>
                    <button class="mat-primary" (click)="openSignInDialog()" md-button>Sign in</button>
                </md-card-actions>
            </md-card>
            <md-card *ngSwitchCase="ConfirmationStatus.FAILED">
                <md-card-header>
                    <md-card-title>Failure</md-card-title>
                    <md-card-subtitle>Your account could not be verified!</md-card-subtitle>
                </md-card-header>
                <md-card-actions>
                    <button (click)="openSignInDialog()" md-button>I already have account</button>
                    <button class="mat-primary" (click)="openRegistrationDialog()" md-button>I am a new user</button>
                </md-card-actions>
            </md-card>
        </div>

    `
})
export class RegistrationComponent implements OnInit, OnDestroy {

    ConfirmationStatus = ConfirmationStatus;

    confirmationStatus: ConfirmationStatus = ConfirmationStatus.PENDING;

    confirmation$$: Subscription;
    queryParams$$: Subscription;

    constructor(private route: ActivatedRoute,
                private securityService: SecurityService,
                private dialog: MdDialog,
                private router: Router) {

    }

    public ngOnInit() {
        this.queryParams$$ = this.route.queryParams
            .subscribe(params => {
                const confirmationCode = params['confirmation'];
                this.confirmation$$ = this.securityService.confirmAccount(confirmationCode)
                    .subscribe(this.handleConfirmationSuccess, this.handleConfirmationFailure)
            });
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

    handleConfirmationSuccess = () => {
        this.confirmationStatus = ConfirmationStatus.SUCCESSFUL;
    };

    handleConfirmationFailure = () => {
        this.confirmationStatus = ConfirmationStatus.FAILED;
    };

    ngOnDestroy() {
        this.queryParams$$.unsubscribe();
        if (this.confirmation$$) {
            this.confirmation$$.unsubscribe()
        }
    }

}
