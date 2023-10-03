<template>
  <div class="select-wrapper">
    <label class="input-field-label">{{ label }} <span v-if="required">*</span></label>
    <div class="select-container">
      <img class="drop-down-icon" src="@/assets/icons/arrows/keyboard_arrow_down.svg"/>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  label: string,
  required: boolean
}>();
</script>

<style scoped>
.select-wrapper {
  width: 100%;
}

.select-container {
  box-sizing: border-box;
  width: 100%;
  height: 40px;
  border: 2px solid white;
  border-radius: var(--border-radius-medium);
  position: relative;
  display: flex;
  align-items: center;
}

.drop-down-icon {
  right: 10px;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>