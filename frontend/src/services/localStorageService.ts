type Form = {
    username: string,
    email: string,
    password: string
    timestamp: number
}

export function saveForm(username: string, email: string, password: string) {
    const form = {
        username: username,
        email: email,
        password: password,
        timestamp: Date.now()
    }

    localStorage.setItem("form", JSON.stringify(form))
}

export function getForm(): Form {
    const form = localStorage.getItem("form")
    if (!form)
        return {username: "", email: "", password: "", timestamp: Date.now()}
    else {
        const formData = JSON.parse(form);
        if ((Date.now() - formData.timestamp) / 1000 > 60 * 15) {
            localStorage.removeItem("form")
            return {username: "", email: "", password: "", timestamp: Date.now()}
        } else {
            return formData;
        }
    }
}

export function deleteForm() {
    localStorage.removeItem("form")
}

export function setRegisterPosition(position: number) {
    localStorage.setItem("registerPosition", position + "")
}

export function getRegisterPosition(): number {
    const position = localStorage.getItem("registerPosition")
    if (!position)
        return 0;
    else
        return parseInt(position);
}

export function deleteRegisterPosition() {
    localStorage.removeItem("registerPosition")
}
