<template>
  <div class="role-display-wrapper">
    <nav class="mobile">
      <div class="nav-element back" @click="emit('back')">
        <img src="@/assets/icons/arrows/arrow_back.svg" :alt="$translate('altTexts.arrowBack')"/>
      </div>
      <div class="nav-element">
        <img src="@/assets/icons/role.svg" :alt="$translate('altTexts.role')"/>
        <div>Role Name</div>
      </div>
      <div class="nav-element"></div>
    </nav>
    <div class="nav-button-wrapper mobile">
      <div class="mobile overlay" @click="emit('toMembers')">
        <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
      </div>
      <div class="nav-button-container">
        Members
      </div>
    </div>
    <div class="nav-button-wrapper mobile">
      <div class="mobile overlay" @click="emit('toPermissions')">
        <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
      </div>
      <div class="nav-button-container">
        Permissions
      </div>
    </div>
    <div v-if="roleId">
      Display of Role with Id {{ roleId }}
    </div>
    <div v-else>
      Display of New Role
    </div>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
  toMembers: []
  toPermissions: []
}>();

defineProps<{
  roleId?: number
}>()
</script>

<style scoped>
.role-display-wrapper {
  position: fixed;
  top: 60px;
  left: 0;
  width: 100%;
  height: 100vh;
  background: var(--background-color);
  padding: 15px;
  box-sizing: border-box;
}

nav {
  display: flex;
  width: 100%;
  justify-content: space-between;
  box-sizing: border-box;
}

.nav-element {
  display: flex;
  align-items: center;
  width: 100%;
  gap: 5px;
}

.nav-element:nth-child(2) {
  justify-content: center;
}


.nav-element img {
  height: 25px;
}

.nav-element.back:hover {
  cursor: pointer;
}

.nav-button-wrapper {
  background: var(--background-color);
  position: relative;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 2px solid var(--secondary-color);
  border-radius: var(--border-radius-medium);
}

.nav-button-wrapper:hover {
  cursor: pointer;
}

.nav-button-wrapper .overlay {
  position: absolute;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
}

.nav-button-wrapper .overlay img {
  height: 35px;
}

@media screen and (min-width: 1250px) {
  .mobile {
    display: none;
  }

  .role-display-wrapper {
    position: static;
    height: fit-content;
    background: none;
  }
}
</style>
