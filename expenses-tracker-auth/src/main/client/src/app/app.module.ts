import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {LoginPage} from './pages/login'
import {PreloadAllModules, RouterModule} from "@angular/router";
import {ROUTES} from "./app.routes";

@NgModule({
    imports: [
        BrowserModule,
        RouterModule.forRoot(ROUTES, {useHash: true, preloadingStrategy: PreloadAllModules}),
    ],
    declarations: [
        AppComponent,
        LoginPage
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}