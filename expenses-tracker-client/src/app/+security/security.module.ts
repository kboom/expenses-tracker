import {NgModule} from "@angular/core";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {TokenAddingInterceptor} from './security.service';

@NgModule({
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenAddingInterceptor,
            multi: true
        }
    ]
})
export class SecurityModule { }