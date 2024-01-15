export class User {
    private readonly _id: number;
    private readonly _username: string;
    private readonly _lastOnline: Date | undefined;
    private readonly _email: string | undefined;
    private readonly _role: string | undefined;

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

    get lastOnline(): Date | undefined {
        return this._lastOnline;
    }

    get email(): string | undefined {
        return this._email;
    }

    get role(): string | undefined {
        return this._role;
    }
}
