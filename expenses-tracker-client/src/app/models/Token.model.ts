export class TokenCodesModel {

    readonly accessToken: string;

    constructor(private token: string) {
        this.accessToken = token;
    }

}