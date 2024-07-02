export class ApplicationSettings {
    private _language: string;
    private _emailAuth: boolean;
    private _fromEmail: string;
    private _emailPassword: string;
    private _emailHostString: string;
    private _isPrivate: boolean;
    private _entryCode: string;
    private _enabledSources: string[];
    private _supportedSources: string[];

    constructor(language: string, emailAuth: boolean, fromEmail: string, emailPassword: string, emailHostString: string, isPrivate: boolean, entryCode: string, enabledSources: string[], supportedSources: string[]) {
        this._language = language;
        this._emailAuth = emailAuth;
        this._fromEmail = fromEmail;
        this._emailPassword = emailPassword;
        this._emailHostString = emailHostString;
        this._isPrivate = isPrivate;
        this._entryCode = entryCode;
        this._enabledSources = enabledSources;
        this._supportedSources = supportedSources;
    }

    static areEqual(applicationSettings1: ApplicationSettings, applicationSettings2: ApplicationSettings) {
        if (applicationSettings1.language !== applicationSettings2.language) {
            return false;
        }

        if (applicationSettings1.emailAuth !== applicationSettings2.emailAuth) {
            return false;
        }

        if (applicationSettings1.fromEmail !== applicationSettings2.fromEmail) {
            return false;
        }

        if (applicationSettings1.emailPassword !== applicationSettings2.emailPassword) {
            return false;
        }

        if (applicationSettings1.emailHostString !== applicationSettings2.emailHostString) {
            return false;
        }

        if (applicationSettings1.isPrivate !== applicationSettings2.isPrivate) {
            return false;
        }

        if (applicationSettings1.entryCode !== applicationSettings2.entryCode) {
            return false;
        }

        if (applicationSettings1.enabledSources.length !== applicationSettings2.enabledSources.length) {
            return false;
        }

        for (let source of applicationSettings1.enabledSources) {
            if (!applicationSettings2.enabledSources.includes(source)) {
                return false;
            }
        }

        if (applicationSettings1.supportedSources.length !== applicationSettings2.supportedSources.length) {
            return false;
        }

        for (let supportedSource of applicationSettings1.supportedSources) {
            if (!applicationSettings2.supportedSources.includes(supportedSource)) {
                return false;
            }
        }

        return true;
    }

    static clone(applicationSettings: ApplicationSettings): ApplicationSettings {
        return new ApplicationSettings(
            applicationSettings.language,
            applicationSettings.emailAuth,
            applicationSettings.fromEmail,
            applicationSettings.emailPassword,
            applicationSettings.emailHostString,
            applicationSettings.isPrivate,
            applicationSettings.entryCode,
            [...applicationSettings.enabledSources],
            [...applicationSettings.supportedSources]
        );
    }

    get language(): string {
        return this._language;
    }

    set language(value: string) {
        this._language = value;
    }

    get emailAuth(): boolean {
        return this._emailAuth;
    }

    set emailAuth(value: boolean) {
        this._emailAuth = value;
    }

    get isPrivate(): boolean {
        return this._isPrivate;
    }

    get supportedSources(): string[] {
        return this._supportedSources;
    }

    set isPrivate(value: boolean) {
        this._isPrivate = value;
    }

    get entryCode(): string {
        return this._entryCode;
    }

    set entryCode(value: string) {
        this._entryCode = value;
    }

    get enabledSources(): string[] {
        return this._enabledSources;
    }

    set enabledSources(value: string[]) {
        this._enabledSources = value;
    }

    get fromEmail(): string {
        return this._fromEmail;
    }

    set fromEmail(value: string) {
        this._fromEmail = value;
    }

    get emailPassword(): string {
        return this._emailPassword;
    }

    set emailPassword(value: string) {
        this._emailPassword = value;
    }

    get emailHostString(): string {
        return this._emailHostString;
    }

    set emailHostString(value: string) {
        this._emailHostString = value;
    }

    toDto(): object {
        return {
            language: this._language,
            emailAuth: this._emailAuth,
            fromEmail: this._fromEmail,
            emailPassword: this._emailPassword,
            emailHostString: this._emailHostString,
            isPrivate: this._isPrivate,
            entryCode: this._entryCode,
            enabledSources: this._enabledSources,
            supportedSources: this._supportedSources
        };
    }
}