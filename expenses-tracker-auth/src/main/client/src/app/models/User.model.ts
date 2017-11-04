import {RoleModel} from "./Role.model";

export default class UserModel implements ModelEntity {

    constructor(
        public username: string,
        public email: string,
        public authorities: RoleModel[] = [],
        public firstName: string = null,
        public lastName: string = null,
    ) {}

    isKnown(): boolean {
        return !!this.username
    }

    hasAnyRole(...role: RoleModel[]) {
        if (this.authorities) {
            return role.some(v => this.authorities.lastIndexOf(v) >= 0)
        } else {
            return false;
        }
    }

}

export const unknownUser = () => new UserModel(null, null);