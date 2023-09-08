<template>
<div class="wrapper">
  <div class="button"></div>
  <div class="slider"></div>
  <div class="label">4/5</div>
</div>
</template>

<script setup lang="ts">

</script>

<style scoped>
.wrapper {
  height: 70px;
}

.button {
  position: relative;
  z-index: 1;
  top: 25px;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: blue;
}

.slider {
  position: relative;
  width: 100px;
  height: 20px;
  background: red;
  bottom: calc(35px - 10px);
}
</style>