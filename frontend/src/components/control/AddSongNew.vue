<template>
  <div class="add-song-wrapper" @click="openOverlay" v-closable="{excluded: [], handler: closeOverlay}">
    <InputField placeholder="Suchen oder Link" v-model="addSongInput"/>
    <div class="add-song-overlay" :class="[showOverlay ? 'visible' : 'not-visible']">
      <div v-if="showSearch">
        <SearchResults :search-term="addSongInput"/>
      </div>
      <div v-else>
        <DefaultButton text="Add to queue"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {computed, ref, watch} from "vue";
import SearchResults from "@/components/control/SearchResults.vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";

const addSongInput = ref("");
const showOverlay = ref(false);

const linkRegex = /^https:\/\/.*/;

const showSearch = computed(() => {
  return !linkRegex.test(addSongInput.value);
});

watch(addSongInput, () => {
  if (!addSongInput.value) {
    showOverlay.value = false;
    return;
  }

  showOverlay.value = true;
})

function openOverlay() {
  if (!addSongInput.value) {
    return;
  }

  showOverlay.value = true;
}

function closeOverlay() {
  showOverlay.value = false;
}

</script>

<style scoped>
.add-song-wrapper {
  width: 100%;
  position: relative;
  z-index: 2;
}

.add-song-overlay {
  position: absolute;
  width: 100%;
  background-color: var(--secondary-color);
  border-radius: var(--border-radius-medium);
  padding: 10px;
  box-sizing: border-box;
}

.add-song-overlay.not-visible {
  visibility: hidden;
}

.add-song-overlay.visible {
  visibility: visible;
}
</style>