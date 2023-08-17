import {createServer, Response, Model} from "miragejs"
import type {Server} from "miragejs/server";
import type {ModelDefinition, Registry} from "miragejs/-types";
import type Schema from "miragejs/orm/schema";
import {User} from "@/models/User";

// typescript help:
// https://github.com/miragejs/examples/blob/master/react-typescript/src/mirage/index.ts
// https://github.com/miragejs/miragejs/issues/460

export function makeServer({environment = "development"} = {}) {
    const UserModel: ModelDefinition<User> = Model.extend({})

    return createServer({
        environment,

        models: {
            user: UserModel
        },

        seeds(server: Server) {
            server.create("user", {
                username: "Bob",
                password: "1234",
                sessionKey: "sdfasdfasdf"
            } as Object);

            server.create("user", {
                username: "Alice",
                password: "35234",
                sessionKey: "243asdfj"
            } as Object);
        },

        routes() {
            this.namespace = "api"

            type AppRegistry = Registry<{ user: typeof UserModel }, { /* factories can be defined here */ }>
            type AppSchema = Schema<AppRegistry>

            this.get("/users", (schema: AppSchema) => {
                return schema.all("user")
            })

            this.post("/login", (schema: AppSchema, request) => {
                let requestBody = JSON.parse(request.requestBody);

                const user = schema.findBy("user", {username: requestBody.username});

                if (user === null) {
                    return new Response(
                        400,
                        {},
                        { errors: ["could not find user"]}
                    )
                }

                if (user.password !== requestBody.password) {
                    return new Response(
                        400,
                        {},
                        { errors: ["wrong password"]}
                    )
                }

                return {sessionKey: user.sessionKey};
            })
        },
    })
}