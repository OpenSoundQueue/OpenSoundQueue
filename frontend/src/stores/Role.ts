import {defineStore} from "pinia";
import {computed, ref} from "vue";
import type {Ref} from "vue";
import {Role} from "@/models/Role";
import {HttpService} from "@/services/HttpService";
import {User} from "@/models/User";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";

const httpService = new HttpService();

export const useRoleStore = defineStore('role', () => {
    const fetchedRole: Ref<Role | undefined> = ref();
    const patchedRole: Ref<Role | undefined> = ref();
    const roleEdited = computed(() => {
        if (fetchedRole.value === undefined || patchedRole.value === undefined) {
            return false;
        }

        return !Role.areEqual(fetchedRole.value, patchedRole.value);
    });

    async function newSelection(id: number) {
        await httpService.getRole(id)
            .then((role: Role) => {
                fetchedRole.value = role;
                patchedRole.value = Role.clone(role);
            })
            .catch(() => {
                fetchedRole.value = undefined;
                patchedRole.value = undefined;
            });
    }

    function rollback() {
        if (fetchedRole.value === undefined)
            patchedRole.value = undefined;
        else
            patchedRole.value = Role.clone(fetchedRole.value)
    }

    async function save() {
        if (patchedRole.value != undefined)
            await httpService.updateRole(Role.toDto(patchedRole.value))
                .then((role: Role) => {
                    fetchedRole.value = role;
                    patchedRole.value = Role.clone(role);
                    ToastService.sendNotification(translate('popUp.editRole.saveSuccess'), "success", 3000)
                })
                .catch((error) => {
                    if (fetchedRole.value != undefined)
                        patchedRole.value = Role.clone(fetchedRole.value)
                    ToastService.sendNotification(translate('popUp.editRole.saveError'), "error", 3000)
                });
    }

    async function deleteRole() {
        if (fetchedRole.value != undefined)
            await httpService.deleteRole(fetchedRole.value?.id)
                .then(() => {
                    fetchedRole.value = undefined;
                    patchedRole.value = undefined;
                })
                .catch(() => {
                    fetchedRole.value = undefined;
                    patchedRole.value = undefined;
                    ToastService.sendNotification(translate('popUp.editRole.saveError'), "error", 3000)
                });
    }

    function patchName(name: string) {
        if (name.length === 0 || patchedRole.value === undefined) {
            return;
        }
        patchedRole.value.name = name;
    }

    function patchPermissions(permissions: string[]) {
        if (permissions.length === 0 || patchedRole.value === undefined) {
            return;
        }
        patchedRole.value.permissions = permissions;
    }

    function patchMember(members: User[]) {
        if (members.length == 0 || patchedRole.value == undefined)
            return

        patchedRole.value.members = members;
    }

    function toggleMember(id: number, name: string) {
        if (patchedRole.value == undefined)
            return;
        for (let i = 0; i < patchedRole.value?.members.length; i++) {
            if (patchedRole.value?.members[i].id == id) {
                patchedRole.value?.members.splice(i, 1)
                return;
            }
        }
        patchedRole.value?.members.push(<User>{
            id: id,
            username: name
        })
    }

    function togglePermission(name: string) {
        if (patchedRole.value == undefined)
            return;
        for (let i = 0; i < patchedRole.value?.permissions.length; i++) {
            if (patchedRole.value?.permissions[i] == name) {
                patchedRole.value?.permissions.splice(i, 1)
                return;
            }
        }
        patchedRole.value?.permissions.push(name)
    }

    return {
        fetchedRole,
        patchedRole,
        roleEdited,
        newSelection,
        rollback,
        save,
        deleteRole,
        patchPermissions,
        patchMember,
        patchName,
        toggleMember,
        togglePermission
    }

})