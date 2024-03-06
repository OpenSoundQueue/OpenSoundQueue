# Vue Router Configuration Documentation 📄

This documentation provides a detailed overview of the Vue Router setup defined in `index.ts`. The router is a critical component for single-page applications built with Vue.js, enabling navigation between pages without reloading the website.

## Table of Contents

- [Router Initialization](#router-initialization)
- [Routes Definition](#routes-definition)
- [Router Guards](#router-guards)
  - [Installation State Guard](#installation-state-guard)
  - [Authentication Guard](#authentication-guard)
  - [Session Key Validation Guard](#session-key-validation-guard)
- [Dependencies](#dependencies)

## Router Initialization

The router is created by calling `createRouter` with a configuration object. This object specifies the `history` mode and the `routes` for the application:

```typescript
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [...]
});
```

### History Mode

The router uses the `createWebHistory` function to enable HTML5 history mode, allowing for cleaner URLs (no `#` hashbang).

## Routes Definition

The application's routes are defined within the `routes` array. Each route is an object that specifies the path, name, component to render, and additional metadata:

| Path                   | Name                    | Component               | Meta                                        |
|------------------------|-------------------------|-------------------------|---------------------------------------------|
| `/`                    | `public`                | `PublicView`            | `requiresAuth: false`                       |
| `/home`                | `home`                  | `HomeView`              | `requiresAuth: true`                        |
| `/login`               | `login`                 | `LoginView`             | `requiresAuth: false`                       |
| `/register`            | `register`              | `RegistrationView`      | `requiresAuth: false`                       |
| `/settings`            | `settings`              | `SettingsView`          | `requiresAuth: true`                        |
| `/installation`        | `installation`          | `InstallationView`      | Redirects for sub-paths                     |
| `/admin`               | `admin`                 | Various                 | Requires permissions                        |

### Nested and Dynamic Routes

Some routes are nested, such as `/home`, which has children routes for different access levels. Dynamic routes are also utilized, for example, `/login/:entryCode` to support login with an entry code.

## Router Guards

Vue Router allows defining global guards that are executed before navigation.

### Installation State Guard

Before each route navigation, the router checks the installation state of the application. If installation is not finished, the user is redirected to the `/installation` path, ensuring the setup process is completed first.

### Authentication Guard

This guard checks if a route requires authentication. It verifies the stored API key and ensures the user has the necessary permissions, if required by the route. If the verification fails, the user is redirected to the `/login` path.

### Session Key Validation Guard

For routes that do not explicitly require authentication, this guard checks if a session key exists and validates it. If the session key is invalid, a logout prompt is shown to the user.

## Dependencies

The router setup relies on several external services and utilities:

- **Vue Router** for SPA navigation.
- **HttpService** for API interactions.
- **CookieService** for managing cookies, particularly the API key.
- **PopUpService** and **ToastService** for user notifications.
- **PermissionService** for permission checks.
- **TranslationPlugin** for i18n.

This Vue Router configuration is essential for controlling access to different parts of the application based on authentication status, installation state, and user permissions, ensuring a secure and user-friendly navigation experience.