import { defineStore } from "pinia";
import { computed, ref } from "vue";
import type { Ref } from "vue";
import { User } from "@/models/User";
import { Role } from "@/models/Role";
import { HttpService } from "@/services/HttpService";
import { ToastService } from "@/services/ToastService";
import { translate } from "@/plugins/TranslationPlugin";

// Initialize HttpService
const httpService = new HttpService();

// Define role store
export const useRoleStore = defineStore('role', () => {
    // Reactive references for fetched and patched role
    const fetchedRole: Ref<Role | undefined> = ref();
    const patchedRole: Ref<Role | undefined> = ref();

    // Computed property to determine if role has been edited
    const roleEdited = computed(() => {
        if (fetchedRole.value === undefined || patchedRole.value === undefined) {
            return false;
        }
        return !Role.areEqual(fetchedRole.value, patchedRole.value);
    });

    // Method to fetch a new role selection
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

    // Method to rollback role changes
    function rollback() {
        if (fetchedRole.value === undefined) {
            patchedRole.value = undefined;
        } else {
            patchedRole.value = Role.clone(fetchedRole.value);
        }
    }

    // Method to save role changes
    async function save() {
        if (patchedRole.value == undefined) return;

        // Create new role if ID is -1
        if (patchedRole.value?.id == -1) {
            await httpService.createRole({
                "name": patchedRole.value.name,
                "permissions": []
            })
                .then((role: Role) => {
                    fetchedRole.value = role;
                    patchedRole.value = Role.clone(role);
                    ToastService.sendNotification(translate('popUp.editRole.createSuccess'), "success", 3000);
                })
                .catch(() => {
                    if (fetchedRole.value != undefined)
                        patchedRole.value = Role.clone(fetchedRole.value);
                    ToastService.sendNotification(translate('popUp.editRole.createError'), "error", 3000);
                });
        }

        // Update existing role
        await httpService.updateRole(Role.toDto(patchedRole.value))
            .then((role: Role) => {
                fetchedRole.value = role;
                patchedRole.value = Role.clone(role);
                ToastService.sendNotification(translate('popUp.editRole.saveSuccess'), "success", 3000);
            })
            .catch(() => {
                if (fetchedRole.value != undefined)
                    patchedRole.value = Role.clone(fetchedRole.value);
                ToastService.sendNotification(translate('popUp.editRole.saveError'), "error", 3000);
            });
    }

    // Method to delete role
    async function deleteRole() {
        if (fetchedRole.value != undefined) {
            await httpService.deleteRole(fetchedRole.value?.id)
                .then(() => {
                    newRole();
                    ToastService.sendNotification(translate('popUp.editRole.deleteSuccess'), "success", 3000);
                })
                .catch(() => {
                    fetchedRole.value = undefined;
                    patchedRole.value = undefined;
                    ToastService.sendNotification(translate('popUp.editRole.deleteError'), "error", 3000);
                });
        }
    }

    // Method to patch role name
    function patchName(name: string) {
        if (name.length === 0 || patchedRole.value === undefined) return;
        patchedRole.value.name = name;
    }

    // Method to patch role permissions
    function patchPermissions(permissions: string[]) {
        if (permissions.length === 0 || patchedRole.value === undefined) return;
        patchedRole.value.permissions = permissions;
    }

    // Method to patch role members
    function patchMember(members: User[]) {
        if (members.length == 0 || patchedRole.value == undefined) return;
        patchedRole.value.members = members;
    }

    // Method to toggle role member
    function toggleMember(id: number, name: string) {
        if (patchedRole.value == undefined) return;
        for (let i = 0; i < patchedRole.value?.members.length; i++) {
            if (patchedRole.value?.members[i].id == id) {
                patchedRole.value?.members.splice(i, 1);
                return;
            }
        }
        patchedRole.value?.members.push(<User>{
            id: id,
            username: name
        });
    }

    // Method to toggle role permission
    function togglePermission(name: string) {
        if (patchedRole.value == undefined) return;
        for (let i = 0; i < patchedRole.value?.permissions.length; i++) {
            if (patchedRole.value?.permissions[i] == name) {
                patchedRole.value?.permissions.splice(i, 1);
                return;
            }
        }
        patchedRole.value?.permissions.push(name);
    }

    // Method to create new role
    function newRole() {
        fetchedRole.value = Role.createNewRole();
        patchedRole.value = Role.createNewRole();
    }

    // Return methods and reactive properties
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
        togglePermission,
        newRole
    };
});