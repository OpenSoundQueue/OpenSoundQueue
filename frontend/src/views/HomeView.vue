<template>
  <main>
    <Collapse label="In Queue">
      <Queue></Queue>
    </Collapse>
  </main>
</template>

<script>
import {defineComponent} from "vue";
import Queue from "@/components/queue/Queue.vue";
import Collapse from "@/components/Collapse.vue";

export default defineComponent({
  components: {Collapse, Queue}
})
</script>

<style></style>
