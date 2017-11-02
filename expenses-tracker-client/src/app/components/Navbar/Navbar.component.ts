import {
    Component,
    ComponentFactory,
    ComponentFactoryResolver,
    ComponentRef,
    OnDestroy,
    OnInit,
    Type,
    ViewChild,
    ViewContainerRef
} from "@angular/core";
import {ISubscription} from "rxjs/Subscription";
import {ActivatedRouteSnapshot, NavigationEnd, Router} from "@angular/router";
import {RoleModelAware} from "../../models/Role.model";
import {UserService} from "../../+user/user.service";
import {UserHolder} from "../../+user/user.holder";

@Component({
    selector: 'navbar',
    // styleUrls: ['./navbar.component.scss'],
    template: `

        <nav>
            <md-toolbar>

                <a md-button [routerLink]=" ['./home'] "
                   routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    Home
                </a>
                <a md-button [routerLink]=" ['./timezones'] "
                   *ngIf="this.userHolder.getUser().entity.hasAnyRole(RoleModel.ROLE_USER, RoleModel.ROLE_ADMIN)"
                   routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    Timezones
                </a>
                <a md-button [routerLink]=" ['./users'] "
                   *ngIf="this.userHolder.getUser().entity.hasAnyRole(RoleModel.ROLE_ADMIN, RoleModel.ROLE_MANAGER)"
                   routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    Users
                </a>
                <span class="fill-remaining-space"></span>
                <userMenu>

                </userMenu>

                <md-toolbar-row [ngStyle]="{ display: toolbarComponents.length > 0 ? 'inherit' : 'none' }">
                    <ng-template #toolbarTarget></ng-template>
                </md-toolbar-row>

            </md-toolbar>
        </nav>

    `
})
@RoleModelAware
export class NavbarComponent implements OnInit, OnDestroy {

    @ViewChild("toolbarTarget", {read: ViewContainerRef})
    toolbarTarget: ViewContainerRef;

    toolbarComponents: ComponentRef<Component>[] = [];
    routerEventSubscription: ISubscription;


    constructor(private readonly userService: UserService,
                private readonly userHolder: UserHolder,
                private router: Router,
                private componentFactoryResolver: ComponentFactoryResolver) {
    }

    ngOnInit(): void {
        this.routerEventSubscription = this.router.events.subscribe(
            (event) => {
                if (event instanceof NavigationEnd) {
                    this.updateToolbarContent(this.router.routerState.snapshot.root);
                }
            }
        );
    }

    ngOnDestroy(): void {
        this.routerEventSubscription.unsubscribe();
    }

    private updateToolbarContent(snapshot: ActivatedRouteSnapshot): void {
        this.clearToolbar();
        let toolbar: any = (snapshot.data as { toolbar: Type<Component> }).toolbar;
        if (toolbar instanceof Type) {
            let factory: ComponentFactory<Component> = this.componentFactoryResolver.resolveComponentFactory(toolbar);
            let componentRef: ComponentRef<Component> = this.toolbarTarget.createComponent(factory, 0);
            this.toolbarComponents.push(componentRef);
        }
        for (let childSnapshot of snapshot.children) {
            this.updateToolbarContent(childSnapshot);
        }
    }

    private clearToolbar() {
        this.toolbarTarget.clear();
        for (let toolbarComponent of this.toolbarComponents) {
            toolbarComponent.destroy();
        }
        this.toolbarComponents = []
    }

}