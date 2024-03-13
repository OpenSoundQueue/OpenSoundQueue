# PropertyService Documentation 📄

`PropertyService.java` is a part of the backend system management package designed to interact with system properties. This service is responsible for setting, getting, and transforming property values within the application. It leverages a utility class, `PropertyLoader`, to load and save properties, thus abstracting the complexity of property management.

## Table of Contents
- [Overview](#overview)
- [Methods](#methods)
  - [setProperty](#setproperty)
  - [getProperty](#getproperty)
  - [getPropertyAsList](#getpropertyaslist)
- [Dependencies](#dependencies)
- [Usage](#usage)

## Overview

**Package:** `com.example.backend.system_management`

**Class:** `PropertyService`

**Annotations:**
- `@Service`: Marks this class as a Spring service stereotype.

**Dependencies:**
- `PropertyLoader`: Autowired component used for loading and saving properties.

## Methods

### `setProperty`

Sets a specific property to a given value.

- **Parameters:**
  - `property`: Name of the property to set.
  - `value`: Value to assign to the property.
- **Throws:** `IOException` if an error occurs during property file handling.

**Example:**

```java
setProperty("database.url", "jdbc:mysql://localhost:3306/mydb");
```

### `getProperty`

Retrieves the value of a specified property as a `String`.

- **Parameters:**
  - `property`: Name of the property to retrieve.
- **Returns:** The value of the property.
- **Throws:** `IOException` if an error occurs during property file handling.

**Example:**

```java
String dbUrl = getProperty("database.url");
```

### `getPropertyAsList`

Gets a property value, expected to be in list format, and splits it into an array of `String`.

- **Parameters:**
  - `property`: Name of the property to retrieve.
- **Returns:** An array of `String` representing the list elements.
- **Throws:** `IOException` if an error occurs during property file handling.

**Example:**

```java
String[] servers = getPropertyAsList("server.list");
```

## Dependencies

This class relies on the `PropertyLoader` class for loading and saving properties. It is autowired, meaning Spring will handle its instantiation and injection, ensuring `PropertyService` has a working instance to utilize.

## Usage

`PropertyService` is designed for easy integration into Spring-based applications. It can be autowired into other components, services, or controllers to manage application properties dynamically. This service is particularly useful for applications that need to modify or access configurations at runtime without the need for hardcoding values.

**Autowired Example:**

```java
@Autowired
private PropertyService propertyService;
```

By utilizing `PropertyService`, developers can abstract the complexities of property management, improve the flexibility of their application, and maintain cleaner code by centralizing property handling logic within this service.