<template>
  <nav>
    <div class="nav-element back" @click="emit('back')">
      <img src="@/assets/icons/arrows/arrow_back.svg" :alt="$translate('altTexts.arrowBack')"/>
    </div>
    <div class="nav-element">
      <img src="@/assets/icons/role.svg" :alt="$translate('altTexts.role')"/>
      <div>Role Name</div>
    </div>
    <div class="nav-element"></div>
  </nav>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: []
}>()
</script>

<style scoped>
nav {
  width: 100%;
  display: flex;
  height: 40px;
  justify-content: space-between;
  align-items: flex-start;
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

</style>