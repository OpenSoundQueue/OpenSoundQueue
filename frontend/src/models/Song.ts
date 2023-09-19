export class Song {
    private readonly _title;
    private readonly _artist;
    private readonly _duration;
    private readonly _link;

    constructor(title: string, artist: string, duration: number, link?: string) {
        this._title = title;
        this._artist = artist;
        this._duration = duration;
        this._link = link;
    }

    get title() {
        return this._title;
    }

    get artist() {
        return this._artist;
    }

    get duration() {
        return this._duration;
    }

    get link() {
        return this._link;
    }
}