<template>
  <div class="progress-container">
    <div class="progress"/>
    <div v-for="(step,index) of installationOrder" class="installation-step" :key="index"
         :class="[currentlyActive(step) ? 'active' : '', alreadyFinished(step) ? 'finished' : '']"
    @click="goToStep(step)">
      <div class="circle">
        <div class="number">{{ index + 1 }}</div>
        <img class="check" src="@/assets/icons/check.svg" :alt="translate('')"/>
      </div>
      <div class="name">{{ translate('installation.progressBar.'+step) }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {translate} from "@/plugins/TranslationPlugin";
import router from "@/router";

//these are the path names of the corresponding routes
const installationOrder:string[] = ["language", "registration", "privacy", "sources"]

function currentlyActive(pathName: string) {
  const routeName = router.currentRoute.value.name;

  return (routeName === pathName);
}

function alreadyFinished(pathName: string){
  const routeName = router.currentRoute.value.name;

  return installationOrder.indexOf(routeName) > installationOrder.indexOf(pathName);
}

function goToStep(pathName:string){
  if (alreadyFinished(pathName))
    router.push({name:pathName})
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
  background-color: var(--tertiary-color);
}

.installation-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;

  z-index: 2;
  width: 80px;
  color: var(--tertiary-color);
}

.circle {
  display: flex;
  justify-content: center;
  align-items: center;
  user-select: none;

  aspect-ratio: 1;
  border-radius: 20px;
  height: 40px;
  background-color: var(--secondary-color);
  border: solid 1px var(--tertiary-color);
  box-shadow: 0 0 0 4px var(--grayish-blue);
}

.installation-step:hover{
  cursor: pointer;
}

.check {
  display: none;
}

.active {
  font-weight: bold;
  color: var(--text-color);
  margin-top: -3px;

  .circle {
    border: 3px solid var(--grayish-blue);
    background-color: var(--primary-color);
    border-radius: 100px;
    box-shadow: 0 0 0 2px var(--primary-color);
  }
}

.finished {

  .circle {
    background-color: var(--primary-color);
    border-color: var(--primary-color);

    .check {
      display: initial;
    }

    .number {
      display: none;
    }
  }
}
</style>