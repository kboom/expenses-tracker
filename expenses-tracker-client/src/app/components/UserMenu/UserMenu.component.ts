import {Component, OnDestroy, OnInit} from "@angular/core";
import {UserService} from "../../+user/user.service";
import {Subscription} from "rxjs/Subscription";
import {MdDialog, MdSnackBar} from "@angular/material";
import {UserHolder} from "../../+user/user.holder";
import {Observable} from "rxjs/Observable";
import {Router} from "@angular/router";
import {UserRepository} from "../../repository/user.repository";
import {ProfileDialog} from "../ProfileDialog/ProfileDialog.component";
import {PasswordChangeDialog} from "../PasswordChangeDialog/PasswordChangeDialog.component";
import {EventBusService} from "../../services/eventBus.service";
import {USER_CHANGED_EVENT} from "../../app.events";

@Component({
    selector: 'userMenu',
    template: `

        <div *ngIf="this.isLoggedIn$ | async; else signInBtn">
            <button md-button [mdMenuTriggerFor]="menu">Hello, {{ this.userHolder.getUser().entity.username }}!
            </button>
            <md-menu #menu="mdMenu">
                <button md-menu-item (click)="editProfile()">Edit profile</button>
                <button md-menu-item (click)="changePassword()">Change password</button>
                <button md-menu-item (click)="signOut()">Sign out</button>
            </md-menu>
        </div>

        <ng-template #signInBtn>
            <a md-button href="login">Sign in</a>
            <a md-button class="mat-primary" href="api/auth/register">Register</a>
        </ng-template>


    `
})
export class UserMenuComponent implements OnInit, OnDestroy {

    authenticationEvents$: Subscription;

    isLoggedIn$: Observable<Boolean>;

    constructor(private readonly userService: UserService,
                private readonly userRepository: UserRepository,
                private readonly userHolder: UserHolder,
                private readonly eventBusService: EventBusService,
                private readonly dialog: MdDialog,
                private readonly snackBar: MdSnackBar,
                private readonly router: Router) {
        this.isLoggedIn$ = this.userHolder.getUser$()
            .map((user) => {
                return user.entity.isKnown();
            })
    }

    handleAuthenticationEvent(event) {
        console.log("Authentication event");
    }

    changePassword() {
        this.dialog.open(PasswordChangeDialog, {
            data: this.userHolder.getUser()
        }).afterClosed()
            .subscribe(result => {

            });
    }

    editProfile() {
        this.dialog.open(ProfileDialog, {
            data: this.userHolder.getUser()
        }).afterClosed()
            .subscribe(result => {
                this.eventBusService.publish(USER_CHANGED_EVENT);
            });
    }

    signOut() {
        this.userService.signOut().subscribe(() => {
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
        this.userService.getAuthenticationEvents$().subscribe(this.handleAuthenticationEvent)
    }

    public ngOnDestroy() {
        // this.authenticationEvents$.unsubscribe()
    }

}
