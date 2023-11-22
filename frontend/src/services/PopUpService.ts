import {ref} from "vue";


export class PopUpService {
    static isVisible = ref(false);
    static message = ref("");
    static buttonText = ref("");
    static userAction = ref("");

    static openPopUp(message: string, buttonText: string) {
        this.message.value = message;
        this.buttonText.value = buttonText;
        this.isVisible.value = true;
    }

    static updatePopUp(event: string) {
        this.userAction.value = event;
    }

    static async waitForUserAction() {
        return new Promise((resolve) => {
            // TODO: change to resolve() and reject()
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