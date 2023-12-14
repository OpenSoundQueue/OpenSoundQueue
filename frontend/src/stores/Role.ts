import {defineStore} from "pinia";
import {computed, ref} from "vue";
import type {Ref} from "vue";
import {Role} from "@/models/Role";
import {HttpService} from "@/services/HttpService";
import type {User} from "@/models/User";
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

    patchedRole.value = cloneRole(fetchedRole.value);

    async function newSelection(id: number) {
        await httpService.getRole(id)
            .then((role: Role) => {
                fetchedRole.value = role;
                patchedRole.value = cloneRole(role);
            })
            .catch((error) => {
                fetchedRole.value = undefined;
                patchedRole.value = undefined;
            });
    }

    function cloneRole(role: Role | undefined): Role | undefined {
        if (role === undefined)
            return undefined;
        return structuredClone({...role,
            id: role.id,
            name: role.name,
            permissions: [...role.permissions],
            members: [...role.members]},)
    }

    async function rollback() {
        patchedRole.value = cloneRole(fetchedRole.value);
    }

    async function save() {
        if (patchedRole.value != undefined)
            await httpService.updateRole(patchedRole.value)
                .then((role: Role) => {
                    fetchedRole.value = role;
                    patchedRole.value = role;
                })
                .catch(() => {
                    fetchedRole.value = undefined;
                    patchedRole.value = undefined;
                    ToastService.sendNotification(translate('popUp.editRole.saveError'),"error",3000)
                });
    }

    async function patchName(name: string) {
        if (name.length === 0 || patchedRole.value === undefined) {
            return;
        }
        patchedRole.value.name = name;
    }

    async function patchPermissions(permissions: string[]) {
        if (permissions.length === 0 || patchedRole.value === undefined) {
            return;
        }
        patchedRole.value.permissions = permissions;
    }

    async function patchMember(members: User[]) {
        if (members.length == 0 || patchedRole.value == undefined)
            return

        patchedRole.value.members = members;
    }

    return {
        fetchedRole,
        patchedRole,
        roleEdited,
        newSelection,
        rollback,
        save,
        patchPermissions,
        patchMember,
        patchName
    }

})