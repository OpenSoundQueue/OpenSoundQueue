import {registration} from "@/store/store";

export type Registration = {
    username: string,
    email: string,
    password: string,
    timestamp: number,
    state: "input" | "validation" | "finished"
}

export function getRegistration(): Registration {
// Retrieve registration from localStorage
    const storedRegistration = localStorage.getItem("registration");

    if (storedRegistration !== null) {
        const parsedRegistration = JSON.parse(storedRegistration);

        // Check if the parsedRegistration match the Registration type
        if (isRegistration(parsedRegistration)) {
            return parsedRegistration;
        } else {
            throw new Error("Invalid registration found in localStorage.")
        }
    } else {
        throw new Error("Invalid registration found in localStorage.");
    }
}

// Type guard function to check if an object is of type Registration
function isRegistration(obj: any): obj is Registration {
    const requiredKeys = Object.keys(registration);

    return (
        typeof obj === "object" &&
        obj !== null &&
        requiredKeys.every((key) => key in obj)
    );
}

export function removeRegistration(){
    localStorage.removeItem("registration")
}
