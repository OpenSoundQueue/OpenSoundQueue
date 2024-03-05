// Imports the 'registration' store.
import { registration } from "@/store/store";

// Defines the shape of the Registration object.
export type Registration = {
    username: string;
    email: string;
    password: string;
    timestamp: number;
    state: "input" | "validation" | "finished";
};

// Retrieves the Registration object from localStorage.
export function getRegistration(): Registration {
    // Retrieve registration from localStorage
    const storedRegistration = localStorage.getItem("registration");

    // Checks if the storedRegistration exists in localStorage.
    if (storedRegistration !== null) {
        const parsedRegistration = JSON.parse(storedRegistration);

        // Checks if the parsedRegistration matches the Registration type.
        if (isRegistration(parsedRegistration)) {
            return parsedRegistration;
        } else {
            throw new Error("Invalid registration found in localStorage.");
        }
    } else {
        throw new Error("Invalid registration found in localStorage.");
    }
}

// Type guard function to check if an object is of type Registration.
function isRegistration(obj: any): obj is Registration {
    // Extracts the keys of the registration object.
    const requiredKeys = Object.keys(registration);

    // Checks if the object is of type 'object' and all required keys exist.
    return (
        typeof obj === "object" &&
        obj !== null &&
        requiredKeys.every((key) => key in obj)
    );
}

// Removes the Registration object from localStorage.
export function removeRegistration(){
    localStorage.removeItem("registration");
}
