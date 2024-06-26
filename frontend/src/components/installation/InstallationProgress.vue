<template>
  <div class="progress-container">
    <div class="progress"/>
    <div v-for="(step,index) of installationOrder" class="installation-step" :key="index"
         :class="[currentlyActive(step) ? 'active' : '', alreadyFinished(step) ? 'finished' : '']"
         @click="goToStep(step)">
      <div class="circle">
        <div class="number">{{ index + 1 }}</div>
        <img class="check" src="@/assets/icons/check.svg" :alt="$translate('altTexts.check')"/>
      </div>
      <div class="name">{{ $translate(`installation.progressBar.${step.toString()}`) }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import router from "@/router";
import type {RouteRecordName} from "vue-router";

//these are the path names of the corresponding routes
const props = defineProps<{
  installationOrder: RouteRecordName[]
}>();

function currentlyActive(pathName: RouteRecordName) {
  const routeName = router.currentRoute.value.name;

  return (routeName === <string>pathName);
}

function alreadyFinished(pathName: RouteRecordName) {
  const routeName = router.currentRoute.value.name;

  return props.installationOrder.indexOf(<string>routeName) > props.installationOrder.indexOf(<string>pathName);
}

function goToStep(pathName: RouteRecordName) {
  if (alreadyFinished(<string>pathName))
    router.push({name: pathName})
}
</script>

<style scoped>
.progress-container {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: space-between;

  width: 100%;
}

.progress {
  position: absolute;
  top: 19px;
  left: 40px;

  width: calc(100% - 80px);
  height: 3px;
  background-color: rgb(var(--tertiary-color));
}

.installation-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;

  z-index: 2;
  width: 80px;
  color: rgb(var(--tertiary-color));
}

.circle {
  display: flex;
  justify-content: center;
  align-items: center;
  user-select: none;

  aspect-ratio: 1;
  border-radius: 21px;
  height: 40px;
  background-color: rgb(var(--secondary-color));
  border: solid 1px rgb(var(--tertiary-color));
  box-shadow: 0 0 0 4px rgb(var(--background-color));
}

.installation-step:hover {
  cursor: pointer;
}

.check {
  display: none;
}

.active {
  font-weight: bold;
  color: rgb(var(--text-color));
  margin-top: -4px;

  .circle {
    border: 3px solid rgb(var(--secondary-color));
    background-color: rgb(var(--primary-color));
    border-radius: 100px;
    box-shadow: 0 0 0 2px rgb(var(--primary-color));
  }
}

.finished {

  .circle {
    background-color: rgb(var(--primary-color));
    border-color: rgb(var(--primary-color));

    .check {
      display: initial;
    }

    .number {
      display: none;
    }
  }
}

@media screen and (max-width: 600px) {
  .circle {
    box-shadow: 0 0 0 4px rgb(var(--background-color));
    height:30px;
  }

  .progress{
    top: 14px;
    height: 1px;
  }

  .active>.circle{
    border-color: rgb(var(--background-color));
  }
}
</style>