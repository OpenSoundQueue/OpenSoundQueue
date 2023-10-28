export class User {
    private readonly _username: string;
    private readonly _lastOnline: Date;
    private readonly _email: string;
    private readonly _role: string;

    constructor(username: string, lastOnline: Date, email: string, role: string) {
        this._username = username;
        this._lastOnline = lastOnline;
        this._email = email;
        this._role = role;
    }

    get getUsername(): string {
        return this._username;
    }

    get lastOnline(): Date {
        return this._lastOnline;
    }

    get email(): string {
        return this._email;
    }

    get role(): string {
        return this._role;
    }
}
