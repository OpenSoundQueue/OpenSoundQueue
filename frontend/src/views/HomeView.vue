<template>
  <main>
    <Collapse :label="$translate('nowPlaying')" :is-collapsed="false">
      <NowPlaying/>
    </Collapse>
    <Collapse :label="$translate('inQueue')" :is-collapsed="false">
      <Queue :page-reload-interval="4000"/>
    </Collapse>
  </main>
</template>

<script setup lang="ts">
import Queue from "@/components/queue/Queue.vue";
import Collapse from "@/components/Collapse.vue";
import NowPlaying from "@/components/NowPlaying.vue";
</script>

<style>
main {
  padding-top: 20px;
  width: 90%;
  margin: auto;
}

@media screen and (min-width: 800px) {
  main {
    width: 750px;
  }
}
</style>
