<template>
  <main>
    <ExpandCollapse :label="$translate('nowPlaying')" :is-collapsed="false">
      <NowPlaying :current-song="currentSong" :current-time="currentTime" :progress="progress"/>
    </ExpandCollapse>
    <ExpandCollapse :label="$translate('inQueue')" :is-collapsed="false">
      <Queue :page-update-interval="4000"/>
    </ExpandCollapse>
    <div class="login-section">
      <p>{{ $translate("loginCTA") }}</p>
      <DynamicButton class="button" @click="router.push('/login')" b-style="login" status="active">{{ $translate("login")}}</DynamicButton>
    </div>
  </main>
  <Footer></Footer>
</template>

<script setup lang="ts">
import Queue from "@/components/queue/QueuePaged.vue";
import ExpandCollapse from "@/components/collapse/ExpandCollapse.vue";
import NowPlaying from "@/components/NowPlaying.vue";
import Footer from "@/components/Footer.vue";
import DynamicButton from "@/components/buttons/DynamicButton.vue";
import router from "@/router";
import {onMounted, ref} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";

const httpService = new HttpService();
const updateInterval = 7000;
const renderingInterval = 100;

const currentSong: Ref<Song | undefined> = ref();
const progress = ref(0);
const currentTime = ref(0);
const isPlaying = ref(false);
const playHead = ref(0);
const songEndTime = ref(0);

onMounted(() => {
  setInterval(getTime, updateInterval);
  setInterval(calculateProgress, renderingInterval);
  getTime();
})

function getTime() {
  httpService.getNowPlaying().then(data => {
    if (data.song) {
      currentSong.value = data.song;

      isPlaying.value = data.isPlaying;

      playHead.value = addTransmissionTime(data.time, data.stamp);

      songEndTime.value = Date.now() + (data.song.duration * 1000) - addTransmissionTime(data.time, data.stamp);
    }
  })
}

function calculateProgress() {
  if (!currentSong.value) {
    return;
  }

  if (!isPlaying.value) {
    // match progress and time label with time from now playing request
    currentTime.value = playHead.value / 1000;
    progress.value = (playHead.value / (currentSong.value?.duration * 1000)) * 100;
    return;
  }

  // song has ended
  if (songEndTime.value - Date.now() < 0) {
    getTime();
  }

  // song is playing
  progress.value = (1 - ((songEndTime.value - Date.now()) / 1000) / currentSong.value?.duration) * 100;
  currentTime.value = currentSong.value.duration - (songEndTime.value - Date.now()) / 1000;
}

function addTransmissionTime(value: number, stampSender: number) {
  return value + Date.now() - stampSender;
}
</script>

<style scoped>
main {
  padding-top: 20px;
  width: calc(100% - 30px);
  margin: auto;
}

.login-section{
  padding: var(--font-size-big) 0 calc(var(--font-size-big) * 2) 0;
}

.login-section>p{
  font-size: var(--font-size-big);
  text-align: center;
  font-family: Comfortaa,sans-serif;
}

.button{
  width: 50%;
  font-size: var(--font-size-medium);
  padding: 0 20px 0 20px;
  margin: 0 auto 0 auto;
}

@media screen and (min-width: 800px) {
  main {
    width: 750px;
  }
}
</style>
