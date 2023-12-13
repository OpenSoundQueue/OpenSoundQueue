export class User {
    private readonly _id: number;
    private readonly _username: string;
    private readonly _lastOnline: Date;
    private readonly _email: string;
    private readonly _role: string;

    constructor(id: number, username: string, lastOnline?: Date, email?: string, role?: string) {
        this._id = id;
        this._username = username;
        this._lastOnline = lastOnline;
        this._email = email;
        this._role = role;
    }

    get id(): number {
        return this._id;
    }

    get username(): string {
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
