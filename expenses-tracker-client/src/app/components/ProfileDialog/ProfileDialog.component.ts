import {Component, Inject, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MD_DIALOG_DATA, MdDialogRef, MdSnackBar} from "@angular/material";
import {SecurityService} from "../../+user/security.service";
import {omit, pick } from "lodash-es";
import {validatorFor} from "../../validators/common.validators";
import {
    LAST_NAME_REGEX,
    FIRST_NAME_REGEX
} from "../../validators/validation.rules";
import {Entity} from "../../models/hateoas/Entity.model";
import {UserModel} from "../../models/User.model";
import {UserService} from "../../+user/user.service";

@Component({
    selector: 'profile-dialog',
    template: `

        <div>

            <h2 md-dialog-title>Edit profile</h2>

            <md-dialog-content fxLayout='column'>

                <md-input-container>
                    <input mdInput disabled type="text" placeholder="Username" [value]="userEntity.entity.username">
                </md-input-container>

                <md-input-container>
                    <input mdInput disabled type="email" placeholder="E-mail" [value]="userEntity.entity.email">
                </md-input-container>

                <form id="profileForm" [formGroup]="profileForm" fxLayout='column'
                      (ngSubmit)="this.doRegister($event)">

                    <md-input-container>
                        <input mdInput formControlName="firstName" type="text" placeholder="First name">
                    </md-input-container>
                    <control-messages [control]="profileForm.controls.firstName"></control-messages>

                    <md-input-container>
                        <input mdInput formControlName="lastName" type="text" placeholder="Last name">
                    </md-input-container>
                    <control-messages [control]="profileForm.controls.lastName"></control-messages>

                </form>

            </md-dialog-content>

            <md-dialog-actions fxLayout='row' fxLayoutAlign='space-between center'>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" class="mat-primary" form="profileForm" md-button [disabled]="!profileForm.valid">
                    Update
                </button>
            </md-dialog-actions>

        </div>


    `
})
// https://codecraft.tv/courses/angular/forms/model-driven-validation/
export class ProfileDialog implements OnInit {

    profileForm: FormGroup;

    constructor(@Inject(MD_DIALOG_DATA) public userEntity: Entity<UserModel>,
                private fb: FormBuilder,
                private dialogRef: MdDialogRef<ProfileDialog>,
                private userService: UserService,
                private snackBar: MdSnackBar) {
        this.profileForm = this.fb.group({
            firstName: ["", Validators.required, validatorFor(FIRST_NAME_REGEX)],
            lastName: ["", Validators.required, validatorFor(LAST_NAME_REGEX)],
        });
    }

    ngOnInit(): void {
        this.setValues(this.userEntity);
    }

    setValues = (userProfileEntity: Entity<UserModel>) => {
        this.profileForm.setValue(pick(userProfileEntity.entity, ['firstName', 'lastName']));
    };

    doRegister(event): void {
        const formData = this.profileForm.value;
        this.userService.updateProfile(this.userEntity.withUpdatedEntity(formData))
            .subscribe((x) => {
                const snackBarRef = this.snackBar.open("Your profile has been updated", "Close", {
                    duration: 5000,
                });
                snackBarRef.onAction().subscribe(() => {
                    snackBarRef.dismiss()
                });
                this.dialogRef.close(x)
            })
    }

}
