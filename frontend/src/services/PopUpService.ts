import {ref} from "vue";

// Defines the PopUpService class.
export class PopUpService {
    // Initializes reactive references for popup visibility, message, button text, and user action.
    static isVisible = ref(false);
    static message = ref("");
    static buttonText = ref("");
    static userAction = ref("");

    // Opens the popup with the specified message and button text.
    static openPopUp(message: string, buttonText: string) {
        this.message.value = message;
        this.buttonText.value = buttonText;
        this.isVisible.value = true;
    }

    // Updates the user action based on the provided event.
    static updatePopUp(event: string) {
        this.userAction.value = event;
    }

    // Waits for the user to take an action on the popup.
    static async waitForUserAction() {
        return new Promise((resolve) => {
            const checkUserAction = () => {
                if (this.userAction.value !== "") {
                    this.isVisible.value = false;
                    resolve(this.userAction.value);
                    this.userAction.value = "";
                } else {
                    setTimeout(checkUserAction, 100); // Check every 100ms (adjust as needed)
                }
            };
            checkUserAction();
        });
    }
}
