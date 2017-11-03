import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {LoginPage} from './pages/login'
import {RegisterPage} from "./pages/register/register.page";
import {PreloadAllModules, RouterModule} from "@angular/router";
import {ROUTES} from "./app.routes";

@NgModule({
    imports: [
        BrowserModule,
        RouterModule.forRoot(ROUTES, {useHash: true, preloadingStrategy: PreloadAllModules}),
    ],
    declarations: [
        AppComponent,
        LoginPage,
        RegisterPage
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}