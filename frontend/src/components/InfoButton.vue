<template>
  <div class="wrapper" v-closable="{excluded: [], handler: collapse}">
    <img src="@/assets/icons/help.svg" ref="help" @click="toggleCollapse">
    <div v-show="!isCollapsed" class="info-container" ref="info">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";


type Dimension = {
  width: number,
  height: number
}

type Positioning = {
  left: number,
  right: number,
  top: number,
  bottom: number
}

const isCollapsed = ref(false);
const help = ref(null);
const info = ref(null);
let space: Positioning = {left: 0, right: 0, top: 0, bottom: 0};
let icon: Positioning = {left: 0, right: 0, top: 0, bottom: 0};
let infoDimensions: Dimension = {width: 0, height: 0};

function collapse() {
  isCollapsed.value = true;
}

onMounted(() => {
  updateValues()
  infoDimensions = {
    width: parseInt(info.value.getBoundingClientRect().width.toFixed()),
    height: parseInt(info.value.getBoundingClientRect().height.toFixed()),
  }
  isCollapsed.value = true;
})

function toggleCollapse() {
  isCollapsed.value = !isCollapsed.value;
  updateValues()
  if (space.left > infoDimensions.width / 2 && space.right > infoDimensions.width / 2) {
    info.value.style.left = "-" + (infoDimensions.width / 2 - (icon.right - icon.left) / 2) + "px"
  } else if (space.left > space.right) {
    info.value.style.left = "-" + (infoDimensions.width - (icon.right - icon.left)) + "px";
  } else {
    info.value.style.left = "0";
  }

  if (space.top > infoDimensions.height) {
    info.value.style.bottom = "40px"
  } else {
    info.value.style.bottom = "-" + (infoDimensions.height + 5) + "px"
  }
}

function updateValues() {
  if (help.value && info.value) {
    icon = {
      left: parseInt(help.value.getBoundingClientRect().left.toFixed()),
      right: parseInt(help.value.getBoundingClientRect().right.toFixed()),
      top: parseInt(help.value.getBoundingClientRect().top.toFixed()),
      bottom: parseInt(help.value.getBoundingClientRect().bottom.toFixed())
    }

    space = {
      left: icon.left,
      right: window.innerWidth - icon.right,
      top: icon.top,
      bottom: window.innerHeight - icon.bottom
    }
  }
}
</script>

<style scoped>
.wrapper {
  position: relative;
}

img {
  cursor: pointer;
}

.info-container {
  position: absolute;
  width: 200px;
  border-radius: var(--border-radius-medium);
  padding: var(--font-size-small);
  background-color: var(--secondary-color);
  box-shadow: 0 4px 4px 0 rgba(0, 0, 0, 0.25);
  z-index: 9999;
  box-sizing: border-box;
}

.info-container > * {
  padding: 0;
  margin: 0;
}
</style>