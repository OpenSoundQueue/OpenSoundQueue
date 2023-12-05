<template>
  <main>
    <nav>
      <div class="mode-switcher">
        <router-link to="/admin/roles" class="link">Roles</router-link>
        <router-link to="/admin/users" class="link">Users</router-link>
      </div>
    </nav>
    <div class="role-container">
      <component :is="component"
                 :role-id="selectedRoleId"
                 @back="back"
                 @select-role="(id?: number) => selectRole(id)"
                 @to-display="toDisplay"
                 @to-members="toMembers"
                 @to-permissions="toPermissions"
      />
    </div>
    <div class="detail-container desktop">
      <nav class="desktop">
        <div class="nav-element"
             :class="[detailComponent === RoleDisplay ? 'active' : '']"
             @click="detailComponent = RoleDisplay">
          Display
          <div class="underline"></div>
        </div>
        <div class="nav-element"
             :class="[detailComponent === RoleMembers ? 'active' : '']"
             @click="detailComponent = RoleMembers">
          Members
          <div class="underline"></div>
        </div>
        <div class="nav-element"
             :class="[detailComponent === RolePermissions ? 'active' : '']"
             @click="detailComponent = RolePermissions">
          Permissions
          <div class="underline"></div>
        </div>
      </nav>
      <component :is="detailComponent"
                 :role-id="selectedRoleId"
      />
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

const props = defineProps<{
  roleId?: string
}>()

const component: ShallowRef<Component | undefined> = shallowRef(RoleList);
const detailComponent: ShallowRef<Component> = shallowRef(RoleDisplay);
const selectedRoleId: Ref<number | undefined> = ref(parseInt(typeof props.roleId === 'undefined' ? "" : props.roleId) ?? undefined);

onMounted(() => {
  chooseComponent();
})

watch(router.currentRoute, () => {
  chooseComponent();
})

function chooseComponent() {
  const routeName = router.currentRoute.value.name;
  console.log(routeName);

  if (routeName === 'roles') component.value = RoleList
  if (routeName === 'roles-display') component.value = RoleDisplay
  if (routeName === 'roles-members') component.value = RoleMembers
  if (routeName === 'roles-permissions') component.value = RolePermissions
}

function back() {
  router.back();
}

function toDisplay() {
  router.push("/admin/roles/display/" + selectedRoleId);
}

function selectRole(id?: number) {
  console.log(id);
  selectedRoleId.value = id;
}

function toMembers() {
  router.push('/admin/roles/members/' + selectedRoleId);
}

function toPermissions() {
  router.push('/admin/roles/permissions/' + selectedRoleId);
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

nav {
  height: 60px;
}

.mode-switcher {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 15px;
  height: 35px;
}

.link {
  height: 35px;
  width: 100%;
  background: var(--background-color);
  border: 3px solid var(--secondary-color);
  box-sizing: border-box;
  color: var(--text-color);
  font-size: var(--font-size-medium);
  border-radius: var(--border-radius-medium);
  display: flex;
  justify-content: center;
  align-items: center;
  text-decoration: none;
}

.link.router-link-active {
  color: var(--background-color);
  border-color: var(--tertiary-color);
  background: var(--tertiary-color);
  font-weight: bold;
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


@media screen and (min-width: 420px) {
  nav {
    width: 270px;
  }
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
    grid-template-rows: 60px calc(100% - 90px);
  }

  .detail-container {
    grid-column: 2;
    grid-row: 2;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
    padding: 20px;
  }

  .detail-container nav {
    display: flex;
    gap: 20px;
    height: 40px;
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
    grid-row: 2;
    height: 100%;
    border-bottom: none;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
    display: flex;
    flex-direction: column;
  }
}
</style>