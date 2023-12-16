<template>
  <button :class="props.bStyle + ' ' + props.status" :disabled="disabled">
    <svg v-show="props.bStyle=='delete'" xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960"
         width="24">
      <path
          d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"/>
    </svg>
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

.save, .delete {
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

.delete.active {
  color: var(--red);
  background: transparent;
  border: solid 2px;
  border-color: var(--red);
}

.delete:hover {
  color: var(--secondary-color);
  font-weight: bold;
  background: var(--red);

}

.delete.waiting {
  color: var(--dark-gray);
  border: solid 2px;
  border-color: var(--dark-gray);
  background: transparent;
}

.delete > svg {
  fill: var(--red);
}

.delete:hover > svg {
  fill: var(--secondary-color);
}

.delete.waiting > svg {
  fill: var(--dark-gray);
}
</style>
