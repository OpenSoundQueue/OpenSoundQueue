export class Permission {
    private readonly _name;
    private readonly _value: boolean;

    constructor(name: string, value: boolean) {
        this._name = name;
        this._value = value;
    }

    get name() {
        return this._name;
    }

    get value() {
        return this._value;
    }
}