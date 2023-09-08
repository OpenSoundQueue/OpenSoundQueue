<template>
  <main>
    <div @click="activate" class="wrapper">
      <div class="slider" :class="[isActive ? 'active' : 'inactive']">
        <div class="button-label-container">
          <div class="button unselectable" :class="[isActive ? 'active' : 'inactive']">
            <img src="@/assets/icons/music/skip.svg" alt="skip icon">
          </div>
          <div class="label unselectable" :class="[isActive ? 'active' : 'inactive']">4 / 5</div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import {ref} from "vue";

const isActive = ref(false);

function activate() {
  console.log(isActive.value);
  isActive.value = !isActive.value;
  console.log(isActive.value);
}
</script>

<style scoped>
.wrapper {
  height: 70px;
  width: 100px;
  display: flex;
  align-items: center;
}

.slider {
  width: 100%;
  height: 30px;
  border-radius: 15px;
  display: flex;
  align-items: center;
}

.slider.inactive {
  background: var(--secondary-color);
}

.slider.active {
  background: var(--tertiary-color);
}

.button-label-container {
  width: 100%;
  display: flex;
  align-items: center;
}

.button {
  z-index: 1;
  position: relative;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--primary-color);
  display: flex;
  justify-content: center;
  align-items: center;
  transition-property: left;
  transition-duration: .2s;
}

.button img {
  height: 15px;
}

.button.active {
  left: calc(100% - 40px);
}

.button.inactive {
  left: 0;
}

.label {
  width: calc(100% - 50px);
  text-align: center;
  transition-property: transform;
  transition-duration: .2s;
}

.label.active {
  transform: translateX(-30px);
  color: var(--background-color);
  font-weight: bold;
}

</style>