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
        },
    })
}