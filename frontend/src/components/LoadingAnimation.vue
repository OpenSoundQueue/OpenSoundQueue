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
  duration: number,
  size: "small" | "medium" | "big"
}>()

const size: {small: string, medium: string, big: string} = {
  small: "5px",
  medium: "10px",
  big: "15px"
}

const passedSize = ref(size[props.size]);

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
  width: 100%;
}

.dot {
  aspect-ratio: 1/1;
  height: var(--font-size-small);
  background-color: rgb(var(--light-gray));
  border-radius: 9999px;
}

@keyframes jumpAnimation {
  0% {
    transform: translateY(0);
  }
  25% {
    transform: translateY(calc(v-bind("passedSize") * -1));
  }
  50% {
    transform: translateY(calc(v-bind("passedSize") * -1.5));
  }
  75% {
    transform: translateY(calc(v-bind("passedSize") * -1));
  }
  100% {
    transform: translateY(0);
  }
}

.jump {
  animation: jumpAnimation 0.375s ease;
}
</style>