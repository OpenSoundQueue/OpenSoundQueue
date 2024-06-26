<template>
  <main :class="{'show-mode-switcher': hasAdvancedPermissions}">
    <div v-if="hasAdvancedPermissions" class="mode-switcher">
      <router-link to="/home/basic" class="link">{{ $translate('modeSwitcher.basic') }}</router-link>
      <router-link to="/home/advanced" class="link advanced">{{ $translate('modeSwitcher.advanced') }}</router-link>
    </div>
    <div class="add-song-container">
      <AddSong  v-if="addSongPermission"/>
      <div v-else class="no-add-permission">{{ translate("addSong.insufficientPermissions")}}</div>
    </div>
    <div class="queue-scroll-container">
      <div class="queue-header desktop" :class="{'drag-enabled': hasQueueReorderPermission, 'delete-enabled': hasQueueDeleteSongsPermission}">
        <div class="queue-number">#</div>
        <div class="title">{{ $translate('queueDescription.title') }}</div>
        <div class="duration">{{ $translate('queueDescription.duration') }}</div>
      </div>
      <div class="hr desktop"></div>
      <div class="queue-scroll-component">
        <QueueScroll :update-interval="4000"
                     :has-reorder="hasQueueReorderPermission && displayAdvanced"
                     :has-delete-songs="hasQueueDeleteSongsPermission && displayAdvanced"
                     @select="args => showSelectionOptions = args"
                     ref="queueScroll"
        />
      </div>
      <div v-if="showSelectionOptions && displayAdvanced" class="selection-options-container">
        <div @click="deleteSelection" class="delete-button">
          <img src="@/assets/icons/delete.svg" :alt="$translate('altTexts.delete')"/>
          {{ $translate('deleteSongs.label') }}
        </div>
      </div>
    </div>
    <div class="control-panel-wrapper">
      <div class="control-panel-container">
        <div class="now-playing-container">
          <NowPlaying :current-song="currentSong"
                      :current-time="currentTime"
                      :progress="progress"
                      :is-playing="isPlaying"
          />
        </div>
        <div class="vote-skip-container">
          <ControlPanel :vote-skip="controlPanelPermissions.voteSkip && !displayAdvanced"
                        :start-stop="controlPanelPermissions.startStop && displayAdvanced"
                        :skip="controlPanelPermissions.skip && displayAdvanced"
                        :replay="controlPanelPermissions.replay && displayAdvanced"
                        :change-volume="controlPanelPermissions.changeVolume && displayAdvanced"
                        :is-playing="isPlaying"
                        @update="update"
          />
        </div>
      </div>
    </div>
    <GridBackground v-if="displayAdvanced"/>
  </main>
</template>

<script setup lang="ts">
import QueueScroll from "@/components/queue/QueueScroll.vue";
import NowPlaying from "@/components/NowPlaying.vue";
import router from "@/router";
import ControlPanel from "@/components/control/ControlPanel.vue";
import {computed, onMounted, ref} from "vue";
import type {Ref} from "vue";
import GridBackground from "@/components/background/GridBackground.vue";
import AddSong from "@/components/control/AddSong.vue";
import {HttpService} from "@/services/HttpService";
import {useNowPlaying} from "@/composables/nowPlaying";
import {PermissionService} from "@/services/PermissionService";
import {translate} from "@/plugins/TranslationPlugin";

type ControlPanelPermissions = {
  voteSkip: boolean,
  startStop: boolean,
  skip: boolean,
  replay: boolean,
  changeVolume: boolean
}

const httpService = new HttpService();

const {currentSong, currentTime, progress, isPlaying} = useNowPlaying(4000, 100);

const hasAdvancedPermissions = ref(false);
const hasQueueReorderPermission = ref(false);
const hasQueueDeleteSongsPermission = ref(false);
const controlPanelPermissions: Ref<ControlPanelPermissions> = ref({
  voteSkip: false,
  startStop: false,
  skip: false,
  replay: false,
  changeVolume: false,
});
const addSongPermission = ref(false);

