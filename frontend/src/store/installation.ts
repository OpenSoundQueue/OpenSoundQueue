import {installation} from "@/store/store";
import type {RouteRecordName} from "vue-router";

// Defines the shape of the Installation object.
export type Installation = {
    currentStep: RouteRecordName;
};

// Retrieves the Installation object from localStorage.
export function getInstallation(): Installation {
    // Retrieve installation from localStorage
    const storedInstallation = localStorage.getItem("installation");

    // Checks if the storedInstallation exists in localStorage.
    if (storedInstallation !== null) {
        const parsedInstallation = JSON.parse(storedInstallation);

        // Checks if the parsedInstallation matches the Installation type.
        if (isInstallation(parsedInstallation)) {
            return parsedInstallation;
        } else {
            throw new Error("Invalid installation found in localStorage.");
        }
    } else {
        throw new Error("Invalid installation found in localStorage.");
    }
}

// Type guard function to check if an object is of type Installation.
function isInstallation(obj: any): obj is Installation {
    // Extracts the keys of the installation object.
    const requiredKeys = Object.keys(installation);

    // Checks if the object is of type 'object' and all required keys exist.
    return (
        typeof obj === "object" &&
        obj !== null &&
        requiredKeys.every((key) => key in obj)
    );
}
