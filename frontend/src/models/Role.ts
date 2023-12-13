import {User} from "@/models/User";

export class Role {
    readonly _id;
    _name;
    _permissions;
    _members;


    constructor(id: number, name: string, permissions: string[], members: User[]) {
        this._id = id;
        this._name = name;
        this._permissions = permissions;
        this._members = members;
    }

    static areEqual(role1: Role, role2: Role): boolean {
        return (
            role1.id === role2.id &&
            role1.name === role2.name &&
            this.arraysAreEqual(role1.permissions, role2.permissions)
        );
    }

    static arraysAreEqual(arr1: string[], arr2: string[]): boolean {
        return arr1.length === arr2.length && arr1.every((value, index) => value === arr2[index]);
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

    get members() {
        return this._members;
    }

    set name(value: string) {
        this._name = value;
    }

    set permissions(value: string[]) {
        this._permissions = value;
    }


    set members(value) {
        this._members = value;
    }
}