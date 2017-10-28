import * as JWT from "jwt-decode";
import {TokenCodesModel} from "./Token.model";
import {RoleModel} from "./Role.model";

// https://gist.github.com/btroncone/d6cf141d6f2c00dc6b35
export class AuthenticationModel {

    readonly details;

    constructor(readonly tokenCodes: TokenCodesModel) {
        this.tokenCodes = tokenCodes;
        if (!!this.tokenCodes) {
            this.details = JWT(this.tokenCodes.accessToken);
        }
    }

    getUsername(): string {
        return this.details.sub;
    }

    hasAnyRole(...role: RoleModel[]) {
        if (this.details) {
            return role.map((roleId) => RoleModel[roleId])
                .some(v => this.details.authorities.includes(v))
        } else {
            return false;
        }
    }

    isAuthenticated(): boolean {
        return !!this.tokenCodes;
    }

    static noAuthentication(): AuthenticationModel {
        return new AuthenticationModel(null);
    }

    static authenticated(tokenCodes: TokenCodesModel): AuthenticationModel {
        return new AuthenticationModel(tokenCodes);
    }

}