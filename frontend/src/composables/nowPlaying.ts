import {onMounted, onUnmounted, ref} from "vue";
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

    let updateIntervalId: number | undefined;
    let renderingIntervalId: number | undefined;

    onMounted(() => {
        updateIntervalId = setInterval(getTime, updateInterval);
        renderingIntervalId = setInterval(calculateProgress, renderingInterval);
        getTime();
    });

    onUnmounted(() => {
        clearInterval(updateIntervalId);
        clearInterval(renderingIntervalId);
    });

    function getTime() {
        console.log("helolo");
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

    return {currentSong, currentTime, progress, isPlaying}
}