const showSelectionOptions = ref(false);

const queueScroll = ref<InstanceType<typeof QueueScroll>>();

onMounted(async () => {
  await PermissionService.getPermissions();
  hasQueueReorderPermission.value = PermissionService.checkPermission("CHANGE_ORDER");
  hasQueueDeleteSongsPermission.value = PermissionService.checkPermission("DELETE_SONGS");

  controlPanelPermissions.value.voteSkip = PermissionService.checkPermission("VOTESKIP");
  controlPanelPermissions.value.startStop = PermissionService.checkPermission("PAUSE_PLAY");
  controlPanelPermissions.value.skip = PermissionService.checkPermission("SKIP");
  controlPanelPermissions.value.replay = PermissionService.checkPermission("REPLAY");
  controlPanelPermissions.value.changeVolume = PermissionService.checkPermission("CHANGE_VOLUME");

  hasAdvancedPermissions.value = PermissionService.hasAnyPermission(["SKIP","PAUSE_PLAY","CHANGE_VOLUME","CHANGE_ORDER","DELETE_SONGS"]);
  addSongPermission.value = PermissionService.checkPermission("ADD_SONG");

  if (hasAdvancedPermissions.value && router.currentRoute.value.name === "default"){
    await router.push("/home/basic")
  }
})

const displayAdvanced = computed(() => {
  return router.currentRoute.value.name === "advanced";
})

function deleteSelection() {
  queueScroll.value?.deleteSelected();
}

function update() {
  httpService.getNowPlaying().then(data => {
    if (data.song) {
      isPlaying.value = data.isPlaying;
    }
  })
}
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
    margin-bottom: 10px;
  }

  .link {
    height: 35px;
    width: 100%;
    background: rgb(var(--background-color));
    border: 3px solid rgb(var(--secondary-color));
    color: rgb(var(--text-color));
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
    color: rgb(var(--background-color));
    border-color: rgb(var(--tertiary-color));
    background: rgb(var(--tertiary-color));
    font-weight: bold;
  }

  .queue-scroll-container {
    height: calc(100% - 100px - 190px - 50px);
  }
}

.add-song-container {
  height: 100px;
}

.no-add-permission{
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.queue-scroll-container {
  border-bottom: rgb(var(--secondary-color)) dashed 2px;
  height: calc(100% - 100px - 190px);
  padding-top: 10px;
  box-sizing: border-box;
  position: relative;
}

.queue-scroll-component {
  height: 100%;
}

.selection-options-container {
  background: rgba(0, 0, 0, 0.6);
  border-radius: var(--border-radius-big);
  position: absolute;
  width: 100%;
  height: 50px;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.selection-options-container .delete-button {
  height: 20px;
  width: fit-content;
  padding: 5px;
  border-radius: var(--border-radius-medium);
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 3px;
}

.selection-options-container .delete-button:hover {
  cursor: pointer;
  background: rgb(var(--secondary-color));
}

.control-panel-wrapper {
  width: 100%;
  bottom: 0;
  left: 0;
}

.control-panel-container {
  margin: auto;
  background: rgb(var(--background-color));
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

.now-playing-container {
  margin-bottom: 20px;
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
    background: rgb(var(--secondary-color));
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
    background: rgb(var(--secondary-color));
    display: flex;
    flex-direction: column;
  }

  .hr {
    margin: auto;
    width: calc(100% - 20px);
    border-bottom: rgb(var(--tertiary-color)) 2px solid;
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

  .queue-header.delete-enabled {
    margin-left: 50px;
  }

  .queue-scroll-component {
    height: calc(100% - 50px);
    padding: 10px 10px 0 0;
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
    border: rgb(var(--secondary-color)) 3px solid;
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
    border-left: rgb(var(--secondary-color)) 3px solid;
    height: 85%;
    box-sizing: border-box;
    grid-row: 1;
    grid-column: 2;
    margin: 0;
  }

}
</style>