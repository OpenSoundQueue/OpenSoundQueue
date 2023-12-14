<template>
  <div class="full-screen" @click="close($event)">
    <div class="pop-up">
      <img class="close" @click="close($event)" src="@/assets/icons/input/delete.svg" :alt="$translate('altTexts.close')"/>
      <p>{{ message }}</p>
      <DynamicButton b-style="login" status="active" @click="accept">{{ buttonText }}</DynamicButton>
    </div>
  </div>
</template>

<script setup lang="ts">
import DynamicButton from "@/components/buttons/DynamicButton.vue";

const props = defineProps<{
  message: string,
  buttonText: string
}>();

const emits = defineEmits<{
  "Update:PopUpStatus": [data: string]
}>()

function close(event: MouseEvent) {
  if (event.target === event.currentTarget) {
    emits("Update:PopUpStatus", "denied")
  }
}

function accept(): void {
  emits("Update:PopUpStatus", "accepted")
}
</script>

<style scoped>
.full-screen {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 4;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.pop-up {
  border-radius: var(--border-radius-big);
  background-color: var(--secondary-color);
  width: calc(100% - 50px);
  padding: 20px;
  box-sizing: border-box;

  display: flex;
  flex-direction: column;
}

.close {
  align-self: flex-end;
  right: 20px;
}

p {
  user-select: none;
  text-align: center;
  margin-top: 0;
  margin-bottom: 15px;
}

@media screen and (min-width: 800px) {
  .pop-up {
    width: 750px;
  }
}
</style>