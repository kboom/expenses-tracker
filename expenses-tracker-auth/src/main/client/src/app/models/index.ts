import {Injectable} from "@angular/core";
import UserModel, { unknownUser } from "./User.model";
import {EntityFactory} from "./hateoas/EntityCollection.model";
import Entity, {ModelFactory} from "./hateoas/Entity.model";
import {RoleModel} from "../modules/+user/user.holder";

@Injectable()
export class UserFactory implements EntityFactory<UserModel>, ModelFactory<UserModel> {

    constructNewModel(): UserModel {
        return unknownUser();
    }

    constructNewEntity(): Entity<UserModel> {
        return Entity.empty(this.constructNewModel())
    }

    constructModel = (obj: any): UserModel => {
        return new UserModel(
            obj.username,
            obj.email,
            (obj.authorities || []).map((authority: any) => RoleModel[authority.name]),
            obj.firstName,
            obj.lastName,
        )
    };

    constructEntity = (obj: any | null): Entity<UserModel> => {
        return Entity.fromJSON(obj, this);
    };

}