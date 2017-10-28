export class UserProfile implements ModelEntity {

    constructor(public username: string,
                public email: string,
                public firstName: string = null,
                public lastName: string = null) {
    }

}