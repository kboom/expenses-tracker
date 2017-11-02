import {EntityFactory} from "../hateoas/EntityCollection.model";
import {UserModel} from "../User.model";
import {Entity, ModelFactory} from "../hateoas/Entity.model";
import {Injectable} from "@angular/core";
import {RoleModel} from "../Role.model";
import {TimezoneModel} from "../Timezone.model";

@Injectable()
export class UserFactory implements EntityFactory<UserModel>, ModelFactory<UserModel> {

    constructNewModel(): UserModel {
        return UserModel.unknownUser();
    }

    constructNewEntity(): Entity<UserModel> {
        return Entity.empty(this.constructNewModel())
    }

    constructModel = (obj: any): UserModel => {
        return new UserModel(
            obj.username,
            obj.email,
            (obj.authorities || []).map((authority) => RoleModel[authority.name]),
            obj.firstName,
            obj.lastName,
        )
    };

    constructEntity = (obj: any | null): Entity<UserModel> => {
        return Entity.fromJSON(obj, this);
    };

}

@Injectable()
export class TimezoneFactory implements EntityFactory<TimezoneModel>, ModelFactory<TimezoneModel> {

    constructNewModel(): TimezoneModel {
        return TimezoneModel.emptyTimezone();
    }

    constructNewEntity(): Entity<TimezoneModel> {
        return Entity.empty(this.constructNewModel())
    }

    constructModel = (obj: any): TimezoneModel => {
        return new TimezoneModel(obj.name, obj.locationName, obj.differenceToGMT);
    };

    constructEntity = (obj: any | null): Entity<TimezoneModel> => {
        return Entity.fromJSON(obj, this);
    };

}