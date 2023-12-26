import {ref} from "vue";
import type {Ref} from "vue";
import {HttpService} from "./HttpService";

const httpService = new HttpService();

export type PermissionType =
    "VOTESKIP" |
    "SKIP" |
    "ADD_SONG" |
    "HISTORY_SEARCH" |
    "PAUSE_PLAY" |
    "CHANGE_VOLUME" |
    "CHANGE_ORDER" |
    "DELETE_SONGS" |
    "MANAGE_ROLES" |
    "MANAGE_USER";
export class PermissionService{
    static permissions:Ref<PermissionType[]> = ref([]);

    static async getPermissions(){
        await httpService.getOwnPermissions()
            .then((permissions)=>{
                this.permissions.value = <PermissionType[]> permissions
            })
            .catch(()=>{
                this.permissions.value = [];
            })
    }

    static checkPermission(permission:PermissionType){
        return this.permissions.value.includes(permission)
    }

    static hasAnyPermission(permissionArray: PermissionType[]) {
        return permissionArray.some((permission) => this.checkPermission(permission));
    }
}