<template>
  <div class="translation-container">
    <div class="dropdown-header" @click="toggleMenu()">
      <img class="menu-icon" :src="currentLangPath">
      <img class="dropdown-arrow" :class="rotationClass" src="/translations/dropdown.svg">
    </div>
    <div class="dropdown-content" v-show="contentVisibility">
      <template v-for="(lang, index) in languages">
        <img v-if="index !== currentLangIndex" class="menu-icon" :src="lang.imgPath" @click="selectLanguage(index)">
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {getCurrentLanguage, setLanguage} from '@/plugins/TranslationPlugin';

type Language = {
  name: string,
  imgPath: string
}

const rotationClass = ref("")
let currentLangIndex: number = 0;
const contentVisibility = ref(false);
const languages: Array<Language> = [
  {
    name: "en",
    imgPath: "/translations/en.png"
  },
  {
    name: "de",
    imgPath: "/translations/de.png"
  }
];

onMounted(()=>{
  const storeLang: string | null = sessionStorage.getItem("lang");


  if (storeLang == null) {
    sessionStorage.setItem("lang", "en");
  }else if(languages.map(x => x.name).includes(storeLang)){
    setLanguage(storeLang)
    currentLangIndex = languages.findIndex(x => x.name == storeLang);
    currentLangPath.value = languages[currentLangIndex].imgPath;
  }
})

languages.forEach((x, index) => {
  if (x.name == getCurrentLanguage().value){
    currentLangIndex = index
  }
})

const currentLangPath = ref(languages[currentLangIndex].imgPath);

function toggleMenu(): void {
  contentVisibility.value = !contentVisibility.value;
  rotationClass.value = rotationClass.value == "rotate" ? "" : "rotate";
}

function selectLanguage(index: number): void {
  currentLangPath.value = languages[index].imgPath;
  currentLangIndex = index;
  setLanguage(languages[currentLangIndex].name);
  sessionStorage.setItem("lang", languages[currentLangIndex].name);
  toggleMenu();
}
</script>

<style scoped>
.translation-container {
  z-index: 999;
}

.translation-container:hover {
  cursor: pointer;
}

.dropdown-header {
  background-color: white;
  display: flex;
  flex-direction: row;
  align-items: center;
  width: min-content;
  padding: 4px 0 4px 4px;
}

.dropdown-content {
  background-color: var(--light-gray);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  position: absolute;
  width: min-content;
  padding: 4px;
  transition: all 0.25s ease;
}

.menu-icon {
  aspect-ratio: 2/1;
  height: 32px;
}

.dropdown-arrow {
  height: 32px;
  transition: all 0.25s ease;
}

.rotate {
  transform: rotate(180deg);
}
</style>