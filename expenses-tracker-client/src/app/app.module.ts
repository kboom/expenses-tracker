import {BrowserModule} from "@angular/platform-browser";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {HttpClientModule} from "@angular/common/http";
import {CdkTableModule} from "@angular/cdk";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ApplicationRef, NgModule} from "@angular/core";
import {createInputTransfer, createNewHosts, removeNgStyles} from "@angularclass/hmr";
import {PreloadAllModules, RouterModule} from "@angular/router";
import {LocalStorageModule} from "angular-2-local-storage";
import {FlexLayoutModule} from "@angular/flex-layout";
import { MomentModule } from 'angular2-moment';
import {
    MdButtonModule,
    MdCardModule,
    MdCheckboxModule,
    MdDialogModule,
    MdGridListModule,
    MdIconModule,
    MdInputModule,
    MdMenuModule,
    MdProgressSpinnerModule,
    MdSelectModule,
    MdSlideToggleModule,
    MdSnackBarModule,
    MdSortModule,
    MdTableModule,
    MdToolbarModule
} from "@angular/material";
/*
 * Platform and Environment providers/directives/pipes
 */
// modules
import {ServerModule} from "./+server";
import {SecurityModule} from "./+user";

import {ENV_PROVIDERS} from "./environment";
import {ROUTES} from "./app.routes";
// styles
import "../styles/styles.scss";
import "../styles/headings.css";
// App is our top level component
import {AppComponent} from "./app.component";
import {APP_RESOLVER_PROVIDERS} from "./app.resolver";
import {AppState, InternalStateType} from "./app.service";
import {HomeComponent} from "./pages/home";
import {AboutComponent} from "./pages/about";
import {UsersComponent} from "./pages/users/users.component";
import {NoContentComponent} from "./pages/no-content";
import {XLargeDirective} from "./pages/home/x-large";
import {UserMenuComponent} from "./components/UserMenu";
import {SignInDialogComponent} from "./components/SignInDialog";
import {UserDetailsDialogComponent} from "./components/UserDetailsDialog";
import {UserService} from "./+user/user.service";
import {UserHolder} from "./+user/user.holder";
import {UsersTableComponent} from "./components/UsersTable/UsersTable.component";
import {UserRepository} from "./repository/user.repository";
import {TimezoneFactory, UserFactory} from "./models/factory";
import {ControlMessagesComponent} from "./components/ControlMessages/ControlMessages.component";
import {ConfirmationDialogComponent} from "./components/ConfirmationDialog";
import {UserDetailsFormComponent} from "./components/UserDetailsForm/UserDetailsForm.component";
import {CreateUserDialogComponent} from "./components/CreateUserDialog/CreateUserDialog.component";
import {ValidationMessageProvider} from "./validators/validation.messages";
import {TimezonesRepository} from "./repository/timezones.repository";
import {
    CanActivateTimezones,
    TimezonesPage,
    TimezonesPageService,
    TimezonesResolver,
    TimezonesToolbarComponent
} from "./pages/timezones/timezones.page";
import {TimezoneComponent} from "./components/TimezoneComponent/Timezone.component";
import {TimezoneForm} from "./components/TimezoneForm/Timezone.form";
import {EditTimezoneDialogComponent} from "./components/EditTimezoneDialog/EditTimezoneDialog.component";
import {CreateTimezoneDialogComponent} from "./components/CreateTimezoneDialog/CreateTimezoneDialog.component";
import {NavbarComponent} from "./components/Navbar/Navbar.component";
import {ProfileDialog} from "./components/ProfileDialog/ProfileDialog.component";
import {PasswordChangeDialog} from "./components/PasswordChangeDialog/PasswordChangeDialog.component";
import {AnalogClockComponent} from "./components/AnalogClock/AnalogClock.component";
import {EventBusService} from "./services/eventBus.service";

