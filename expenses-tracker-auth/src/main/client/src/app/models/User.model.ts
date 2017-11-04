class UserModel {

    constructor(
        readonly username: string,
        readonly authenticated: boolean
    ) {}

}

const unknownUser = () => new UserModel(null, false);

export { unknownUser }
export default UserModel