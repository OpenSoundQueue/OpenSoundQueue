<template>
  <div>
    <div class="new-role-button-wrapper">
      <div class="mobile overlay" @click="() => toDisplay()">
        <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
      </div>
      <div class="new-role-button-container" @click="() => selectRole()">
        New Role
      </div>
    </div>
    <p>search</p>

    <div class="select-role-button-wrapper" v-for="id in 5">
      <div class="mobile overlay" @click="toDisplay(id)">
        <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
      </div>
      <div class="select-role-button-container" @click="selectRole(id)">
        <img src="@/assets/icons/role.svg" :alt="$translate('altTexts.role')"/>
        Role {{ id }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
  toDisplay: []
  selectRole: [id?: number]
}>();

function toDisplay(roleId?: number) {
  emit("selectRole", roleId);
  emit("toDisplay");
}

function selectRole(roleId?: number) {
  emit("selectRole", roleId);
}
</script>

<style scoped>
.new-role-button-wrapper {
  background: var(--primary-color);
  position: relative;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: var(--border-radius-medium);
}

.new-role-button-wrapper .overlay {
  position: absolute;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
}

.new-role-button-wrapper .overlay img {
  height: 35px;
}

.select-role-button-wrapper {
  background: var(--background-color);
  position: relative;
  height: 40px;
  border: 2px solid var(--secondary-color);
  border-radius: var(--border-radius-medium);
}

.select-role-button-wrapper .overlay {
  position: absolute;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
  height: 100%;
}

.select-role-button-wrapper .overlay img {
  height: 35px;
}

.select-role-button-container {
  height: 100%;
  display: flex;
  align-items: center;
  gap: 10px;
  padding-left: 10px;
}

.select-role-button-container img {
  height: 25px;
}

@media screen and (min-width: 1250px) {
    .mobile {
      visibility: hidden;
    }
}
</style>