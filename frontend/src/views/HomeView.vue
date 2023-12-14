<template>
  <main :class="{'show-mode-switcher': hasAdvancedPermissions}">
    <div v-if="hasAdvancedPermissions" class="mode-switcher">
      <router-link to="/home/basic" class="link">{{ $translate('modeSwitcher.basic') }}</router-link>
      <router-link to="/home/advanced" class="link advanced">{{ $translate('modeSwitcher.advanced') }}</router-link>
    </div>
    <div class="add-song-container">
      <div class="mobile">
        <OverlayCollapse :label="$translate('addSong')"
                         :is-collapsed="true"
        >
          <template #custom-icon>
            <img src="@/assets/icons/music/playlist_add.svg" :alt="$translate('altTexts.playlistAdd')">
          </template>
          <template #content>
            <div class="add-song-slot">
              <AddSong/>
            </div>
          </template>
        </OverlayCollapse>
      </div>
      <div class="add-song desktop">
        <AddSong/>
      </div>
    </div>
    <div class="queue-scroll-container">
      <div class="queue-header desktop" :class="{'drag-enabled': hasQueueReorderPermission}">
        <div class="queue-number">#</div>
        <div class="title">{{ $translate('queueDescription.title') }}</div>
        <div class="duration">{{ $translate('queueDescription.duration') }}</div>
      </div>
      <div class="hr desktop"></div>
      <div class="queue-scroll-component">
        <QueueScroll :update-interval="4000" :has-reorder="hasQueueReorderPermission"/>
      </div>
    </div>
    <div class="control-panel-wrapper">
      <div class="control-panel-container">
        <div class="now-playing-container">
          <NowPlaying :update-interval="1000"/>
        </div>
        <div class="vote-skip-container">
          <ControlPanel :vote-skip="!hasAdvancedControlPanelPermission"
                        :start-stop="hasAdvancedControlPanelPermission"
                        :skip="hasAdvancedControlPanelPermission"
                        :replay="hasAdvancedControlPanelPermission">
          </ControlPanel>
        </div>
      </div>
    </div>
    <GridBackground v-if="displayAdvanced"/>
  </main>
</template>

<script setup lang="ts">
import QueueScroll from "@/components/queue/QueueScroll.vue";
import NowPlaying from "@/components/NowPlaying.vue";
import AddSong from "@/components/control/AddSong.vue";
import OverlayCollapse from "@/components/collapse/OverlayCollapse.vue";
import router from "@/router";
import ControlPanel from "@/components/control/ControlPanel.vue";
import {computed, onMounted, ref} from "vue";
import GridBackground from "@/components/background/GridBackground.vue";


const hasAdvancedPermissions = ref(false);
const hasQueueReorderPermission = computed(() => {
  return router.currentRoute.value.name === "advanced";
});

const hasAdvancedControlPanelPermission = computed(() => {
  return router.currentRoute.value.name === "advanced";
})

const displayAdvanced = computed(() => {
  return router.currentRoute.value.name === "advanced";
})

onMounted(() => {
  if (router.currentRoute.value.name !== "default") {
    hasAdvancedPermissions.value = true;
  }
})
</script>

<style scoped>
.desktop {
  display: none;
}

main {
  width: calc(100% - 30px);
  height: calc(100svh - 60px);
  margin: auto;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-sizing: border-box;
  padding-top: 20px;
}

main.show-mode-switcher {
  .mode-switcher {
    display: flex;
    justify-content: flex-start;
    align-items: flex-start;
    gap: 15px;
    height: 50px;
  }

  .link {
    height: 35px;
    width: 100%;
    background: var(--background-color);
    border: 3px solid var(--secondary-color);
    color: var(--text-color);
    font-size: var(--font-size-medium);
    border-radius: var(--border-radius-medium);
    display: flex;
    justify-content: center;
    align-items: center;
    text-decoration: none;
  }

  .link.advanced {
    background-image: url("@/assets/background/grid_background_small.png");
    background-position: center;
  }

  .link.router-link-exact-active {
    color: var(--background-color);
    border-color: var(--tertiary-color);
    background: var(--tertiary-color);
    font-weight: bold;
  }

  .queue-scroll-container {
    height: calc(100% - 70px - 190px - 50px);
  }
}

.add-song-slot {
  width: 100%;
  height: 100%;
}

.queue-scroll-container {
  border-bottom: var(--secondary-color) dashed 2px;
  height: calc(100% - 70px - 190px);
}

.queue-scroll-component {
  height: 100%;
}

.control-panel-wrapper {
  width: 100%;
  bottom: 0;
  left: 0;
}

.control-panel-container {
  margin: auto;
  background: var(--background-color);
  height: 190px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-sizing: border-box;
}

.vote-skip-container {
  margin: 10px 0;
  gap: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

@media screen and (min-width: 800px) {
  main {
    width: 750px;
  }
}

@media screen and (min-width: 1250px) {
  .mobile {
    display: none;
  }

  .desktop {
    display: block;
  }

  main {
    width: 1250px;
    display: grid;
    grid-template-columns: 66% 33%;
    grid-template-rows: calc(100% - 230px) 230px;
  }

  main.show-mode-switcher {
    height: calc(100svh - 60px - 50px);
    grid-template-rows: 50px calc(100% - 230px) 230px;

    .mode-switcher {
      grid-row: 1;
      width: 50%;
    }

    .add-song-container {
      grid-row: 2;
    }

    .queue-scroll-container {
      grid-row: 2;
      height: 100%;
    }

    .control-panel-wrapper {
      grid-row: 3;
    }
  }

  .add-song-container {
    grid-column: 2;
    grid-row: 1;
    height: 100%;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
  }

  .add-song.desktop {
    height: 100%;
    padding: 10px;
    box-sizing: border-box;
  }

  .queue-scroll-container {
    grid-column: 1;
    grid-row: 1;
    height: 100%;
    border-bottom: none;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
    display: flex;
    flex-direction: column;
  }

  .hr {
    margin: auto;
    width: calc(100% - 20px);
    border-bottom: var(--tertiary-color) 2px solid;
    height: 2px;
  }

  .queue-header {
    padding: 20px 20px 0 27px;
    display: flex;
    gap: 10px;
    justify-content: space-between;
    align-items: center;
    height: 40px;

    .queue-number {
      width: 28px;
    }

    .title {
      width: 90%;
    }
  }

  .queue-header.drag-enabled {
    .duration {
      margin-right: 70px;
    }
  }

  .queue-scroll-component {
    height: calc(100% - 50px);
    padding: 10px 10px 10px 0;
    box-sizing: border-box;
  }

  .control-panel-wrapper {
    grid-column: 1 / span 2;
    grid-row: 2;
    height: 100%;
    display: flex;
    align-items: center;
  }

  .control-panel-container {
    width: 100%;
    border: var(--secondary-color) 3px solid;
    border-radius: var(--border-radius-big);
    display: grid;
    place-items: center;
    grid-template-columns: 66% 33%;
  }

  .now-playing-container {
    width: 100%;
    margin-bottom: 20px;
    grid-row: 1;
    grid-column: 1;
    padding: 20px;
    box-sizing: border-box;
  }

  .vote-skip-container {
    width: 100%;
    border-left: var(--secondary-color) 3px solid;
    height: 85%;
    box-sizing: border-box;
    grid-row: 1;
    grid-column: 2;
    margin: 0;
  }

}
</style>