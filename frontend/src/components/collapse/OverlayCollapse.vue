<template>
  <div class="collapse-wrapper" v-closable="{excluded: [], handler: collapse}">
    <div @click="toggleCollapse"
         class="open-close-container drop-shadow unselectable"
         :class="[isCollapsed ? 'closed' : 'opened']">
      <div class="custom-icon">
        <slot name="custom-icon"></slot>
      </div>
      <div class="label">{{ label }}</div>
      <div class="open-close-icon">
        <img :alt="$translate('altTexts.arrowUp')" :style="{transform: `rotate(${isCollapsed ? 90 : 180}deg)`}"
             src="@/assets/icons/arrows/keyboard_arrow_up.svg">
      </div>
    </div>
    <div v-show="!isCollapsed" class="collapse-container drop-shadow">
      <div class="slot-wrapper">
        <slot name="content"></slot>
      </div>
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

function collapse() {
  isCollapsed.value = true;
}

function toggleCollapse() {
  isCollapsed.value = !isCollapsed.value;
}

</script>

<style scoped>

.collapse-wrapper {
  width: 100%;
  font-size: var(--font-size-medium);
}

.open-close-container {
  background: rgb(var(--secondary-color));
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 10px 10px;
  gap: 10px;
}

.open-close-container:hover {
  cursor: pointer;
}

.open-close-container.closed {
  border-radius: var(--border-radius-medium);
}

.open-close-container.opened {
  border-radius: var(--border-radius-medium) var(--border-radius-medium) 0 0;
}

.open-close-icon {
  margin-left: auto;
}

.open-close-icon img {
  transition: transform 0.2s ease;
}

.collapse-container {
  position: relative;
  height: 400px;
  margin-bottom: -400px;
  width: 100%;
  z-index: 2;
  background: rgb(var(--secondary-color));
  border-radius: 0 0 var(--border-radius-medium) var(--border-radius-medium);
}

.slot-wrapper {
  height: 100%;
  display: flex;
  justify-content: center;
  padding: 10px;
  box-sizing: border-box;
}
</style>