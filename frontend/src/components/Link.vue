<template>
  <div class="link-wrapper">
    <div v-if="isExternal" class="external-container">
      <a :href="path" target="_blank" class="link">{{ label}}</a>
      <img class="icon-external" src="@/assets/icons/arrows/arrow_outward.svg"/>
    </div>
    <div v-else>
      <router-link :to="path" class="link" :class="{'outer-area': isOuterArea}">
        {{ label }}
        <div class="underline"></div>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  isExternal?: boolean,
  isOuterArea?: boolean,
  path: string,
  label: string
}>();
</script>


<style scoped>
.link-wrapper {
  width: fit-content;
}

.link {
  text-decoration: none;
  color: var(--text-color);
  font-size: var(--font-size-nav);
}

.router-link-active {
  font-weight: bold;

  .underline {
    background: var(--primary-color);
    margin-top: 4px;
    height: 3px;
    width: 100%;
  }
}

.external-container {
  display: flex;
  align-items: center;
}

.outer-area {
  color: var(--tertiary-color);
}

.icon-external {
  height: 100%;
}
</style>