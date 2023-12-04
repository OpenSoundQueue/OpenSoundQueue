export class Role {
    private readonly _id;
    private readonly _name;
    private readonly _permissions;


    constructor(id: number, name: string, permissions: []) {
        this._id = id;
        this._name = name;
        this._permissions = permissions;
    }


    get id() {
        return this._id;
    }

    get name() {
        return this._name;
    }

    get permissions() {
        return this._permissions;
    }
}