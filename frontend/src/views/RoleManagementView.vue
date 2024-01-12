<template>
  <main :class="{'show-mode-switcher': hasAllManagementPermissions}">
    <AdminNavigation v-show="hasAllManagementPermissions"/>
    <div class="role-container">
      <component :is="component"
                 :role-id="selectedRoleId"
                 @select-role="(id?: number) => selectRole(id)"
      />
    </div>
    <div class="detail-container desktop">
      <nav class="desktop">
        <div class="nav-element"
             :class="[detailComponent === RoleDisplay ? 'active' : '']"
             @click="changeTab(RoleDisplay)">
          {{ $translate('roleEdit.navigation.display') }}
          <div class="underline"></div>
        </div>
        <div class="nav-element"
             :class="[detailComponent === RoleMembers ? 'active' : '']"
             @click="changeTab(RoleMembers)">
          {{ $translate('roleEdit.navigation.members') }}
          <div class="underline"></div>
        </div>
        <div class="nav-element"
             :class="[detailComponent === RolePermissions ? 'active' : '']"
             @click="changeTab(RolePermissions)">
          {{ $translate('roleEdit.navigation.permissions') }}
          <div class="underline"></div>
        </div>
        <div v-show="store.roleEdited" class="nav-element save-button">
          <!-- TODO: Style title -->
          <img class="undo" src="@/assets/icons/undo.svg"
               :alt="translate('altTexts.undo')"
               :title="translate('roleEdit.rollback')"
               @click="store.rollback()"/>
          <DynamicButton b-style="save" :status="saveButtonState" @click="save()">{{
              translate('roleEdit.save')
            }}
          </DynamicButton>
        </div>
      </nav>
      <component :is="detailComponent"
                 :role="store.patchedRole"/>
    </div>
    <GridBackground/>
  </main>
</template>

<script setup lang="ts">
import GridBackground from "@/components/background/GridBackground.vue";
import {onMounted, ref, shallowRef, watch} from "vue";
import type {Ref, ShallowRef, Component} from "vue";
import RoleList from "@/components/roles/RoleList.vue";
import RoleDisplay from "@/components/roles/RoleDisplay.vue";
import RoleMembers from "@/components/roles/RoleMembers.vue";
import RolePermissions from "@/components/roles/RolePermissions.vue";
import router from "@/router";
import {useRoleStore} from "@/stores/Role";
import {PopUpService} from "@/services/PopUpService";
import {translate} from "@/plugins/TranslationPlugin";
import DynamicButton from "@/components/buttons/DynamicButton.vue";
import {ToastService} from "@/services/ToastService";
import {PermissionService} from "@/services/PermissionService";
import AdminNavigation from "@/components/AdminNavigation.vue";

const store = useRoleStore();


const props = defineProps<{
  roleId?: string
}>()

const component: ShallowRef<Component | undefined> = shallowRef(RoleList);
const detailComponent: ShallowRef<Component> = shallowRef(RoleDisplay);
const selectedRoleId: Ref<number | undefined> = ref(parseInt(typeof props.roleId === 'undefined' ? "" : props.roleId) ?? undefined);
const hasAllManagementPermissions = ref(true);
const saveButtonState = ref("active")

onMounted(async () => {
  await PermissionService.getPermissions();
  if (PermissionService.checkPermission("MANAGE_ROLES"))
    await router.push("/admin/roles");
  else
    await router.push("/admin/users");

  hasAllManagementPermissions.value = PermissionService.hasAllPermissions(["MANAGE_ROLES", "MANAGE_USER"])

  if (props.roleId != undefined) {
    await store.newSelection(Number(props.roleId))
  }
  chooseComponent();
})

watch(router.currentRoute, () => {
  chooseComponent();
})

async function changeTab(component: Component) {
  if (store.patchedRole?.id == -1) {
    ToastService.sendNotification(translate('popUp.editRole.newRoleRedirectError'), "error", 3000)
    return
  }
  if (store.roleEdited) {
    PopUpService.openPopUp(translate('popUp.editRole.unsavedChanges'), translate('popUp.editRole.save'));
    const userAction = await PopUpService.waitForUserAction();

    if (userAction === "accepted") {
      await store.save();
    } else {
      await store.rollback();
    }
  }
  detailComponent.value = component
}

function chooseComponent() {
  const routeName = router.currentRoute.value.name;

  if (routeName === 'roles') component.value = RoleList
  if (routeName === 'roles-display') component.value = RoleDisplay
  if (routeName === 'roles-members') component.value = RoleMembers
  if (routeName === 'roles-permissions') component.value = RolePermissions
}

function selectRole(id?: number) {
  if (id == -1) {
    detailComponent.value = RoleDisplay;
  }
  selectedRoleId.value = id;
}

async function save() {
  if (saveButtonState.value != "active")
    return

  saveButtonState.value = "waiting"
  await store.save()
  selectRole(store.fetchedRole?.id)
  saveButtonState.value = "active"
}
</script>

<style scoped>
.desktop {
  display: none;
}

main {
  width: calc(100% - 30px);
  height: calc(100vh - 60px);
  margin: 0 auto 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  box-sizing: border-box;
  padding-top: 20px;
}

.role-container {
  overflow-y: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.detail-container {
  flex: 0 0 auto;
}

.save-button {
  margin-top: 0 !important;
  margin-left: auto;

  display: flex;
  flex-direction: row;
  gap: 10px;
}

.save-button > button {
  min-width: 100px;
}

.undo {
  height: 30px;
  margin: auto 0 auto 0;
}

@media screen and (min-width: 800px) {
  main {
    width: 800px;
  }
}

@media screen and (min-width: 1250px) {
  .desktop {
    display: initial;
  }

  main {
    width: 1250px;
    display: grid;
    justify-content: space-between;
    grid-template-columns: 33% 66%;
    grid-template-rows: calc(100% - 30px);
  }

  main.show-mode-switcher {
    grid-template-rows: 60px calc(100% - 90px);

    .role-container {
      grid-row: 2;
    }

    .detail-container {
      grid-row: 2;
    }
  }


  .detail-container {
    grid-column: 2;
    grid-row: 1;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
    padding: 20px;
  }

  .detail-container nav {
    display: flex;
    width: 100%;
    gap: 20px;
    height: 40px;
  }

  .detail-container > div {
    height: calc(100% - 40px);
    width: 100%;
  }

  .nav-element {
    margin-top: 10px;
  }

  .nav-element:hover {
    cursor: pointer;
  }

  .nav-element.active {
    font-weight: bold;
  }

  .nav-element.active .underline {
    background: var(--primary-color);
    margin-top: 4px;
    height: 3px;
    width: 100%;
  }

  .role-container {
    grid-column: 1;
    padding: 20px;
    box-sizing: border-box;
    grid-row: 1;
    height: 100%;
    border-bottom: none;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
    display: flex;
    flex-direction: column;
  }
}
</style>