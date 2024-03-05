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

    // Define MirageJS models for each entity
    const SongModel: ModelDefinition<Song> = Model.extend({});
    const UserModel: ModelDefinition<User> = Model.extend({});
    const RoleModel: ModelDefinition<Role> = Model.extend({});
    const PermissionModel: ModelDefinition<Permission> = Model.extend({});

    // Function to create MirageJS server
    return createServer({
        environment,

        // Define models and fixtures
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

        // Seed the server with fixture data
        seeds(server: Server) {
            server.loadFixtures("songs");
            server.loadFixtures("users");
            server.loadFixtures("roles");
            server.loadFixtures("permissions");
            //disable console.log()
            server.logging = false; // Disable logging
        },

        // Define API routes
        routes() {
            this.namespace = "api" // Set namespace for API routes

            type AppRegistry = Registry<{
                song: typeof SongModel,
                user: typeof SongModel
                role: typeof RoleModel
                permission: typeof PermissionModel
            }, { /* factories can be defined here */ }>
            type AppSchema = Schema<AppRegistry>

            // Endpoint for paginated queue items
            this.get("/queue/page/:pageNumber/page-size/:pageSize", (schema: AppSchema, request) => {
                // Extract page number and page size from the request parameters
                const pageNumber: number = parseInt(request.params.pageNumber);
                const pageSize: number = parseInt(request.params.pageSize);

                // Get all songs from the database
                const songs = schema.db.songs;

                // Return empty page if there are no songs
                if (songs.length == 0)
                    return { page: undefined, numberOfPages: undefined };

                // Calculate start and end indexes for pagination
                const start: number = pageSize * pageNumber;
                const end: number = start + pageSize;

                // Return paginated songs and number of pages
                return {
                    page: songs.slice(start, end).map((song, index) => {
                        return {
                            song: song,
                            numberInQueue: index + pageSize * pageNumber
                        }
                    }),
                    numberOfPages: Math.ceil(songs.length / pageSize)
                };
            });

            // Endpoint for getting all queue items
            this.get("/queue/all", (schema: AppSchema) => {
                // Get all songs from the database
                const songs = schema.db.songs;

                // Return all songs with their index in the queue
                return songs.slice(1).map((song, index) => {
                    return {
                        song: song,
                        numberInQueue: index
                    }
                });
            });

            // Endpoint for getting the currently playing song
            let lastRequest = Date.now();
            let queueIsPlaying = true;
            let time = 0;
            this.get("/queue/now-playing", (schema: AppSchema) => {
                // Get all songs from the database
                const songs = schema.db.songs;

                // Return information about the currently playing song
                if (songs.length == 0) {
                    return {
                        isPlaying: false,
                        time: 0,
                        stamp: Date.now(),
                        song: null
                    }
                }

                // Check if the current song has finished playing
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

                // Return information about the currently playing song
                return {
                    isPlaying: queueIsPlaying,
                    time: time,
                    stamp: Date.now(),
                    song: songs[0]
                }
            });

            // Endpoint for adding a random song to the queue
            this.post("/queue/add/", (schema: AppSchema) => {
                // Get all songs from the database
                const songs = schema.db.songs;

                // Generate a random index to select a song
                const random = Math.floor(Math.random() * songs.length);

                // Return the randomly selected song
                return {
                    ...songs[random]
                };
            });

            // Route for getting the status of vote skipping
            let votes = 0;
            let hasVoted = false;
            this.get("vote-skip/status", () => {
                // Return status of vote skipping
                return {
                    hasVoted: hasVoted,
                    received: votes,
                    required: 25
                };
            });

            // Route for voting to skip the current song
            this.get("vote-skip/vote", () => {
                // Check if the user has already voted
                if (hasVoted) {
                    return {};
                }

                // Increment vote count and mark user as voted
                votes++;
                hasVoted = true;

                // Return updated vote skipping status
                return {
                    hasVoted: hasVoted,
                    received: votes,
                    required: 25
                };
            });

            // Route for withdrawing vote to skip the current song
            this.get("vote-skip/withdraw", () => {
                // Check if the user has voted
                if (!hasVoted) {
                    return {};
                }

                // Decrement vote count and mark user as not voted
                votes--;
                hasVoted = false;

                // Return updated vote skipping status
                return {
                    hasVoted: hasVoted,
                    received: votes,
                    required: 25
                };
            })

            // Route for skipping the current song in the queue
            this.post("/queue/skip", (schema: AppSchema) => {
                // Remove the current song from the queue
                const currentSong = schema.db.songs[0];
                schema.db.songs.remove(currentSong);

                // Reset time and last request variables
                time = 0;
                lastRequest = Date.now();

                // Reset vote-related variables
                hasVoted = false;
                votes--;

                // Return the updated queue
                return schema.db.songs;
            })

            // Route for replaying the current song in the queue
            this.post("/queue/replay", () => {
                // Reset time to replay the current song
                time = 0;

                // Return an empty object
                return {};
            })

            // Route for starting playback of the queue
            this.post("/queue/start", () => {
                // Set queueIsPlaying to true to start playback
                queueIsPlaying = true;

                // Return an empty object
                return {};
            })

            // Route for stopping playback of the queue
            this.post("/queue/stop", () => {
                // Set queueIsPlaying to false to stop playback
                queueIsPlaying = false;

                // Return an empty object
                return {};
            })

            // Function for fuzzy search on an array of items
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

            // Route for searching the queue history by title
            this.get("search/history/:searchTerm", (schema: AppSchema, request) => {
                const searchTerm = request.params.searchTerm;

                const songs: any[] = schema.db.songs;

                // Perform fuzzy search on songs array based on title
                const searchByTitle = fuzzySearch(songs, "title")
                const results = searchByTitle(searchTerm);

                // Return the search results
                return {
                    ...results
                };
            })

            // Route for searching the queue history by title with maximum results limit
            this.get("search/history/:searchTerm/max-results/:maxResults", (schema: AppSchema, request) => {
                const searchTerm = request.params.searchTerm;
                const maxResults = parseInt(request.params.maxResults);

                const songs: any[] = schema.db.songs;

                // Perform fuzzy search on songs array based on title
                const searchByTitle = fuzzySearch(songs, "title")
                const results = searchByTitle(searchTerm);

                // Return search results with maximum result limit applied
                if (results.length > maxResults) {
                    return results.slice(0, maxResults);
                }

                return {
                    ...results
                };
            })

            // Route for private authentication login
            this.post("/login/private/auth", (schema: AppSchema, request) => {
                const username = request.params.username;
                const password = request.params.password;
                const entryCode = request.params.entryCode;
                return {
                    "type": "Private & Auth"
                };
            })

            // Route for public authentication login
            this.post("/login/public/auth", (schema: AppSchema, request) => {
                const username = request.params.username;
                const password = request.params.password;
                return {
                    "type": "Public & Auth"
                };
            })

            // Route for private login without authentication
            this.post("/login/private", (schema: AppSchema, request) => {
                const username = request.params.username;
                const entryCode = request.params.entryCode;
                return {
                    "type": "Private & nix"
                };
            })

            // Route for public login without authentication
            this.post("/login/public", (schema: AppSchema, request) => {
                const username = request.params.username;
                return {
                    "type": "Public & nix"
                };
            })

            // Route for verifying API key
            this.get("/verify/api-key", (schema: AppSchema, request) => {
                return {};
            })

            // Route for fetching all users
            this.get("/users", (schema: AppSchema) => {
                return schema.db.users;
            })

            // Route for fetching the current user
            this.get("/self", (schema: AppSchema) => {
                return schema.db.users.find(2);
            })

            // Route for deleting a user by ID
            this.delete("/user/:id", (schema: AppSchema, request) => {
                const id = request.params.id;
                const user = schema.db.users.find(id);

                if (!user) {
                    return {"error": "user with id:" + id + " not found"}
                }

                schema.db.users.remove(user);
                return schema.db.users;
            })

            // Route for changing the order of songs in the queue
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

            // Route for creating a user account
            this.post("/register/create-account", () => {
                return {};
            });

            // Route for logging out
            this.post("/logout", () => {
                return {};
            })

            // Route for fetching all roles
            this.get("/roles", (schema: AppSchema) => {
                return schema.db.roles;
            })

            // Route for fetching all permissions
            this.get("/permissions", (schema: AppSchema) => {
                return schema.db.permissions;
            })

            // Route for fetching a specific role by ID
            this.get("/role/get/:id", (schema: AppSchema, request) => {
                const id = request.params.id;
                return schema.db.roles.find(id);
            })

            // Route for deleting a role by ID
            this.delete("/role/:id", (schema: AppSchema, request) => {
                const id = request.params.id;
                const role: Role = schema.db.roles.find(id);

                schema.db.roles.remove(role);

                return schema.db.roles;
            })

            // Route for creating a new role
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

            // Route for editing an existing role
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

            // Default system settings
            let language = "en";
            let isPrivate = true;
            let entryCode = "Schodl";
            let emailAuth = false;
            let sources: Record<string, boolean> = {
                youtube: true,
                soundcloud: false,
            };
            let installationFinished = false;

            // Route to get the current language setting
            this.get("/system/language", (schema: AppSchema) => {
                return {
                    language: language
                };
            });

            // Route to update the language setting
            this.patch("/system/language/set/:language", (schema: AppSchema, request) => {
                language = request.params.language;

                return {
                    language: language
                };
            });

            // Route to get the privacy settings
            this.get("/system/privacy", (schema: AppSchema) => {
                return {
                    "isPrivate": isPrivate,
                    "entryCode": entryCode
                };
            });

            // Route to update the privacy settings
            this.patch("/system/privacy/set", (schema: AppSchema, request) => {
                const body = JSON.parse(request.requestBody);
                isPrivate = body.isPrivate;
                entryCode = body.entryCode;

                return {
                    "isPrivate": isPrivate,
                    "entryCode": entryCode
                };
            });

            // Route to get the email authentication setting
            this.get("/system/email-auth", (schema: AppSchema) => {
                return {
                    "emailAuth": emailAuth
                };
            });

            // Route to update the email authentication setting
            this.patch("/system/email-auth/set/:emailAuth", (schema: AppSchema, request) => {
                emailAuth = (request.params.emailAuth == "true");

                return {
                    "emailAuth": emailAuth
                };
            });

            // Route to get the list of supported sources
            this.get("/system/supported-sources", (schema: AppSchema) => {
                return Object.keys(sources);
            });

            // Route to get the enabled sources
            this.get("/system/sources", (schema: AppSchema) => {
                return Object.keys(sources).filter((key) => sources[key]);
            });

            // Route to update the enabled sources
            this.patch("/system/sources/set", (schema: AppSchema, request) => {
                const body = JSON.parse(request.requestBody);

                // Reset all sources to false
                for (const source of Object.keys(sources)) {
                    sources[source] = false;
                }

                // Enable selected sources
                body.sources.forEach((source: string) => {
                    if (Object.keys(sources).includes(source)) {
                        sources[source] = true;
                    }
                });

                // Mark installation as finished
                installationFinished = true;

                return Object.keys(sources).filter((key) => sources[key]);
            });

            // Route to get the installation state
            this.get("/system/installation-state", (schema: AppSchema) => {
                return {
                    finished: installationFinished
                };
            });
        },
    })
}