export class ApplicationSettings {
    private _language: string; // = ref("en");
    private _requireEmailAuth: boolean; // = ref(true);
    private _isPrivate: boolean; // = ref(true);
    private _entryCode: string; // = ref("");
    private _sources: string[]; //Ref<string[]> = ref([]);
    private _supportedSources: string[];

    constructor(language: string, requireEmailAuth: boolean, isPrivate: boolean, entryCode: string, sources: string[], supportedSources: string[]) {
        this._language = language;
        this._requireEmailAuth = requireEmailAuth;
        this._isPrivate = isPrivate;
        this._entryCode = entryCode;
        this._sources = sources;
        this._supportedSources = supportedSources;
    }

    static areEqual(applicationSettings1: ApplicationSettings, applicationSettings2: ApplicationSettings) {
        if (applicationSettings1.language !== applicationSettings2.language) {
            return false;
        }

        if (applicationSettings1.requireEmailAuth !== applicationSettings2.requireEmailAuth) {
            return false;
        }

        if (applicationSettings1.isPrivate !== applicationSettings2.isPrivate) {
            return false;
        }

        if (applicationSettings1.entryCode !== applicationSettings2.entryCode) {
            return false;
        }

        if (applicationSettings1.sources.length !== applicationSettings2.sources.length) {
            return false;
        }

        if (!applicationSettings1.sources.every((element: string, index: number) => {
            return element === applicationSettings2.sources[index];
        })) {
            return false;
        }

        if (applicationSettings1.supportedSources.length !== applicationSettings2.supportedSources.length) {
            return false;
        }

        if (!applicationSettings1.supportedSources.every((element: string, index: number) => {
            return element === applicationSettings2.supportedSources[index];
        })) {
            return false;
        }

        return true;
    }

    static clone(applicationSettings: ApplicationSettings): ApplicationSettings {
        return new ApplicationSettings(
            applicationSettings.language,
            applicationSettings.requireEmailAuth,
            applicationSettings.isPrivate,
            applicationSettings.entryCode,
            [...applicationSettings.sources],
            [...applicationSettings.supportedSources]
        );
    }

    get language(): string {
        return this._language;
    }

    set language(value: string) {
        this._language = value;
    }

    get requireEmailAuth(): boolean {
        return this._requireEmailAuth;
    }

    set requireEmailAuth(value: boolean) {
        this._requireEmailAuth = value;
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

    get sources(): string[] {
        return this._sources;
    }

    set sources(value: string[]) {
        this._sources = value;
    }
}