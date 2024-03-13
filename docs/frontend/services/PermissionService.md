# PermissionService Documentation 📃

The `PermissionService.ts` module is part of a Vue application, leveraging the composition API from Vue 3. This service is designed to manage user permissions within the application, providing functionalities to fetch, check, and evaluate permissions granted to the user.

## Overview 🌟

The `PermissionService` class is a pivotal part of the application's security and access control mechanism. It interacts with an `HttpService` to fetch permissions from a server and provides methods to check the presence of specific permissions or sets of permissions.

## Table of Contents

- [Dependencies](#dependencies)
- [Permission Types](#permission-types)
- [PermissionService Class](#permissionservice-class)
  - [Properties](#properties)
  - [Methods](#methods)
    - [getPermissions](#getpermissions)
    - [checkPermission](#checkpermission)
    - [hasAnyPermission](#hasanypermission)
    - [hasAllPermissions](#hasallpermissions)

## Dependencies

The service relies on Vue 3's `ref` API for reactivity and a custom `HttpService` for network requests.

- **Vue Composition API**: Specifically, the `ref` from Vue 3 is used to create a reactive reference.
- **HttpService**: A custom service (presumably for HTTP requests) used to fetch the permissions from a server.

```typescript
import { ref } from "vue";
import type { Ref } from "vue";
import { HttpService } from "./HttpService";
```

## Permission Types

The service defines a set of permission types as a TypeScript union type. These represent the different actions or features a user might have access to within the application.

```typescript
export type PermissionType = "VOTESKIP" | "SKIP" | "ADD_SONG" | "REPLAY" | "HISTORY_SEARCH" | "PAUSE_PLAY" | "CHANGE_VOLUME" | "CHANGE_ORDER" | "DELETE_SONGS" | "MANAGE_ROLES" | "MANAGE_USER" | "MANAGE_SYSTEM_SETTINGS";
```

## PermissionService Class

### Properties

- **permissions**: A reactive reference (`Ref`) holding an array of `PermissionType`. Initially empty, it is meant to be populated with the permissions fetched from the server.

### Methods

#### getPermissions

Fetches the current user's permissions from the server and updates the `permissions` reactive reference.

**Signature**:
```typescript
static async getPermissions(): Promise<void>
```

#### checkPermission

Checks if a specific permission is granted to the user.

**Signature**:
```typescript
static checkPermission(permission: PermissionType): boolean
```

#### hasAnyPermission

Determines if any permissions in a given array are granted to the user.

**Signature**:
```typescript
static hasAnyPermission(permissionArray: PermissionType[]): boolean
```

#### hasAllPermissions

Checks if all permissions in a given array are granted to the user.

**Signature**:
```typescript
static hasAllPermissions(permissionArray: PermissionType[]): boolean
```

## Example Usage 🚀

To utilize the `PermissionService`, one might first call `getPermissions` to fetch and populate the permissions. Subsequently, the `checkPermission`, `hasAnyPermission`, or `hasAllPermissions` methods can be used to determine access for specific actions or features.

```typescript
async function setupPermissions() {
  await PermissionService.getPermissions(); // Fetch permissions
  const canAddSong = PermissionService.checkPermission("ADD_SONG");
  console.log(`Can add song: ${canAddSong}`);
}
```

This service is essential for managing access control in applications, ensuring that users can only interact with features and functionalities they are explicitly granted access to.