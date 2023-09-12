<template>
  <div class="button-wrapper">
    <button>
      <img :src="iconPath" :alt="iconAlt"/>
      {{ text }}
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  text: string,
  iconPath: string,
  iconAlt: string
}>();
</script>


<style scoped>
.button-wrapper {
  width: 100%;
  margin: 20px 0 20px 0;
}

button {
  width: 100%;
  background: var(--primary-color);
  height: 40px;
  border: none;
  border-radius: var(--border-radius-medium);
  color: var(--text-color);
  font-size: var(--font-size-medium);
}

button:hover {
  cursor: pointer;
}
</style>