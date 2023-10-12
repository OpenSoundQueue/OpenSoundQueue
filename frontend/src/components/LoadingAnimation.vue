<template>
  <div class="loading-container">
    <div v-for="(dot,index) in dotCount" class="dot" :class="animationStatus(index)"></div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import type {Ref} from "vue";

const dots: Ref<Array<boolean>> = ref([])
const props = defineProps<{
  dotCount: number,
  duration: number
}>()

onMounted(() => {
  dots.value = new Array(props.dotCount).fill(false);

  const position = ref(0)
  let intervalDuration: number = props.duration / props.dotCount;
  intervalDuration *= 1 / 2;

  setInterval( () => {
    if (position.value > props.dotCount -1 && position.value < props.dotCount*2 -1 ) {
      position.value++;
      position.value %= props.dotCount*2;
    }else{
    dots.value[position.value] = true;
    position.value++;
    position.value %= props.dotCount*2;
    dots.value[position.value] = false;
    }
  }, intervalDuration)
})

function animationStatus(index: number): string {
  return dots.value[index] ? "jump" : "";
}
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  margin-top: 100px;
}

.dot {
  aspect-ratio: 1/1;
  height: var(--font-size-small);
  background-color: var(--light-gray);
  border-radius: 9999px;
}

@keyframes jumpAnimation {
  0% {
    transform: translateY(0);
  }
  25% {
    transform: translateY(calc(var(--font-size-small) * -1));
  }
  50% {
    transform: translateY(calc(var(--font-size-small) * -1.5));
  }
  75% {
    transform: translateY(calc(var(--font-size-small) * -1));
  }
  100% {
    transform: translateY(0);
  }
}

.jump {
  animation: jumpAnimation 0.375s ease;
}
</style>