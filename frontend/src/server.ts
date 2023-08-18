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

            type AppRegistry = Registry<{ song: typeof SongModel}, { /* factories can be defined here */ }>
            type AppSchema = Schema<AppRegistry>

            this.post("/queue-page", (schema: AppSchema, request) => {
                let requestBody = JSON.parse(request.requestBody);

                const songs = schema.db.songs;

                let start = requestBody.pageSize * requestBody.pageNumber;
                let end = start + requestBody.pageSize;

                return {
                    page: songs.slice(start, end),
                    numberOfPages: Math.ceil(songs.length / requestBody.pageSize)
                };
            })
        },
    })
}