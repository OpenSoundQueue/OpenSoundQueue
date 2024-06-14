<template>
  <div class="toggle-switch-container" :class="checked ? 'checked' : ''">
    <div class="check-container" :class="checked ? 'checked' : ''">
      <div class="background-fill"/>
      <div class="circle" :class="checked ? 'checked' : ''"/>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  checked: boolean
}>();

defineEmits<{
  change: []
}>()
</script>

<style scoped>
.toggle-switch-container {
  aspect-ratio: 2/1;
  width: 45px;
  height: 20px;
  background-color: rgb(var(--secondary-color));
  border-radius: 999px;
  border: solid 2px rgb(var(--tertiary-color));
  overflow: hidden;
}

.check-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  height: 100%;
  /*margin-left: calc(var(--font-size-medium) * -1.5 - 2px);*/
  transform: translateX(calc(-45px + 20px / 2 + 3px));
  transition: 0.1s ease-in-out;
}

.background-fill {
  background-color: rgb(var(--primary-color));
  aspect-ratio: 2/1;
  height: 100%;
}

.circle {
  /*margin-left: calc(var(--font-size-medium) / -2);*/
  transform: translateX(calc(var(--font-size-medium) / -2));
  height: 20px;
  aspect-ratio: 1;
  background-color: rgb(var(--tertiary-color));
  border-radius: 50%;
  transition: 0.1s ease-in-out;
  border: rgb(var(--secondary-color)) 2px solid;
  box-sizing: border-box;
}

.toggle-switch-container.checked {
  border-color: rgb(var(--primary-color));
}

.check-container.checked {
  transform: translateX(calc(-10px + 3px));
}

.circle.checked {
  background-color: rgb(var(--light-gray));
  border-color: rgb(var(--primary-color));
}
</style>