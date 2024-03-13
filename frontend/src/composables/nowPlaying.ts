import { onMounted, onUnmounted, ref, watch } from "vue";
import { Song } from "@/models/Song";
import type { Ref } from "vue";
import { HttpService } from "@/services/HttpService";

export function useNowPlaying(updateInterval: number, renderingInterval: number) {
    const httpService = new HttpService(); // Instantiating HttpService for making API requests

    // Declaring reactive references for song-related state
    const currentSong: Ref<Song | undefined> = ref();
    const progress = ref(0);
    const currentTime = ref(0);
    const isPlaying = ref(true);
    const playHead = ref(0);
    const songEndTime = ref(0);
    const songHasEnded = ref(false);

    let updateIntervalTimer: ReturnType<typeof setInterval> | undefined;
    let renderingIntervalTimer: ReturnType<typeof setInterval> | undefined;

    // Vue lifecycle hook - executed when the component is mounted
    onMounted(() => {
        // Setting up intervals for updating song information and rendering progress
        updateIntervalTimer = setInterval(getTime, updateInterval);
        renderingIntervalTimer = setInterval(calculateProgress, renderingInterval);
        getTime(); // Initial call to fetch current song information
    });

    // Vue lifecycle hook - executed when the component is unmounted
    onUnmounted(() => {
        // Clearing intervals to prevent memory leaks
        clearInterval(updateIntervalTimer);
        clearInterval(renderingIntervalTimer);
    });

    // Vue watcher to reset songHasEnded flag when the currentSong changes
    watch(currentSong, () => {
        songHasEnded.value = false;
    });

    // Function to fetch the current playing song information from the server.
    function getTime() {
        httpService.getNowPlaying().then(data => {
            // Updating reactive references based on the received data
            currentSong.value = data.song;
            isPlaying.value = data.isPlaying;

            if (data.song) {
                playHead.value = addTransmissionTime(data.time, data.stamp);
                songEndTime.value = Date.now() + (data.song.duration * 1000) - addTransmissionTime(data.time, data.stamp);
            }
        });
    }

    // Function to calculate the progress of the currently playing song.
    function calculateProgress() {
        if (!currentSong.value) {
            // Resetting progress if there is no current song
            progress.value = 0;
            currentTime.value = 0;
            return;
        }

        if (!isPlaying.value) {
            // Updating progress and currentTime if the song is paused
            currentTime.value = playHead.value / 1000;
            progress.value = (playHead.value / (currentSong.value?.duration * 1000)) * 100;
            return;
        }

        if (songEndTime.value - Date.now() < 0) {
            // Handling case when the song has ended
            if (songHasEnded.value) {
                return;
            }

            getTime(); // Fetching new song information
            songHasEnded.value = true; // Setting flag to prevent repeated requests
        }

        // Calculating progress and currentTime for the playing song
        progress.value = (1 - ((songEndTime.value - Date.now()) / 1000) / currentSong.value?.duration) * 100;
        currentTime.value = currentSong.value.duration - (songEndTime.value - Date.now()) / 1000;
    }

    // Function to adjust the transmission time for synchronizing song progress.
    function addTransmissionTime(value: number, stampSender: number) {
        return value + Date.now() - stampSender;
    }

    // Returning reactive references for use in the Vue component
    return { currentSong, currentTime, progress, isPlaying };
}
