<template>
  <div class="wrapper" v-closable="{excluded: [], handler: collapse}">
    <img src="@/assets/icons/help.svg" ref="help" @click="toggleCollapse">
    <div v-show="!isCollapsed" class="info-container" ref="info">
      <p>
        <slot></slot>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";

type Dimensions = {
  width: number,
  height: number
}
type EdgeDistances = {
  left: number,
  right: number,
  top: number,
  bottom: number
}

const isCollapsed = ref(false);
const help = ref(null);
const info = ref(null);
let space: EdgeDistances = {left: 0, right: 0, top: 0, bottom: 0};
let icon: EdgeDistances = {left: 0, right: 0, top: 0, bottom: 0};
let infoDimensions: Dimensions = {width: 0, height: 0};

function collapse() {
  isCollapsed.value = true;
}

onMounted(() => {
  if (info.value) {
    const infoElement = info.value as HTMLElement;

    updateValues()

    infoDimensions = {
      width: parseInt(infoElement.getBoundingClientRect().width.toFixed()),
      height: parseInt(infoElement.getBoundingClientRect().height.toFixed()),
    }
  }
  isCollapsed.value = true;
})

function toggleCollapse() {
  isCollapsed.value = !isCollapsed.value;
  updateValues()
  updatePositioning()
}

function updatePositioning() {
  if (info.value) {
    const infoElement = info.value as HTMLElement;

    if (space.left > infoDimensions.width / 2 && space.right > infoDimensions.width / 2) {
      infoElement.style.left = "-" + (infoDimensions.width / 2 - (icon.right - icon.left) / 2) + "px"
    } else if (space.left > space.right) {
      infoElement.style.left = "-" + (infoDimensions.width - (icon.right - icon.left)) + "px";
    } else {
      infoElement.style.left = "0";
    }

    if (space.top > infoDimensions.height) {
      infoElement.style.bottom = "35px"
    } else {
      infoElement.style.bottom = "-" + (infoDimensions.height + 5) + "px"
    }

  }
}

function updateValues() {
  if (help.value && info.value) {
    const helpElement = help.value as HTMLElement;
    icon = {
      left: parseInt(helpElement.getBoundingClientRect().left.toFixed()),
      right: parseInt(helpElement.getBoundingClientRect().right.toFixed()),
      top: parseInt(helpElement.getBoundingClientRect().top.toFixed()),
      bottom: parseInt(helpElement.getBoundingClientRect().bottom.toFixed())
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
  height: 24px;
  width: 24px;
}

.wrapper:hover {
  cursor: pointer;
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
  line-height: 1.4;
}

</style>