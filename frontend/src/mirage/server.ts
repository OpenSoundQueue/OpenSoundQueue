import {createServer, Model} from "miragejs"
import type {Server} from "miragejs/server";
import type {ModelDefinition, Registry} from "miragejs/-types";
import type Schema from "miragejs/orm/schema";
import songs from "@/mirage/fixtures/songs";
import users from "@/mirage/fixtures/users";
import roles from "@/mirage/fixtures/roles";
import permissions from "@/mirage/fixtures/permissions";
import {Song} from "@/models/Song";
import {User} from "@/models/User";
import {Role} from "@/models/Role";
import {Permission} from "@/models/Permission";

// typescript help:
// https://github.com/miragejs/examples/blob/master/react-typescript/src/mirage/index.ts
// https://github.com/miragejs/miragejs/issues/460

export function makeServer({environment = "development"} = {}) {
    const SongModel: ModelDefinition<Song> = Model.extend({});
    const UserModel: ModelDefinition<User> = Model.extend({});
    const RoleModel: ModelDefinition<Role> = Model.extend({});
    const PermissionModel: ModelDefinition<Permission> = Model.extend({});

    return createServer({
        environment,

        models: {
            song: SongModel,
            user: UserModel,
            role: RoleModel,
            permissions: PermissionModel
        },

        fixtures: {
            songs,
            users,
            roles,
            permissions
        },

        seeds(server: Server) {
            server.loadFixtures("songs");
            server.loadFixtures("users");
            server.loadFixtures("roles");
            server.loadFixtures("permissions");
            //disable console.log()
            server.logging = false;
        },

        routes() {
            this.namespace = "api"

            type AppRegistry = Registry<{
                song: typeof SongModel,
                user: typeof SongModel
                role: typeof RoleModel
                permission: typeof PermissionModel
            }, { /* factories can be defined here */ }>
            type AppSchema = Schema<AppRegistry>

            this.get("/queue/page/:pageNumber/page-size/:pageSize", (schema: AppSchema, request) => {
                const pageNumber: number = parseInt(request.params.pageNumber);
                const pageSize: number = parseInt(request.params.pageSize);

                const songs = schema.db.songs;

                if (songs.length == 0)
                    return {page: undefined, numberOfPages: undefined}

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

                return songs.slice(1).map((song, index) => {
                    return {
                        song: song,
                        numberInQueue: index
                    }
                });
            })

            let lastRequest = Date.now();
            let queueIsPlaying = true;
            let time = 0;
            this.get("/queue/now-playing", (schema: AppSchema) => {
                const songs = schema.db.songs;

                if (songs.length == 0) {
                    return {
                        isPlaying: false,
                        time: 0,
                        stamp: Date.now(),
                        song: null
                    }
                }

                if (time > songs[0].duration * 1000) {
                    const currentSong = schema.db.songs[0];
                    schema.db.songs.remove(currentSong);
                    time = 0;

                    hasVoted = false;
                    votes--;
                } else {
                    time = queueIsPlaying ? time + (Date.now() - lastRequest) : time;
                }

                lastRequest = Date.now();

                return {
                    isPlaying: queueIsPlaying,
                    time: time,
                    stamp: Date.now(),
                    song: songs[0]
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
                    required: 25
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
                    required: 25
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
                    required: 25
                };
            })

            this.post("/queue/skip", (schema: AppSchema) => {
                const currentSong = schema.db.songs[0];
                schema.db.songs.remove(currentSong);

                time = 0;
                lastRequest = Date.now();

                hasVoted = false;
                votes--;

                return schema.db.songs;
            })

            this.post("/queue/replay", () => {
                time = 0;
                return {};
            })

            this.post("/queue/start", () => {
                queueIsPlaying = true;
                return {};
            })

            this.post("/queue/stop", () => {
                queueIsPlaying = false;
                return {};
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

            this.delete("/user/:id", (schema: AppSchema, request) => {
                const id = request.params.id;
                const user = schema.db.users.find(id);

                if (!user) {
                    return {"error": "user with id:" + id + " not found"}
                }

                schema.db.users.remove(user);
                return schema.db.users;
            })

            this.patch("/queue/change-order", (schema: AppSchema, request) => {
                const body = JSON.parse(request.requestBody);

                const songs = schema.db.songs;
                const songToMove = songs.splice(body.oldPos + 1, 1)[0];
                songs.splice(body.newPos + 1, 0, songToMove);

                for (const [index, song] of schema.db.songs.entries()) {
                    schema.db.songs.update(song.id, {
                        title: songs[index].title,
                        artist: songs[index].artist,
                        duration: songs[index].duration,
                        link: songs[index].link,
                    });
                }

                return schema.db.songs.map((song, index) => {
                    return {
                        song: song,
                        numberInQueue: index
                    }
                });
            })

            this.post("/register/create-account", () => {
                return {};
            });

            this.post("/logout", () => {
                return {};
            })

            this.get("/roles", (schema: AppSchema) => {
                return schema.db.roles;
            })

            this.get("/permissions", (schema: AppSchema) => {
                return schema.db.permissions;
            })

            this.get("/role/get/:id", (schema: AppSchema, request) => {
                const id = request.params.id;
                return schema.db.roles.find(id);
            })

            this.delete("/role/:id", (schema: AppSchema, request) => {
                const id = request.params.id;
                const role: Role = schema.db.roles.find(id);

                schema.db.roles.remove(role);

                return schema.db.roles;
            })

            this.post("/role/create", (schema: AppSchema) => {
                const id = schema.db.roles.length;

                schema.db.roles.insert({
                    "id": id,
                    "name": "SCHODL",
                    "permissions": [
                        {
                            "skip": false
                        },
                        {
                            "play": false
                        },
                        {
                            "viewAdminPanel": false
                        }
                    ]
                });

                return schema.db.roles;
            });

            this.patch("/role/edit", (schema: AppSchema) => {
                const id = schema.db.roles.length;

                schema.db.roles.update(1, {
                    "name": "Mod MOD",
                    "permissions": [
                        {
                            "skip": true
                        },
                        {
                            "play": true
                        },
                        {
                            "viewAdminPanel": false
                        }
                    ]
                });

                return schema.db.roles;
            });

            let language = "en"
            let isPrivate = true
            let entryCode = "Schodl"
            let emailAuth = false
            let sources: Record<string, boolean> = {
                youtube: false,
                soundcloud: false,
            }
            let installationFinished = false;

            this.get("/system/language", (schema: AppSchema) => {

                return {
                    language: language
                };
            });

            this.patch("/system/language/set/:language", (schema: AppSchema, request) => {
                language = request.params.language;

                return {
                    language: language
                };
            });

            this.get("/system/privacy", (schema: AppSchema) => {
                return {
                    "isPrivate": isPrivate,
                    "entryCode": entryCode
                };
            });

            this.patch("/system/privacy/set", (schema: AppSchema, request) => {
                const body = JSON.parse(request.requestBody);
                isPrivate = body.isPrivate
                entryCode = body.entryCode

                return {
                    "isPrivate": isPrivate,
                    "entryCode": entryCode
                };
            });

            this.get("/system/email-auth", (schema: AppSchema) => {
                return {
                    "emailAuth": emailAuth
                };
            });

            this.patch("/system/email-auth/set/:emailAuth", (schema: AppSchema, request) => {
                emailAuth = (request.params.emailAuth == "true");

                return {
                    "emailAuth": emailAuth
                };
            });

            this.get("/system/supported-sources", (schema: AppSchema) => {
                return Object.keys(sources);
            });

            this.get("/system/sources", (schema: AppSchema) => {
                return Object.keys(sources).filter((key) => sources[key])
            });

            this.patch("/system/sources/set", (schema: AppSchema, request) => {
                const body = JSON.parse(request.requestBody);

                for (const source of Object.keys(sources)) {
                    sources[source] = false
                }

                body.sources.forEach((source: string) => {
                    if (Object.keys(sources).includes(source)) {
                        sources[source] = true
                    }
                })

                installationFinished = true;

                return Object.keys(sources).filter((key) => sources[key])
            });

            this.get("/system/installation-state", (schema: AppSchema) => {
                return {
                    finished: installationFinished
                };
            });
        },
    })
}