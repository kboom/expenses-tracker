import {Component, Inject, ViewChild} from "@angular/core";
import {MD_DIALOG_DATA, MdDialogRef} from "@angular/material";
import {extend, includes, map, pick, transform} from "lodash-es";
import {Entity} from "../../models/hateoas/Entity.model";
import {UserEntityManager} from "../components/UserDetailsForm/UserDetailsForm.component";
import {Observable} from "rxjs/Observable";
import {UserDetailsFormComponent} from "../UserDetailsForm";
import {TimezoneModel} from "../../models/Timezone.model";
import {TimezonesRepository} from "../../repository/timezones.repository";
import {TimezoneForm, FormEntityManager} from "../TimezoneForm";
import {TimezoneFactory} from "../../models/factory/index";

@Component({
    selector: 'editTimezoneDialog',
    template: `

        <div>

            <h2 md-dialog-title>Edit timezone</h2>

            <md-dialog-content>

                <timezoneForm [timezoneEntity]="timezoneEntity" [entityManager]="this"></timezoneForm>

            </md-dialog-content>

            <div style="height: 35px"></div>

            <md-dialog-actions fxLayout='row' fxLayoutAlign='space-between center'>
                <button md-button md-dialog-close>Cancel</button>
                <button class="mat-primary" type="submit" md-raised-button form="timezoneForm"
                        [disabled]="!timezoneForm.timezoneForm.valid">Save
                </button>
            </md-dialog-actions>

        </div>

    `
})
// [disabled]="!userDetailsForm.valid"
export class CreateTimezoneDialogComponent implements FormEntityManager<TimezoneModel> {

    private timezoneEntity: Entity<TimezoneModel>;

    @ViewChild(TimezoneForm)
    private timezoneForm: TimezoneForm;

    constructor(private timezoneFactory: TimezoneFactory,
                private dialogRef: MdDialogRef<CreateTimezoneDialogComponent>,
                private timezoneRepository: TimezonesRepository) {
        this.timezoneEntity = timezoneFactory.constructNewEntity();
    }

    onSubmit(timezoneEntity: Entity<TimezoneModel>): Observable<any> {
        return Observable.create((observer) => {
            this.timezoneRepository.createTimezone(timezoneEntity)
                .subscribe(() => {
                    observer.complete();
                    this.dialogRef.close();
                });
        });
    }

}
