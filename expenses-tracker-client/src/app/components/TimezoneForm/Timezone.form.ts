import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {extend, includes, map, pick, transform} from "lodash-es";
import {Entity} from "../../models/hateoas/Entity.model";
import {Observable} from "rxjs/Observable";
import {TIMEZONE_LOCATION_REGEX, TIMEZONE_NAME_REGEX} from "../../validators/validation.rules";
import {validatorFor} from "../../validators/common.validators";
import {TimezoneModel} from "../../models/Timezone.model";

export interface FormEntityManager<T> {

    onSubmit(entity: Entity<T>): Observable<any>

}

@Component({
    selector: 'timezoneForm',
    template: `

        <form id="timezoneForm" [formGroup]="timezoneForm" (ngSubmit)="this.onSubmit($event)" fxLayout='column'>

            <md-input-container>
                <input mdInput formControlName="name" type="text" placeholder="Name">
            </md-input-container>
            <control-messages [control]="timezoneForm.controls.name"></control-messages>

            <md-input-container>
                <input mdInput formControlName="locationName" type="text" placeholder="Location name">
            </md-input-container>
            <control-messages [control]="timezoneForm.controls.locationName"></control-messages>

            <md-input-container>
                <input mdInput formControlName="differenceToGMT" type="number" placeholder="Time difference to GMT">
            </md-input-container>
            <control-messages [control]="timezoneForm.controls.differenceToGMT"></control-messages>

        </form>

    `
})
export class TimezoneForm implements OnInit {

    public timezoneForm: FormGroup;

    @Input()
    private timezoneEntity: Entity<TimezoneModel>;

    @Input()
    private entityManager: FormEntityManager<TimezoneModel>;

    constructor(private fb: FormBuilder) {
        this.timezoneForm = this.fb.group({
            name: ["", Validators.required, validatorFor(TIMEZONE_NAME_REGEX)],
            locationName: ["", Validators.required, validatorFor(TIMEZONE_LOCATION_REGEX)],
            differenceToGMT: [0, [Validators.required, Validators.max(12), Validators.min(-12)]]
        });
    }

    ngOnInit(): void {
        this.setValues(this.timezoneEntity);
    }

    setValues = (timezoneEntity: Entity<TimezoneModel>) => {
        this.timezoneForm.setValue(pick(timezoneEntity.entity, ['name', 'locationName', 'differenceToGMT']));
    };

    onSubmit(event): void {
        const formData = this.timezoneForm.value;
        this.entityManager.onSubmit(this.timezoneEntity.withUpdatedEntity(formData)).subscribe()
    }

}
