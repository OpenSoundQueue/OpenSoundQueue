<template>
  <div class="sorting-container" @click="updateSorting()">
    <p>{{ props.label }}</p>
    <div class="arrow-container">
      <img v-show="sortingOptions[sortingStatus] !== 'desc'" class="arrow"
           src="@/assets/icons/arrows/keyboard_arrow_up.svg" :alt="$translate('altTexts.sortingArrowUp')"/>
      <img v-show="sortingOptions[sortingStatus] !== 'asc'" class="arrow"
           src="@/assets/icons/arrows/keyboard_arrow_down.svg" :alt="$translate('altTexts.sortingArrowDown')"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

const sortingOptions = ["none", "desc", "asc"];
const sortingStatus = ref(0);

const props = defineProps<{
  label: string,
}>();

const emit = defineEmits<{
  "update:sortingStatus": [data: string]
}>();

function updateSorting(): void {
  sortingStatus.value = (sortingStatus.value + 1) % 3;
  emit("update:sortingStatus", sortingOptions[sortingStatus.value]);
}
</script>

<style scoped>
.sorting-container {
  display: flex;
  flex-direction: row;
  gap: 5px;
  font-size: var(--font-size-medium);
  align-items: center;
  user-select: none;
}

.sorting-container:hover {
  cursor: pointer;
}

.arrow-container {
  display: flex;
  flex-direction: column;
  height: var(--font-size-medium);
  justify-content: center;
}

p {
  margin: 0;
  padding: 0;
}

.arrow {
  height: calc(var(--font-size-medium) / 1.65);
  aspect-ratio: 1/1;
}
</style>