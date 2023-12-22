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
import {useNowPlaying} from "@/composables/nowPlaying";

const {currentSong, currentTime, progress} = useNowPlaying(7000, 100);

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
