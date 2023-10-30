import {ref} from "vue";
import type {Ref} from "vue";


export class PopUpService {
    static isVisible = ref(false);
    static message= ref("");
    static userAction = ref("");

    static openPopUp(message: string) {
        this.message.value = message;
        this.isVisible.value = true;
    }

    static updatePopUp(event: string){
        this.userAction.value = event
    }

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