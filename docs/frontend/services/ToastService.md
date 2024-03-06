# ToastService Documentation 📄✨

The `ToastService.ts` file defines a **ToastService** class responsible for managing toast notifications within a Vue.js application. This service allows showing brief messages to the user, indicating success, error, or information. Below is an overview of its functionalities.

## Overview 🌟

The **ToastService** class provides a simple way to display temporary messages to the user. It leverages Vue's reactivity system to show and hide messages with varying levels of importance.

### Key Features:

- Display toast notifications with a message.
- Customize message level (information, success, error).
- Automatically hide the toast after a specified duration.
- Manually close the toast.

## API Reference 📚

### Properties and Methods

| Property/Method       | Type                            | Description                                                                 |
|-----------------------|---------------------------------|-----------------------------------------------------------------------------|
| `isVisible`           | `Ref<boolean>`                  | Reactive property to control the visibility of the toast.                   |
| `messageLevel`        | `Ref<MessageLevel>`            | Reactive property to set the level of the message (information, success, error).|
| `message`             | `Ref<string>`                   | Reactive property to set the content of the toast message.                  |
| `sendNotification`    | `(message: string, messageLevel: MessageLevel, duration: number) => void` | Method to display the toast with the specified message, level, and duration.|
| `close`               | `() => void`                    | Method to manually close the toast.                                         |

### Types

```typescript
type MessageLevel = "information" | "success" | "error";
```

### Usage Examples 🚀

#### Displaying a Success Message

```typescript
import { ToastService } from './ToastService';

ToastService.sendNotification('Operation successful!', 'success', 3000);
```

#### Closing the Toast Manually

```typescript
import { ToastService } from './ToastService';

ToastService.close();
```

## Implementation Details 🛠️

The `ToastService` utilizes Vue's reactive system (`ref` and `Ref` from Vue) to manage the state of the toast notifications. It defines static properties `isVisible`, `messageLevel`, and `message` to control the toast's visibility, level, and content, respectively. A private `timeout` variable is used to manage the duration the toast is displayed.

The `sendNotification` method sets the message properties and uses `setTimeout` to automatically hide the toast after a specified duration. The `close` method provides a way to manually hide the toast by clearing the timeout and setting `isVisible` to `false`.

## Conclusion 📌

The **ToastService** class in `ToastService.ts` is a practical utility for managing toast notifications in Vue.js applications. It abstracts the complexity of handling the toast's lifecycle, providing an easy-to-use interface for displaying temporary messages to the user.