<template>
  <div class="toast-wrapper">
    <div class="message-wrapper" :class="[messageLevel, isVisible ? 'visible' : 'invisible']">
      <div class="message-nav">
        <img class="message-level-icon" v-if="messageLevel === 'information'" src="@/assets/icons/info.svg"/>
        <img class="message-level-icon" v-else-if="messageLevel === 'success'" src="@/assets/icons/check.svg"/>
        <img class="message-level-icon" v-else-if="messageLevel === 'error'" src="@/assets/icons/error.svg"/>
        <img class="message-level-icon" v-else src="@/assets/icons/info.svg"/>
        <img @click="close" class="close" src="@/assets/icons/input/delete.svg"/>
      </div>
      <div class="message">{{ message }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  messageLevel: string
  message: string
  isVisible: boolean
}>();

const emit = defineEmits<{
  close: []
}>()

function close() {
  emit("close");
}
</script>


<style scoped>

.message-wrapper {
  width: 220px;
  max-height: 120px;
  border-radius: var(--border-radius-medium);
  border: 1px solid var(--text-color);
  position: fixed;
  top: 10px;
  right: 10px;
  z-index: 3;
  padding: 5px;
  transition: transform 0.2s ease;
  display: flex;
  flex-direction: column;
}

.message-nav {
  display: flex;
  justify-content: space-between;
}

.message-nav .close {
  border: 1px solid var(--text-color);
  border-radius: var(--border-radius-small);
}

.message-nav .close {
  cursor: pointer;
}

.message {
  padding: 10px 0;
  display: flex;
  height: 100%;
  justify-content: center;
  align-items: center;
  text-align: center;
  font-weight: bold;
}

.message-wrapper.information {
  background: var(--tertiary-color-transparent);
}

.message-wrapper.error {
  background: var(--red-tranparent);
}

.message-wrapper.success {
  background: var(--green-tranparent);
}

.message-wrapper.visible {
}

.message-wrapper.invisible {
  transform: translateX(250px);
}

</style>