<template>
 <div class="collapse-wrapper">
   <div class="open-close-container drop-shadow" @click="toggleCollapse">
     <div class="label">{{ label }}</div>
     <div class="icon">
       <img :alt="$translate('altTexts.arrowDown')" :style="{transform: `rotate(${isCollapsed ? -90 : 0}deg)`}" src="@/assets/icons/arrows/keyboard_arrow_down.svg">
     </div>
   </div>
   <div class="collapse-container" v-show="!isCollapsed">
     <slot></slot>
   </div>
   <div class="end-indicator" v-show="!isCollapsed"></div>
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
  font-size: var(--font-size-medium);
  margin-bottom: 20px;
}

.open-close-container {
  background: rgb(var(--secondary-color));
  border-radius: var(--border-radius-medium);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 10px;
}

.icon img {
  transition: transform 0.2s ease;
}

.collapse-container {
  margin: auto;
  width: calc(100% - 40px);
  padding-top: 10px;
}

.end-indicator {
  border-radius: 0 0 var(--border-radius-big) var(--border-radius-medium);
  height: 20px;
  margin-top: 20px;
  border: rgb(var(--secondary-color)) 3px dashed;
  border-top: none;
}
</style>