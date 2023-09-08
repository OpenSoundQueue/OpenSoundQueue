export interface SongDto {
    title: string,
    artist: string
    duration: number
}

export class Song {
    private readonly _title;
    private readonly _artist;
    private readonly _duration;

    constructor(title: string, artist: string, duration: number) {
        this._title = title;
        this._artist = artist;
        this._duration = duration;
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
}