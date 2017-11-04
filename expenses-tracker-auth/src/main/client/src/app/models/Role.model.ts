export enum RoleModel {

    ROLE_ADMIN, ROLE_USER, ROLE_MANAGER

}

export function RoleModelAware(constructor: Function) {
    constructor.prototype.RoleModel = RoleModel;
}