import {ref} from "vue";
import type {Ref} from "vue";
import {HttpService} from "./HttpService";

// Creates an instance of HttpService.
const httpService = new HttpService();

// Defines the types of permissions.
export type PermissionType =
    "VOTESKIP" |
    "SKIP" |
    "ADD_SONG" |
    "REPLAY" |
    "HISTORY_SEARCH" |
    "PAUSE_PLAY" |
    "CHANGE_VOLUME" |
    "CHANGE_ORDER" |
    "DELETE_SONGS" |
    "MANAGE_ROLES" |
    "MANAGE_USER" |
    "MANAGE_SYSTEM_SETTINGS";

// Defines the PermissionService class.
export class PermissionService{
    // Initializes a reactive reference to hold the permissions.
    static permissions:Ref<PermissionType[]> = ref([]);

    // Fetches permissions from the server.
    static async getPermissions(){
        await httpService.getOwnPermissions()
            .then((permissions)=>{
                this.permissions.value = <PermissionType[]> permissions
            })
            .catch(()=>{
                this.permissions.value = [];
            })
    }

    // Checks if a specific permission is granted.
    static checkPermission(permission:PermissionType){
        return this.permissions.value.includes(permission)
    }

    // Checks if any of the specified permissions are granted.
    static hasAnyPermission(permissionArray: PermissionType[]) {
        return permissionArray.some((permission) => this.checkPermission(permission));
    }

    // Checks if all the specified permissions are granted.
    static hasAllPermissions(permissionArray: PermissionType[]) {
        return permissionArray.every((permission) => this.checkPermission(permission));
    }
}
