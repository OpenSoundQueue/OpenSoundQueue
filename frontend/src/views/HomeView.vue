<template>
  <main>
    <div class="wrapper">
      <div class="slider">
        <div class="button-label-container">
          <div class="button">
            <img src="@/assets/icons/music/skip.svg" alt="skip icon">
          </div>
          <div class="label">4/5</div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">

</script>

<style scoped>
.wrapper {
  height: 70px;
  width: 100%;
  display: flex;
  align-items: center;
}

.slider {
  width: 100%;
  height: 20px;
  border-radius: 10px;
  background: red;
  display: flex;
  align-items: center;
}

.button-label-container {
  width: 100%;
  display: flex;
  align-items: center;
}

.button {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: blue;
  display: flex;
  justify-content: center;
  align-items: center;
}

.label {
  width: calc(100% - 50px);
  text-align: center;
}

</style>