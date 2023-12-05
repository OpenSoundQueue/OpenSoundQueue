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

    <div class="select-role-button-wrapper" v-for="id in 5" :class="id === roleId ? 'selected' : ''">
      <div class="mobile overlay" @click="toDisplay(id)">
        <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
      </div>
      <div class="select-role-button-container" @click="selectRole(id)">
        <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M480-440q-59 0-99.5-40.5T340-580q0-59 40.5-99.5T480-720q59 0 99.5 40.5T620-580q0 59-40.5 99.5T480-440Zm0-80q26 0 43-17t17-43q0-26-17-43t-43-17q-26 0-43 17t-17 43q0 26 17 43t43 17Zm0 440q-139-35-229.5-159.5T160-516v-244l320-120 320 120v244q0 152-90.5 276.5T480-80Zm0-400Zm0-315-240 90v189q0 54 15 105t41 96q42-21 88-33t96-12q50 0 96 12t88 33q26-45 41-96t15-105v-189l-240-90Zm0 515q-36 0-70 8t-65 22q29 30 63 52t72 34q38-12 72-34t63-52q-31-14-65-22t-70-8Z"/></svg>
        Role {{ id }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  roleId: number
}>();

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

.new-role-button-wrapper:hover {
  cursor: pointer;
}

.new-role-button-wrapper .overlay {
  position: absolute;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
}

.new-role-button-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
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

.select-role-button-wrapper:hover {
  cursor: pointer;
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

.select-role-button-container svg {
  height: 25px;
  fill: var(--tertiary-color);
}

@media screen and (min-width: 1250px) {
  .mobile {
    visibility: hidden;
  }

  .select-role-button-wrapper {
    border: none;
    background: none;
  }

  .select-role-button-wrapper.selected {
    background: var(--tertiary-color);
    color: var(--background-color);
    font-weight: bold;
  }

  .select-role-button-wrapper.selected svg {
    fill: var(--background-color);
  }
}
</style>