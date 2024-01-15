<template>
  <div class="toggle-switch-container">
    <div class="check-container" :class="checked?'checked':''">
      <div class="background-fill"/>
      <div class="circle" :class="checked?'checked':''"/>
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
.toggle-switch-container{
  aspect-ratio: 2/1;
  height: calc(var(--font-size-medium));
  background-color: var(--background-color);
  border-radius: 999px;
  border: solid 2px var(--secondary-color);
  overflow: hidden;
}

.check-container{
  display: flex;
  flex-direction: row;
  align-items: center;
  height: 100%;
  margin-left: calc(var(--font-size-medium) * -1.5 - 2px);
  transition: 0.1s ease-in-out;
}

.background-fill{
  background-color: var(--pink);
  aspect-ratio: 2/1;
  height: 100%;
}

.circle{
  margin-left: calc(var(--font-size-medium) / -2);
  aspect-ratio: 1;
  height: 100%;
  background-color: var(--light-gray);
  border-radius: 999px;
  transition: 0.1s ease-in-out;
  border: solid 2px var(--secondary-color);

}

.check-container.checked{
  margin-left: calc(var(--font-size-medium) / -2 - 2px);
}
.circle.checked{
  background-color: var(--primary-color);
}
</style>