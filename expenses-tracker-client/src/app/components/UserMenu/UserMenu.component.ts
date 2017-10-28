import {Component, OnDestroy, OnInit} from "@angular/core";
import {SecurityService} from "../../+security/security.service";
import {Subscription} from "rxjs/Subscription";
import {MdDialog, MdSnackBar} from "@angular/material";
import {SignInDialogComponent} from "../SignInDialog/SignInDialog.component";
import {SecurityContextHolder} from "../../+security/security.context";
import {Observable} from "rxjs/Observable";
import {RegistrationDialogComponent} from "../RegistrationDialog/RegistrationDialog.component";
import {Router} from "@angular/router";
import {UserRepository} from "../../repository/user.repository";
import {ProfileDialog} from "../ProfileDialog/ProfileDialog.component";
import {PasswordChangeDialog} from "../PasswordChangeDialog/PasswordChangeDialog.component";
import {PasswordResetDialog} from "../PasswordResetDialog/PasswordResetDialog.component";
import {EventBusService} from "../../services/eventBus.service";
import {USER_CHANGED_EVENT} from "../../app.events";

@Component({
    selector: 'userMenu',
    template: `

        <div *ngIf="this.isLoggedIn$ | async; else signInBtn">
            <button md-button [mdMenuTriggerFor]="menu">Hello, {{ this.securityContext.getAuthentication().getUsername()
                }}!
            </button>
            <md-menu #menu="mdMenu">
                <button md-menu-item (click)="editProfile()">Edit profile</button>
                <button md-menu-item (click)="changePassword()">Change password</button>
                <button md-menu-item (click)="signOut()">Sign out</button>
            </md-menu>
        </div>

        <ng-template #signInBtn>
            <button md-button (click)="this.openSignInDialog()">Sign in</button>
            <button md-button class="mat-primary" (click)="this.openRegistrationDialog()">Register</button>
        </ng-template>


    `
})
export class UserMenuComponent implements OnInit, OnDestroy {

    authenticationEvents$: Subscription;

    isLoggedIn$: Observable<Boolean>;

    constructor(private authService: SecurityService,
                private userRepository: UserRepository,
                private securityContext: SecurityContextHolder,
                private eventBusService: EventBusService,
                private dialog: MdDialog,
                private snackBar: MdSnackBar,
                private router: Router) {
        this.isLoggedIn$ = this.securityContext.getAuthentication$()
            .map((authentication) => authentication.isAuthenticated())
    }

    handleAuthenticationEvent(event) {
        console.log("Authentication event");
    }

    openRegistrationDialog() {
        this.dialog.open(RegistrationDialogComponent).afterClosed()
            .subscribe(result => {

            });
    }

    openSignInDialog() {
        this.dialog.open(SignInDialogComponent).afterClosed()
            .subscribe(result => {
                if(result === "forgot") {
                    this.dialog.open(PasswordResetDialog)
                } else {
                    this.router.navigate(['home']);
                }
            });
    }

    changePassword() {
        const username = this.securityContext.getAuthentication().getUsername();
        this.userRepository.getUserByUsername(username).subscribe((userEntity) => {
            this.dialog.open(PasswordChangeDialog, {
                data: userEntity
            }).afterClosed()
                .subscribe(result => {

                });
        });
    }

    editProfile() {
        this.authService.getProfile().subscribe((userProfile) => {
            this.dialog.open(ProfileDialog, {
                data: userProfile
            }).afterClosed()
                .subscribe(result => {
                    this.eventBusService.publish(USER_CHANGED_EVENT);
                });
        });
    }

    signOut() {
        this.authService.signOut().subscribe(() => {
            this.router.navigate(['home']);
        }, () => {
            const snackBarRef = this.snackBar.open("Could not log out!", "Close", {
                duration: 5000,
            });
            snackBarRef.onAction().subscribe(() => {
                snackBarRef.dismiss()
            });
        });
    }

    public ngOnInit() {
        this.authService.getAuthenticationEvents$().subscribe(this.handleAuthenticationEvent)
    }

    public ngOnDestroy() {
        // this.authenticationEvents$.unsubscribe()
    }

}
