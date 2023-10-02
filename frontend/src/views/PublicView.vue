<template>
  <main>
    <ExpandCollapse :label="$translate('nowPlaying')" :is-collapsed="false">
      <NowPlaying :update-interval="1000"/>
    </ExpandCollapse>
    <ExpandCollapse :label="$translate('inQueue')" :is-collapsed="false">
      <Queue :page-update-interval="4000"/>
    </ExpandCollapse>
  </main>
  <Footer></Footer>
</template>

<script setup lang="ts">
import Queue from "@/components/queue/QueuePaged.vue";
import ExpandCollapse from "@/components/collapse/ExpandCollapse.vue";
import NowPlaying from "@/components/NowPlaying.vue";
import Footer from "@/components/Footer.vue";
</script>

<style>
main {
  padding-top: 20px;
  width: calc(100% - 30px);
  margin: auto;
}

@media screen and (min-width: 800px) {
  main {
    width: 750px;
  }
}
</style>
