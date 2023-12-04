export class Permission {
    private readonly _name;

    constructor(name:string) {
        this._name = name;
    }

    get name() {
        return this._name;
    }
}