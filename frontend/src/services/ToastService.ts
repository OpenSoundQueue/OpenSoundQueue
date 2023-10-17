import {ref} from "vue";
import type {Ref} from "vue";

type MessageLevel = "information" | "success" | "error";

export class ToastService {
    static isVisible = ref(false);
    static messageLevel: Ref<MessageLevel> = ref("information");
    static message= ref("");
    private static timeout: ReturnType<typeof setTimeout>;

    static sendNotification(message: string, messageLevel: MessageLevel , duration: number) {
        this.message.value = message;
        this.messageLevel.value = messageLevel;

        this.isVisible.value = true;

        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => this.isVisible.value = false, duration);
    }

    static close() {
        clearTimeout(this.timeout);
        this.isVisible.value = false;
    }
}