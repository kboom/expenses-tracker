import {Component, Injectable, OnInit, ViewChild} from "@angular/core";
import {Title} from "./title";
import {XLargeDirective} from "./x-large";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import "rxjs/add/observable/combineLatest";
import {ActivatedRoute, ActivatedRouteSnapshot, CanActivate, Resolve, RouterStateSnapshot} from "@angular/router";
import {UserHolder} from "../../+user/user.holder";
import {RoleModel} from "../../models/Role.model";
import {TimezonesRepository} from "../../repository/timezones.repository";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {Entity} from "../../models/hateoas/Entity.model";
import {TimezoneModel} from "../../models/Timezone.model";
import {EditTimezoneDialogComponent} from "../../components/EditTimezoneDialog/EditTimezoneDialog.component";
import {MdDialog, MdSnackBar} from "@angular/material";
import {CreateTimezoneDialogComponent} from "../../components/CreateTimezoneDialog/CreateTimezoneDialog.component";
import {FormControl} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";
import {ConfirmationDialogComponent} from "../../components/ConfirmationDialog";
import {EventBusService} from "../../services/eventBus.service";
import {TIMEZONE_CHANGED_EVENT, TIMEZONE_CREATED_EVENT, TIMEZONE_REMOVED_EVENT} from "../../app.events";

@Injectable()
export class TimezonesPageService {

    public readonly timezoneFilter$: Subject<string> = new BehaviorSubject(null);

    connectTimezonesFilter(valueChanges: Observable<string>) {
        valueChanges.subscribe((value) => {
            this.timezoneFilter$.next(value);
        });
    }

    disconnectTimezonesFilter() {

    }

}

@Component({
    selector: 'timezonesPage',
    providers: [],
    styleUrls: ['./timezones.page.scss'],
    template: `

        <div class="timezones__container" fxLayout='row wrap' fxLayoutAlign='center start' fxLayoutGap="20px">

            <timezone class="timezones__timezone" fxFlex="nogrow" [timezone]="timezone.entity"
                      class="timezones__timezone"
                      *ngFor="let timezone of timezones$ | async"
                      (onEdit)="editTimezone(timezone)"
                      (onDelete)="deleteTimezone(timezone)"
            ></timezone>

        </div>

    `
})
export class TimezonesPage implements OnInit {

    private timezones$ = new BehaviorSubject<Entity<TimezoneModel>[]>([]);

    dataChange = new BehaviorSubject<Entity<TimezoneModel>[]>([]);
    filterChange = new BehaviorSubject('');

    constructor(private route: ActivatedRoute,
                private dialog: MdDialog,
                private snackBar: MdSnackBar,
                private eventBusService: EventBusService,
                private timezonesPageService: TimezonesPageService,
                private timezonesRepository: TimezonesRepository) {
    }

    // this does not work because of BehaviourSubject emits

    // this.timezones$.next(timezones.entities);
    ngOnInit(): void {
        this.route.data.map((data) => data.timezones).subscribe((timezones) => {
            this.dataChange.next(timezones.entities);
        });

        this.eventBusService.observeEvents(
            TIMEZONE_CREATED_EVENT,
            TIMEZONE_CHANGED_EVENT,
            TIMEZONE_REMOVED_EVENT
        ).subscribe(() => {
            this.timezonesRepository.getAllTimezones().subscribe((timezones) => {
                this.dataChange.next(timezones.entities);
            });
        });

        this.timezonesPageService.timezoneFilter$.asObservable().subscribe((filterPhrase) => {
            this.filterChange.next(filterPhrase);
        });

        const displayDataChanges = [
            this.dataChange,
            this.filterChange
        ];

        Observable.combineLatest(displayDataChanges, (data, filter) => {
            return !!filter ? data.filter((entity: Entity<TimezoneModel>) => {
                const searchStr = (entity.entity.name + entity.entity.locationName).toLowerCase();
                return searchStr.indexOf(filter.toLowerCase()) != -1;
            }) : data;
        }).subscribe((timezones) => {
            this.timezones$.next(timezones);
        })
    }

    deleteTimezone(timezoneEntity) {
        const dialog = this.dialog.open(ConfirmationDialogComponent, {
            data: {
                title: `Delete timezone ${timezoneEntity.entity.name}`,
                message: `Are you sure you want to delete timezone ${timezoneEntity.entity.name}? This action cannot be undone.`
            }
        });
        dialog.afterClosed().subscribe(shouldDelete => {
            if (shouldDelete) {
                this.timezonesRepository.deleteTimezone(timezoneEntity).subscribe(() => {
                    const snackBarRef = this.snackBar.open("Timezone deleted", "Close", {
                        duration: 5000,
                    });
                    snackBarRef.onAction().subscribe(() => {
                        snackBarRef.dismiss()
                    });
                    this.eventBusService.publish(TIMEZONE_REMOVED_EVENT);
                }, () => {
                    const snackBarRef = this.snackBar.open("Could not delete timezone", "Close", {
                        duration: 5000,
                    });
                    snackBarRef.onAction().subscribe(() => {
                        snackBarRef.dismiss()
                    });
                });
            }
        });
    }

    editTimezone(timezoneEntity) {
        this.dialog.open(EditTimezoneDialogComponent, {
            data: timezoneEntity
        }).afterClosed()
            .subscribe(result => {
                this.eventBusService.publish(TIMEZONE_CHANGED_EVENT);
            });
    }

}

@Component({
    selector: 'timezones-toolbar',
    styleUrls: ["timezones.page.scss"],
    providers: [],
    template: `

        <md-toolbar-row class="timezones-navbar">
            <div style="width: 100%" fxLayout="row" fxLayoutAlign="space-between center" fxLayoutGap="20px">
                <md-input-container class="timezones-navbar__element" fxFlex="grow">
                    <input mdInput [ngModel]="x" #filterInput="ngModel" placeholder="Filter">
                </md-input-container>
                <button md-raised-button class="timezones-navbar__element mat-primary" (click)="createTimezone()">
                    Create
                </button>
            </div>
        </md-toolbar-row>

    `
})
// [ngModel]="model.first_name" (ngModelChange)="filter($event)"
export class TimezonesToolbarComponent implements OnInit {

    @ViewChild("filterInput")
    filter: FormControl;

    constructor(private dialog: MdDialog,
                private eventBusService: EventBusService,
                private timezonesPageService: TimezonesPageService) {

    }

    createTimezone(timezoneEntity) {
        this.dialog.open(CreateTimezoneDialogComponent).afterClosed()
            .subscribe(result => {
                this.eventBusService.publish(TIMEZONE_CREATED_EVENT);
            });
    }

    ngOnInit(): void {
        this.timezonesPageService.connectTimezonesFilter(this.filter.valueChanges);
    }

    ngOnDestroy(): void {
        this.timezonesPageService.disconnectTimezonesFilter();
    }

}

@Injectable()
export class TimezonesResolver implements Resolve<any> {

    constructor(private timezoneRepository: TimezonesRepository) {
    }

    public resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return this.timezoneRepository.getAllTimezones();
    }

}

@Injectable()
export class CanActivateTimezones implements CanActivate {

    constructor(private authContextHolder: UserHolder) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.authContextHolder.getUser().entity.hasAnyRole(RoleModel.ROLE_USER, RoleModel.ROLE_ADMIN)
    }

}

export const TIMEZONES_PAGE_ROUTE = {
    path: 'timezones',
    component: TimezonesPage,
    resolve: {
        timezones: TimezonesResolver
    },
    data: {
        toolbar: TimezonesToolbarComponent
    },
    canActivate: [CanActivateTimezones]
};
