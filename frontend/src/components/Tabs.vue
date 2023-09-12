<template>
  <div class="tab-switcher-wrapper">
    <div class="tabs-container">
      <div v-for="(tab, index) in tabs"
           :key="index"
           @click="setActiveTab(index)"
           class="tab unselectable"
           :class="[index === activeTab ? 'active' : 'inactive']">
        {{ tab }}
      </div>
      <div class="spacer"></div>
    </div>
    <div class="content-wrapper">
      <div v-for="(tab, index) in tabs"
           :key="index"
           class="tab-wrapper"
           v-show="index === activeTab">
        <slot :name="`tab-${index}`"></slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

const props = defineProps<{
  tabs: string[]
}>()

const activeTab = ref(0);

function setActiveTab(index: number) {
  if (index < 0 || index > props.tabs.length - 1) {
    return;
  }

  activeTab.value = index;
}
</script>


<style scoped>

.tab-switcher-wrapper {
  width: calc(100% - 30px);
  height: 100px;
}

.tabs-container {
  display: flex;
  justify-content: space-around;
  width: 100%;
  gap: 10px;
}

.tab {
  width: 100%;
  height: 40px;
  border-radius: var(--border-radius-medium) var(--border-radius-medium) 0 0;
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  cursor: pointer;
  border: var(--tertiary-color) 2px solid;
  border-bottom: none;
}

.tab.active {
  background: var(--tertiary-color);
  color: var(--background-color);
}

.tab.inactive {
 background: none;
  color: var(--text-color);
}

.spacer {
  display: none;
}

.content-wrapper {
  border: var(--tertiary-color) 5px solid;
  border-radius: 0 0 var(--border-radius-medium) var(--border-radius-medium);
  height: 100%;
}

.tab-wrapper.active {
  display: block;
}

.tab-wrapper.inactive {
  display: none;
}

@media screen and (min-width: 420px) {
  .tab {
    width: 160px;
  }

  .spacer {
    display: block;
    width: calc(100% - 320px);
  }

  .content-wrapper {
    border-radius: 0 var(--border-radius-medium) var(--border-radius-medium) var(--border-radius-medium);
  }
}

</style>