import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {UserModel} from "../../models/User.model";
import {extend, includes, map, pick, transform, omit} from "lodash-es";
import {Entity} from "../../models/hateoas/Entity.model";
import {RoleModel, RoleModelAware} from "../../models/Role.model";
import {EnumEx, WithEnumEx} from "../../utils/enum.utils";
import {Observable} from "rxjs/Observable";
import {
    EMAIL_REGEX, FIRST_NAME_REGEX, LAST_NAME_REGEX, PASSWORD_REGEX,
    USERNAME_REGEX
} from "../../validators/validation.rules";
import {atLeastOneTrue, validatorFor} from "../../validators/common.validators";

export interface UserEntityManager {

    onSubmit(userEntity: Entity<UserModel>): Observable<any>

}

@Component({
    selector: 'userDetailsForm',
    template: `

        <form id="userDetailsForm" [formGroup]="userDetailsForm" (ngSubmit)="this.onSubmit($event)" fxLayout='column'>

            <md-input-container>
                <input mdInput formControlName="username" type="text" placeholder="Username">
            </md-input-container>
            <control-messages [control]="userDetailsForm.controls.username"></control-messages>
            
            <md-input-container>
                <input mdInput formControlName="email" type="email" placeholder="E-mail">
            </md-input-container>
            <control-messages [control]="userDetailsForm.controls.email"></control-messages>

            <md-input-container>
                <input mdInput formControlName="firstName" type="text" placeholder="First name">
            </md-input-container>
            <control-messages [control]="userDetailsForm.controls.firstName"></control-messages>
            
            <md-input-container>
                <input mdInput formControlName="lastName" type="text" placeholder="Last name">
            </md-input-container>
            <control-messages [control]="userDetailsForm.controls.lastName"></control-messages>
            
            <!-- This does not go to model -->
            <md-checkbox (change)="togglePassword()">Set password</md-checkbox>

            <md-input-container>
                <input mdInput formControlName="password" type="password" placeholder="Password">
            </md-input-container>
            <control-messages [control]="userDetailsForm.controls.password"></control-messages>
            
            <md-slide-toggle formControlName="enabled">Enabled</md-slide-toggle>

            <div style="height: 35px;"></div>

            <div formArrayName="authorities" fxLayout='row wrap' fxLayoutAlign='space-between center'
                 fxLayoutGap="20px" style="padding-bottom: 15px">
                <md-checkbox [formControlName]="RoleModel[roleName]"
                             *ngFor="let roleName of EnumEx.getNames(RoleModel)">
                    {{ roleName }}
                </md-checkbox>
            </div>
            <control-messages [control]="userDetailsForm.controls.authorities"></control-messages>
            
        </form>

    `
})
@RoleModelAware
@WithEnumEx
export class UserDetailsFormComponent implements OnInit {

    public userDetailsForm: FormGroup;

    @Input()
    private userEntity: Entity<UserModel>;

    @Input()
    private entityManager: UserEntityManager;

    constructor(private fb: FormBuilder) {
        this.userDetailsForm = this.fb.group({
            username: ["", Validators.required, validatorFor(USERNAME_REGEX)],
            email: ["", Validators.required, validatorFor(EMAIL_REGEX)],
            firstName: ["", Validators.required, validatorFor(FIRST_NAME_REGEX)],
            lastName: ["", Validators.required, validatorFor(LAST_NAME_REGEX)],
            setPassword: [false], // this does not get into model
            password: [{value: null, disabled: true}, Validators.required, validatorFor(PASSWORD_REGEX)],
            enabled: [false, Validators.required],
            authorities: this.fb.array([
                [false],
                [false],
                [false]
            ], atLeastOneTrue('role:atLeastOne'))
        });
    }

    togglePassword() {
        if(this.userDetailsForm.controls.password.disabled) {
            this.userDetailsForm.controls.password.enable();
        } else {
            this.userDetailsForm.controls.password.disable();
        }
    }

    ngOnInit(): void {
        this.setValues(this.userEntity);
    }

    setValues = (userEntity: Entity<UserModel>) => {
        this.userDetailsForm.setValue(
            extend({}, pick(userEntity.entity, ['username', 'password', 'email', 'firstName', 'lastName', 'enabled']), {
                authorities: EnumEx.getValues(RoleModel).map((role) => {
                    return includes(userEntity.entity.authorities, role);
                }),
                setPassword: false
            })
        );
    };

    onSubmit(event): void {
        const formData = this.userDetailsForm.value;
        this.entityManager.onSubmit(this.userEntity.withUpdatedEntity(transform(omit(formData, ['setPassword']), (result, value, key) => {
            if (key == 'authorities') {
                result[key] = EnumEx.getValues(RoleModel).filter((role) => value[role])
            } else {
                result[key] = value;
            }
        }, {}))).subscribe()
    }

}
