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
            this.permissionsAreEqual(role1.permissions, role2.permissions) &&
            this.membersAreEqual(role1.members, role2.members)
        );
    }

    static permissionsAreEqual(arr1: string[], arr2: string[]): boolean {
        return arr1.length === arr2.length && arr1.every((value, index) => value === arr2[index]);
    }

    static membersAreEqual(arr1: User[], arr2: User[]): boolean {
        if (arr1.length !== arr2.length)
            return false;
        arr1.sort((a, b) => a.id - b.id);
        arr2.sort((a, b) => a.id - b.id);
        for (let i = 0; i < arr1.length; i++) {
            if (arr1[i].id !== arr2[i].id || arr1[i].username !== arr2[i].username)
                return false;
        }
        return true;
    }

    static clone(role: Role): Role {
        return new Role(role.id, role.name, [...role.permissions], [...role.members]);
    }

    static toDto(role: Role): object {
        return {
            id: role.id,
            name: role.name,
            permissions: role.permissions,
            members: role.members
        };
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