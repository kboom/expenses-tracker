import {Routes} from "@angular/router";
import {LoginPage} from "./pages/login/login.page";
import {RegisterPage} from "./pages/register/register.page";
import {WelcomePage} from "./pages/welcome/welcome.page";

export const ROUTES: Routes = [
    {path: 'login', component: LoginPage},
    {path: 'register', component: RegisterPage},
    {path: 'welcome', component: WelcomePage},
    {path: '', redirectTo: 'login', pathMatch: 'full'}
];
