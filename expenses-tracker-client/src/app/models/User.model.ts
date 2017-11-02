import {RoleModel} from "./Role.model";
export class UserModel implements ModelEntity {

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

    static unknownUser(): UserModel {
        return new UserModel(null, null);
    }

}