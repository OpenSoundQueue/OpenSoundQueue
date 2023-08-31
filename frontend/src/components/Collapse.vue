<template>
 <div class="collapse-wrapper">
   <div class="open-close-container" @click="toggleCollapse">
     <div class="label">{{ label }}</div>
     <div class="icon">
       <img alt="arrow down" v-if="isCollapsed" src="@/assets/icons/arrows/keyboard_arrow_down.svg">
       <img alt="arrow up" v-else src="@/assets/icons/arrows/keyboard_arrow_up.svg">
     </div>
   </div>
   <div class="collapse-container" v-if="!isCollapsed">
     <slot></slot>
   </div>
 </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

const props = defineProps<{
  label: string,
  isCollapsed: boolean
}>();

const isCollapsed = ref(props.isCollapsed);

function toggleCollapse() {
  isCollapsed.value = !isCollapsed.value;
}
</script>

<style scoped>
.collapse-wrapper {
  width: 100%;
  font-size: 16px;
}

.open-close-container {
  background: var(--secondary-color);
  border-radius: var(--border-radius-medium);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 10px;
}
</style>