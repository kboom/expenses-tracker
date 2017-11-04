import {Routes} from "@angular/router";
import {LoginPage} from "./pages/login/login.page";
import {RegisterPage} from "./pages/register/register.page";
import {WELCOME_PAGE_ROUTE} from "./pages/welcome/welcome.page";

export const ROUTES: Routes = [
    {path: 'login', component: LoginPage},
    {path: 'register', component: RegisterPage},
    WELCOME_PAGE_ROUTE,
    {path: '', redirectTo: 'login', pathMatch: 'full'}
];
