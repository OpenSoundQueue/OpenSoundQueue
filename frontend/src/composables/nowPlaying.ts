import {onMounted, onUnmounted, ref, watch} from "vue";
import {Song} from "@/models/Song";
import type {Ref} from "vue";
import {HttpService} from "@/services/HttpService";

export function useNowPlaying(updateInterval: number, renderingInterval: number) {
    const httpService = new HttpService();

    const currentSong: Ref<Song | undefined> = ref();
    const progress = ref(0);
    const currentTime = ref(0);
    const isPlaying = ref(false);
    const playHead = ref(0);
    const songEndTime = ref(0);
    const songHasEnded = ref(false);

    let updateIntervalTimer: ReturnType<typeof setInterval> | undefined;
    let renderingIntervalTimer: ReturnType<typeof setInterval> | undefined;

    onMounted(() => {
        updateIntervalTimer = setInterval(getTime, updateInterval);
        renderingIntervalTimer = setInterval(calculateProgress, renderingInterval);
        getTime();
    });

    onUnmounted(() => {
        clearInterval(updateIntervalTimer);
        clearInterval(renderingIntervalTimer);
    });

    watch(currentSong, () => {
        songHasEnded.value = false;
    })

    function getTime() {
        httpService.getNowPlaying().then(data => {
            console.log(data);
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
            // getTime() request only fired once
            if (songHasEnded.value) {
                return;
            }

            getTime();

            songHasEnded.value = true;
        }

        // song is playing
        progress.value = (1 - ((songEndTime.value - Date.now()) / 1000) / currentSong.value?.duration) * 100;
        currentTime.value = currentSong.value.duration - (songEndTime.value - Date.now()) / 1000;
    }

    function addTransmissionTime(value: number, stampSender: number) {
        return value + Date.now() - stampSender;
    }

    return {currentSong, currentTime, progress, isPlaying}
}