function validate(type:string, value:string):boolean {
    if (value.length === 0) return true;
    switch(type){
        case "password":
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
            return passwordRegex.test(value);
            break;
        case "username":
            const usernameRegex = /^[a-zA-Z0-9_-]{3,20}$/;
            return usernameRegex.test(value);
            break;
    }
}

export function $validateUsername(value:string): Function{
    return () => validate("username",value)
}
export function $validatePassword(value:string): Function{
    return () => validate("password",value)
}