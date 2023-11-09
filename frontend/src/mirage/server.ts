import {createServer, Model} from "miragejs"
import type {Server} from "miragejs/server";
import type {ModelDefinition, Registry} from "miragejs/-types";
import type Schema from "miragejs/orm/schema";
import songs from "@/mirage/fixtures/songs";
import users from "@/mirage/fixtures/users";
import {Song} from "@/models/Song";
import {User} from "@/models/User";

// typescript help:
// https://github.com/miragejs/examples/blob/master/react-typescript/src/mirage/index.ts
// https://github.com/miragejs/miragejs/issues/460

export function makeServer({environment = "development"} = {}) {
    const SongModel: ModelDefinition<Song> = Model.extend({});
    const UserModel: ModelDefinition<User> = Model.extend({});

    return createServer({
        environment,

        models: {
            song: SongModel,
            user: UserModel
        },

        fixtures: {
            songs,
            users,
        },

        seeds(server: Server) {
            server.loadFixtures("songs");
            server.loadFixtures("users");
            //disable console.log()
            server.logging = false;
        },

        routes() {
            this.namespace = "api"

            type AppRegistry = Registry<{ song: typeof SongModel,user: typeof SongModel }, { /* factories can be defined here */ }>
            type AppSchema = Schema<AppRegistry>

            this.get("/queue/page/:pageNumber/page-size/:pageSize", (schema: AppSchema, request) => {
                const pageNumber: number = parseInt(request.params.pageNumber);
                const pageSize: number = parseInt(request.params.pageSize);

                const songs = schema.db.songs;

                const start: number = pageSize * pageNumber;
                const end: number = start + pageSize;

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
            let hasVoted = false;
            this.get("vote-skip/status", () => {
                return {
                    hasVoted: hasVoted,
                    received: votes,
                    required: 5
                };
            })

            this.get("vote-skip/vote", () => {
                if (hasVoted) {
                    return {};
                }

                votes++;
                hasVoted = true;

                return {
                    hasVoted: hasVoted,
                    received: votes,
                    required: 5
                };
            })

            this.get("vote-skip/withdraw", () => {
                if (!hasVoted) {
                    return {};
                }

                votes--;
                hasVoted = false;

                return {
                    hasVoted: hasVoted,
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

            this.post("/login/private/auth", (schema: AppSchema, request) => {
                const username = request.params.username;
                const password = request.params.password;
                const entryCode = request.params.entryCode;
                return {
                    "type": "Private & Auth"
                };
            })

            this.post("/login/public/auth", (schema: AppSchema, request) => {
                const username = request.params.username;
                const password = request.params.password;
                return {
                    "type": "Public & Auth"
                };
            })

            this.post("/login/private", (schema: AppSchema, request) => {
                const username = request.params.username;
                const entryCode = request.params.entryCode;
                return {
                    "type": "Private & nix"
                };
            })

            this.post("/login/public", (schema: AppSchema, request) => {
                const username = request.params.username;
                return {
                    "type": "Public & nix"
                };
            })

            this.get("/verify/api-key", (schema: AppSchema, request) => {
                return {};
            })

            this.get("/users", (schema: AppSchema) => {
                return schema.db.users;
            })

            this.get("/self", (schema: AppSchema) => {
                return schema.db.users.find(2);
            })

            this.delete("/user/:id", (schema: AppSchema,request) => {
                const id = request.params.id;
                const user = schema.db.users.find(id);

                if (!user){
                    return {"error": "user with id:"+id+" not found"}
                }

                schema.db.users.remove(user);
                return schema.db.users;
            })

            this.patch("/queue/change-order", (schema: AppSchema, request) => {
                const songs = schema.db.songs;

                return songs.map((song, index) => {
                    return {
                        song: song,
                        numberInQueue: index
                    }
                });
            })
        },
    })
}