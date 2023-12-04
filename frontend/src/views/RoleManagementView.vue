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
                 :role-id="roleId"
                 @back="back"
                 @select-role="(id?: number) => selectRole(id)"
                 @to-display="toDisplay"
                 @to-members="toMembers"
                 @to-permissions="toPermissions"
      />
    </div>
    <div class="detail-container desktop">
      <component :is="detailComponent"
                 :role-id="roleId"
                 @to-display="toDisplay"
                 @to-members="toMembers"
                 @to-permissions="toPermissions"
      />
    </div>
    <GridBackground/>
  </main>
</template>

<script setup lang="ts">
import GridBackground from "@/components/background/GridBackground.vue";
import {ref, shallowRef} from "vue";
import type {Ref, ShallowRef, Component} from "vue";
import RoleList from "@/components/roles/RoleList.vue";
import RoleDisplay from "@/components/roles/RoleDisplay.vue";
import RoleMembers from "@/components/roles/RoleMembers.vue";
import RolePermissions from "@/components/roles/RolePermissions.vue";

const component: ShallowRef<Component> = shallowRef(RoleList);
const detailComponent: ShallowRef<Component> = shallowRef(RoleDisplay);
const history: ShallowRef<Component[]> = shallowRef([RoleList]);
const roleId: Ref<number | undefined> = ref();

function back() {
  if (history.value.length < 1) {
      return;
  }

  component.value = history.value.pop();
}

function toDisplay() {
  history.value.push(component.value);
  component.value = RoleDisplay;
  detailComponent.value = RoleDisplay;
}

function selectRole(id?: number) {
  roleId.value = id;
}

function toMembers() {
  history.value.push(component.value);
  component.value = RoleMembers;
  detailComponent.value = RoleMembers;
}

function toPermissions() {
  history.value.push(component.value);
  component.value = RolePermissions;
  detailComponent.value = RolePermissions;
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
  justify-content: space-between;
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
  height: 50px;
}

.link {
  height: 35px;
  width: 100%;
  background: var(--background-color);
  border: 3px solid var(--secondary-color);
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
}

.detail-container {
  flex: 0 0 auto;
}


@media screen and (min-width: 420px) {
  nav {
    width: 270px;
  }
}

@media screen and (min-width: 1250px) {
  .desktop {
    display: initial;
  }

  main {
    width: 1250px;
    display: grid;
    grid-template-columns: 33% 66%;
    grid-template-rows: 60px calc(100% - 90px);
  }

  .detail-container {
    grid-column: 2;
    grid-row: 2;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
  }

  .role-container {
    grid-column: 1;
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