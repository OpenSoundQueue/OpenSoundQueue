<template>
  <div class="checkbox" :class="checked?'checked':''">
    <svg v-show="checked" xmlns="http://www.w3.org/2000/svg" width="8" height="6" viewBox="0 0 8 6" fill="none">
      <path d="M2.66667 6L0 3.44681L0.933333 2.55319L2.66667 4.21277L7.06667 0L8 0.893617L2.66667 6Z" fill="white"/>
    </svg>
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
.checkbox {
  display: flex;
  justify-content: center;
  align-items: center;

  aspect-ratio: 1/1;
  height: 20px;
  box-sizing: border-box;
  border: 2px solid rgb(var(--tertiary-color));
  border-radius: 2px;
  background-color: transparent;
}

.checked {
  background-color: rgb(var(--primary-color));
  border-color: rgb(var(--primary-color));
}

svg {
  aspect-ratio: 1;
  height: 100%;
}
</style>