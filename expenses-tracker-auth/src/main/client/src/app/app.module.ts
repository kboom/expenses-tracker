import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {LoginPage} from './pages/login'
import {RegisterPage} from "./pages/register/register.page";
import {WelcomePage, CanActivateWelcomePage} from "./pages/welcome/welcome.page";
import {PreloadAllModules, RouterModule} from "@angular/router";
import {ReactiveFormsModule, FormsModule} from "@angular/forms";
import {FlexLayoutModule} from "@angular/flex-layout";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpModule} from "@angular/http";
import {HttpClientModule} from "@angular/common/http";
import {ROUTES, PageChangeEventsRouter} from "./app.routes";
import {
    MatButtonModule,
    MatInputModule,
} from "@angular/material";
import {ControlMessagesComponent} from "./components/ControlMessages/ControlMessages.component";
import {ValidationMessageProvider} from "./services/validation/validation.messages";
import {LocalLoginFormComponent} from "./components/LocalLoginForm/LocalLoginForm.component";
import {SocialLoginPanelComponent} from "./components/SocialLoginPanel/SocialLoginPanel.component";
import {LoginService} from "./services/login.service";
import {UserRepository} from "./modules/+user/user.repository";
import {UserFactory} from "./models/index";
import {UserHolder} from "./modules/+user/user.holder";
import {ServerModule} from "./modules/+server/server.module";
import EventBusService from "./app.events";

const APP_PROVIDERS = [
    EventBusService,
    ValidationMessageProvider,
    LoginService,
    UserRepository,
    UserFactory,
    UserHolder,
    CanActivateWelcomePage,
    PageChangeEventsRouter
];

import "../styles/styles.scss";

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpModule,
        HttpClientModule,
        FlexLayoutModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule.forRoot(ROUTES, {useHash: true, preloadingStrategy: PreloadAllModules}),
        MatInputModule,
        MatButtonModule,
        ServerModule
    ],
    declarations: [
        AppComponent,
        LoginPage,
        WelcomePage,
        RegisterPage,
        ControlMessagesComponent,
        LocalLoginFormComponent,
        SocialLoginPanelComponent
    ],
    providers: [
        ...APP_PROVIDERS
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}