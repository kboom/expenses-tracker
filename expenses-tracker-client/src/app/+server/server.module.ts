import {NgModule} from "@angular/core";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {TokenAddingInterceptor} from "./security.service";
import {DefaultContentTypeInterceptor} from "./server.interceptor";

@NgModule({
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: DefaultContentTypeInterceptor,
            multi: true
        }
    ]
})
export class ServerModule {
}