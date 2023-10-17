<template>
  <div class="button-wrapper">
    <button :disabled="isDisabled || isLoading" :class="{loading: isLoading}">
      <span class="button-container" v-if="!isLoading">
        <slot></slot>
        {{ text }}
      </span>
      <span v-else class="loading-container">
        <LoadingAnimation :duration="500" :dot-count="3" size="small"/>
      </span>
    </button>
  </div>
</template>

<script setup lang="ts">
import LoadingAnimation from "@/components/LoadingAnimation.vue";

defineProps<{
  text: string,
  isDisabled?: boolean,
  isLoading?: boolean
}>();
</script>


<style scoped>
.button-wrapper {
  width: 100%;
}

button {
  width: 100%;
  background: var(--primary-color);
  height: 40px;
  border: none;
  border-radius: var(--border-radius-medium);
}

.button-container {
  width: 100%;
  color: var(--text-color);
  font-size: var(--font-size-medium);
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.loading-container {
  width: 100px;
  margin: auto;
}

button:hover {
  cursor: pointer;
}

button.loading:disabled {
  background: var(--primary-color);
}

button:disabled {
  background: var(--dark-gray);
}
</style>