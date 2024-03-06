# UserService.java - Documentation 📚

The `UserService` class is a crucial component in the user management module of a backend application, designed to handle various user-related functionalities. This service encapsulates logic for user operations such as retrieval, registration, authentication, role management, and more.

## Table of Contents

- [Overview](#overview)
- [Constructor and Dependencies](#constructor-and-dependencies)
- [Core Functionalities](#core-functionalities)
- [Utility Methods](#utility-methods)
- [Email Verification](#email-verification)
- [Role and Permission Management](#role-and-permission-management)
- [Miscellaneous](#miscellaneous)

## Overview

**Package:** `com.example.backend.user_management`

**Dependencies:**
- UserInfoRepository
- PasswordEncoder
- TokenUtils
- EmailUtils
- EmailComponent
- RoleRepository

**Features:**
- User retrieval by various identifiers
- User registration and password handling
- Email verification mechanism
- Role and permission management
- Online status tracking

## Constructor and Dependencies

The `UserService` class is initialized with several dependencies required for its operations:

```java
public UserService(
    UserInfoRepository userInfoRepository,
    PasswordEncoder passwordEncoder,
    TokenUtils tokenUtils,
    EmailUtils emailUtils,
    EmailComponent emailComponent,
    RoleRepository roleRepository
) {
    this.userInfoRepository = userInfoRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenUtils = tokenUtils;
    this.emailUtils = emailUtils;
    this.emailComponent = emailComponent;
    this.roleRepository = roleRepository;
}
```

## Core Functionalities

### User Retrieval

Methods for retrieving users based on username, token, ID, and email.

```java
public UserInfoEntity getUserByUsername(String username);
public UserInfoEntity getUserByToken(String token);
public UserInfoEntity getUserById(Long id);
public UserInfoEntity getUserByEmail(String email);
```

### User Registration

Two methods for registering users, with and without a password.

```java
public UserInfoEntity registerNewAuthUser(UserInfoEntity user);
public UserInfoEntity registerNewUser(UserInfoEntity user);
```

### Token Management

Updating and removing access tokens for users.

```java
public void updateToken(Long id, String token);
public void removeToken(UserInfoEntity user);
```

### Online Status

Updating the last online timestamp of a user and retrieving all online users.

```java
public void updateLastOnline(UserInfoEntity user);
public List<UserDto> getAllOnlineUsers();
```

## Utility Methods

- `getAll()`: Returns a list of all users.
- `deleteUser(Long id)`: Deletes a user by ID.

## Email Verification

Sending an email for account verification and verifying the code received by the user.

```java
public void sendEmailVerification(UserInfoEntity user) throws MessagingException, IOException;
public boolean verifyEmail(String email, String code);
```

## Role and Permission Management

Changing roles for a user, retrieving a user's permissions, and role-specific operations.

```java
public void changeRolesOfUser(long id, List<Role> roles);
public List<Permissions> getPermissionsOfUser(long id);
public void removeRole(int id);
```

## Miscellaneous

Updating a user's username or email, and utility functions for managing user roles within the application context.

```java
public void updateName(long id, String name);
public void updateEmail(long id, String email);
```

---

This document provides a comprehensive overview of the `UserService` class, detailing its purpose, functionalities, and how it integrates within the broader user management system of the backend application. Through various methods, it facilitates user management operations, ensuring a seamless and secure user experience.