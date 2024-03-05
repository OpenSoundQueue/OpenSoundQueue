import {ref} from "vue";
import type {Ref} from "vue";

// Defines the possible levels of messages.
type MessageLevel = "information" | "success" | "error";

// Defines the ToastService class.
export class ToastService {
    // Initializes reactive references for toast visibility, message level, and message content.
    static isVisible = ref(false);
    static messageLevel: Ref<MessageLevel> = ref("information");
    static message= ref("");
    // Initializes a timeout variable for controlling the duration of the toast display.
    private static timeout: ReturnType<typeof setTimeout>;

    // Sends a notification with the specified message, message level, and duration.
    static sendNotification(message: string, messageLevel: MessageLevel , duration: number) {
        this.message.value = message;
        this.messageLevel.value = messageLevel;

        this.isVisible.value = true;

        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => this.isVisible.value = false, duration);
    }

    // Closes the toast.
    static close() {
        clearTimeout(this.timeout);
        this.isVisible.value = false;
    }
}