// Application wide providers
const APP_PROVIDERS = [
    UserService,
    UserHolder,
    ValidationMessageProvider,
    UserRepository,
    UserFactory,
    TimezonesRepository,
    TimezoneFactory,
    CanActivateTimezones,
    TimezonesResolver,
    TimezonesToolbarComponent,
    TimezonesPageService,
    EventBusService,
    AppState
];

type StoreType = {
    state: InternalStateType,
    restoreInputValues: () => void,
    disposeOldHosts: () => void
};

/**
 * `AppModule` is the main entry point into Angular2's bootstraping process
 */
@NgModule({
    bootstrap: [AppComponent],
    declarations: [
        AppComponent,
        AnalogClockComponent,
        AboutComponent,
        HomeComponent,
        UsersComponent,
        TimezonesPage,
        TimezoneComponent,
        NoContentComponent,
        XLargeDirective,
        UserMenuComponent,
        UserDetailsDialogComponent,
        CreateTimezoneDialogComponent,
        UsersTableComponent,
        ControlMessagesComponent,
        ConfirmationDialogComponent,
        EditTimezoneDialogComponent,
        UserDetailsFormComponent,
        TimezoneForm,
        CreateUserDialogComponent,
        NavbarComponent,
        TimezonesToolbarComponent,
        ProfileDialog,
        PasswordChangeDialog
    ],
    /**
     * Import Angular's modules.
     */
    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        HttpModule,
        HttpClientModule,
        ServerModule,
        SecurityModule,
        BrowserAnimationsModule,
        CdkTableModule,
        RouterModule.forRoot(ROUTES, {useHash: true, preloadingStrategy: PreloadAllModules}),
        MdInputModule,
        MdButtonModule,
        MdCheckboxModule,
        MdSelectModule,
        MdMenuModule,
        MdToolbarModule,
        MdDialogModule,
        MdTableModule,
        MdSortModule,
        MdIconModule,
        MdGridListModule,
        MdSnackBarModule,
        MdSlideToggleModule,
        MdProgressSpinnerModule,
        MdCardModule,
        LocalStorageModule.withConfig({
            prefix: 'timezones',
            storageType: 'localStorage'
        }),
        FlexLayoutModule,
        MomentModule
    ],

    entryComponents: [
        UserDetailsDialogComponent,
        ConfirmationDialogComponent,
        CreateUserDialogComponent,
        EditTimezoneDialogComponent,
        CreateTimezoneDialogComponent,
        TimezonesToolbarComponent,
        ProfileDialog,
        PasswordChangeDialog
    ],

    /**
     * Expose our Services and Providers into Angular's dependency injection.
     */
    providers: [
        ENV_PROVIDERS,
        APP_PROVIDERS
    ]
})
export class AppModule {

    constructor(public appRef: ApplicationRef,
                public appState: AppState) {
    }

    public hmrOnInit(store: StoreType) {
        if (!store || !store.state) {
            return;
        }
        console.log('HMR store', JSON.stringify(store, null, 2));
        /**
         * Set state
         */
        this.appState._state = store.state;
        /**
         * Set input values
         */
        if ('restoreInputValues' in store) {
            let restoreInputValues = store.restoreInputValues;
            setTimeout(restoreInputValues);
        }

        this.appRef.tick();
        delete store.state;
        delete store.restoreInputValues;
    }

    public hmrOnDestroy(store: StoreType) {
        const cmpLocation = this.appRef.components.map((cmp) => cmp.location.nativeElement);
        /**
         * Save state
         */
        const state = this.appState._state;
        store.state = state;
        /**
         * Recreate root elements
         */
        store.disposeOldHosts = createNewHosts(cmpLocation);
        /**
         * Save input values
         */
        store.restoreInputValues = createInputTransfer();
        /**
         * Remove styles
         */
        removeNgStyles();
    }

    public hmrAfterDestroy(store: StoreType) {
        /**
         * Display new elements
         */
        store.disposeOldHosts();
        delete store.disposeOldHosts;
    }

}
