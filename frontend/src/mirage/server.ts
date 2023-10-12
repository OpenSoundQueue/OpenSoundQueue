import {createServer, Model} from "miragejs"
import type {Server} from "miragejs/server";
import type {ModelDefinition, Registry} from "miragejs/-types";
import type Schema from "miragejs/orm/schema";
import songs from "@/mirage/fixtures/songs";
import {Song} from "@/models/Song";

// typescript help:
// https://github.com/miragejs/examples/blob/master/react-typescript/src/mirage/index.ts
// https://github.com/miragejs/miragejs/issues/460

export function makeServer({environment = "development"} = {}) {
    const SongModel: ModelDefinition<Song> = Model.extend({});

    return createServer({
        environment,

        models: {
            song: SongModel
        },

        fixtures: {
            songs
        },

        seeds(server: Server) {
            server.loadFixtures("songs");
            //disable console.log()
            server.logging = false;
        },

        routes() {
            this.namespace = "api"

            type AppRegistry = Registry<{ song: typeof SongModel }, { /* factories can be defined here */ }>
            type AppSchema = Schema<AppRegistry>

            this.get("/queue/page/:pageNumber/page-size/:pageSize", (schema: AppSchema, request) => {
                let pageNumber: number = parseInt(request.params.pageNumber);
                let pageSize: number = parseInt(request.params.pageSize);

                const songs = schema.db.songs;

                let start: number = pageSize * pageNumber;
                let end: number = start + pageSize;

                return {
                    page: songs.slice(start, end).map((song, index) => {
                        return {
                            song: song,
                            numberInQueue: index + pageSize * pageNumber
                        }
                    }),
                    numberOfPages: Math.ceil(songs.length / pageSize)
                };
            })

            this.get("/queue/all", (schema: AppSchema) => {
                const songs = schema.db.songs;

                return songs.map((song, index) => {
                    return {
                        song: song,
                        numberInQueue: index
                    }
                });
            })

            let start = Date.now();
            this.get("/queue/now-playing", (schema: AppSchema) => {
                const songs = schema.db.songs;

                if (Date.now() - start > 100_000) {
                    start = Date.now();
                }

                return {
                    isPlaying: true,
                    time: Date.now() - start,
                    stamp: Date.now(),
                    song: songs[1]
                }
            })

            this.post("/queue/add/", (schema: AppSchema) => {
                const songs = schema.db.songs;
                const random = Math.floor(Math.random() * songs.length);

                return {
                    ...songs[random]
                };
            })

            let votes = 0;
            let isActive = false;
            this.get("vote-skip/status", () => {
                return {
                    isActive: isActive,
                    received: votes,
                    required: 5
                };
            })

            this.get("vote-skip/vote", () => {
                if (isActive) {
                    return {};
                }

                votes++;
                isActive = true;

                return {
                    isActive: isActive,
                    received: votes,
                    required: 5
                };
            })

            this.get("vote-skip/withdraw", () => {
                if (!isActive) {
                    return {};
                }

                votes--;
                isActive = false;

                return {
                    isActive: isActive,
                    received: votes,
                    required: 5
                };
            })

            function fuzzySearch(items: any[], key: any) {
                // Returns a method that you can use to create your own reusable fuzzy search.

                return function (query: string) {
                    const words = query.toLowerCase().split(' ');

                    return items.filter(function (item: string) {
                        const normalizedTerm = item[key].toLowerCase();

                        return words.every(function (word) {
                            return (normalizedTerm.indexOf(word) > -1);
                        });
                    });
                };
            }

            this.get("search/history/:searchTerm", (schema: AppSchema, request) => {
                const searchTerm = request.params.searchTerm;

                const songs: any[] = schema.db.songs;

                const searchByTitle = fuzzySearch(songs, "title")
                const results = searchByTitle(searchTerm);

                return {
                    ...results
                };
            })

            this.get("search/history/:searchTerm/max-results/:maxResults", (schema: AppSchema, request) => {
                const searchTerm = request.params.searchTerm;
                const maxResults = parseInt(request.params.maxResults);

                const songs: any[] = schema.db.songs;

                const searchByTitle = fuzzySearch(songs, "title")
                const results = searchByTitle(searchTerm);

                if (results.length > maxResults) {
                    return results.slice(0, maxResults);
                }

                return {
                    ...results
                };
            })
        },
    })
}