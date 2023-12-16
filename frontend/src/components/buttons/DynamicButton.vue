<template>
  <button :class="props.bStyle + ' ' + props.status" :disabled="disabled">
    <slot></slot>
  </button>

</template>

<script setup lang="ts">
import {computed} from "vue";

const props = defineProps<{
  status: String,
  bStyle: String
}>()

const disabled = computed(() => {
  return (props.status === 'inactive' || props.status === 'waiting')
})
</script>

<style scoped>
button {
  width: 100%;
  background: var(--primary-color);
  height: 40px;
  border: none;
  border-radius: var(--border-radius-medium);
  color: var(--text-color);
  font-size: var(--font-size-medium);
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;

  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  box-sizing: border-box;
  -moz-box-sizing: border-box;
  -webkit-box-sizing: border-box;

  box-shadow: 0 4px 4px 0 rgba(0, 0, 0, 0.25);
}

button:hover {
  cursor: pointer;
}

.save{
  box-shadow: none;
  margin-top: 0;
}

.login.active.save.active {
  color: var(--text-color);
  background: var(--primary-color);
}

.login.inactive.save.inactive {
  color: var(--text-color);
  background: var(--dark-gray);
}

.login.waiting.save.waiting {
  color: var(--text-color);
  border: solid 2px;
  border-color: var(--primary-color);
  background: transparent;
}

</style>
