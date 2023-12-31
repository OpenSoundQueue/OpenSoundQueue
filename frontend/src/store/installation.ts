import {installation} from "@/store/store";
import type {RouteRecordName} from "vue-router";

export type Installation = {
    currentStep: RouteRecordName
}

export function getInstallation(): Installation {
// Retrieve installation from localStorage
    const storedInstallation = localStorage.getItem("installation");

    if (storedInstallation !== null) {
        const parsedInstallation = JSON.parse(storedInstallation);

        // Check if the parsedInstallation match the Installation type
        if (isInstallation(parsedInstallation)) {
            return parsedInstallation;
        } else {
            throw new Error("Invalid settings found in localStorage.")
        }
    } else {
        throw new Error("Invalid settings found in localStorage.");
    }
}

// Type guard function to check if an object is of type Settings
function isInstallation(obj: any): obj is Installation {
    const requiredKeys = Object.keys(installation);

    return (
        typeof obj === "object" &&
        obj !== null &&
        requiredKeys.every((key) => key in obj)
    );
}
