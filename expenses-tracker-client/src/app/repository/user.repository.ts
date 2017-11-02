import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/observable/zip";
import "rxjs/add/operator/map";
import {Injectable} from "@angular/core";
import {UserModel} from "../models/User.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {EntityCollectionModel} from "../models/hateoas/EntityCollection.model";
import {RoleModel} from "../models/Role.model";
import {Entity} from "../models/hateoas/Entity.model";
import {extend, omit, transform} from "lodash-es";
import {UserFactory} from "../models/factory";

const getAllUsersURL = "api/users?projection=withDetails";
const postUserURL = "api/users";
const getUserByUsernameURL = "api/users/search/findByUsername?projections=withDetails";

@Injectable()
export class UserRepository {

    constructor(private userFactory: UserFactory,
                private http: HttpClient) {

    }

    public getUserByUsername(username: string): Observable<EntityCollectionModel<UserModel>> {
        return this.http.get(getUserByUsernameURL, { params: new HttpParams().set("username", username) })
            .map((body: any) => this.userFactory.constructEntity(body))
            .catch(() => Observable.throw("Could not get user"));
    }

    public getAllUsers(): Observable<EntityCollectionModel<UserModel>> {
        return this.http.get(getAllUsersURL)
            .map((body: any) => new EntityCollectionModel('users', body, this.userFactory))
            .catch(() => Observable.throw("Could not get users"));
    }

    createUser(userEntity: Entity<UserModel>): Observable<any> {
        return this.http.post(postUserURL, this.withMappedLinkedEntities(userEntity), {});
    }

    public updateUser(userEntity: Entity<UserModel>): Observable<Entity<UserModel>> {
        return this.http.patch(userEntity.links['self']['href'], this.withMappedLinkedEntities(userEntity), {
            headers: new HttpHeaders().set("Content-Type", "application/merge-patch+json")
        });
    }

    public deleteUser(userEntity: Entity<UserModel>) {
        return this.http.delete(userEntity.links['self']['href']);
    }

    private constructRoleURIsFor(userEntity: Entity<UserModel>) {
        const basePath = "api/authorities/:roleName";
        return userEntity.entity.authorities.map((role) => basePath.replace(":roleName", RoleModel[role])).join("\n");
    }

    private withMappedLinkedEntities(userEntity: Entity<UserModel>) {
        return transform(userEntity.entity, (result, value, key) => {
            if(key == 'authorities') {
                result[key] = value.map((val) => `/authorities/${RoleModel[val]}`)
            } else {
                result[key] = value;
            }
        });
    }

}
