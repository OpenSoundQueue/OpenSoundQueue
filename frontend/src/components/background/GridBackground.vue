<template>
  <teleport to="#app">
    <div class="background">
      <div class="gradient"></div>
    </div>
  </teleport>
</template>

<style scoped>

.background {
  width: 100vw;
  height: 100vh;
  z-index: -1;
  position: fixed;
  left: 0;
  top: 0;
  background-image: url("@/assets/background/grid-background_big.png");
  background-size: 300px;
}

.background .gradient {
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.00) 0%, var(--background-color) 54.69%);
  height: 100%;
  width: 100%;
}
</style>