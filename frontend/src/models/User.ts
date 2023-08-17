export interface IUser {
    username: string;
    password: string;
    sessionKey: string;
}

export class User implements IUser {
    private readonly _username: string;
    private readonly _password: string;
    private readonly _sessionKey: string;

    constructor(username: string, password: string, sessionKey: string) {
        this._username = username;
        this._password = password;
        this._sessionKey = sessionKey;
    }

    get username(): string {
        return this._username;
    }

    get password(): string {
        return this._password;
    }

    get sessionKey(): string {
        return this._sessionKey;
    }
}