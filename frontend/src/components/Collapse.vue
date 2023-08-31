<template>
 <div class="collapse-wrapper">
   <div class="open-close-container" @click="toggleCollapse">
     <div class="label">{{ label }}</div>
     <div class="icon">
       <img alt="arrow down" :style="{transform: `rotate(${isCollapsed ? 180 : 0}deg)`}" src="@/assets/icons/arrows/keyboard_arrow_up.svg">
     </div>
   </div>
   <div class="collapse-container" v-show="!isCollapsed">
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
  margin-bottom: 20px;
}

.open-close-container {
  background: var(--secondary-color);
  border-radius: var(--border-radius-medium);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 10px;
}

.collapse-container {
  margin: auto;
  width: calc(100% - 40px);
  padding-top: 10px;
}

.icon img {
  transition: transform 0.2s ease;
}
</style>