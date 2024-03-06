# 📄 PopUpService Documentation

The `PopUpService.ts` file defines a service for creating and managing popups in a Vue application. This service allows for showing a popup with a message and a button, waiting for user interaction, and then performing actions based on that interaction. Below is a detailed documentation of its functionality and usage.

## 🌟 Features

- **Reactive Variables:** Uses Vue's reactive system to manage popup states.
- **Dynamic Content:** Allows setting of popup message and button text dynamically.
- **User Action Tracking:** Captures and responds to user actions on the popup.
- **Asynchronous User Interaction Handling:** Waits for the user to interact with the popup before proceeding.

## 🛠️ Methods

| Method              | Parameters                           | Description                                                               |
|---------------------|--------------------------------------|---------------------------------------------------------------------------|
| `openPopUp`         | `message: string, buttonText: string`| Opens a popup with the specified message and button text.                 |
| `updatePopUp`       | `event: string`                      | Updates the popup's state based on a user action.                         |
| `waitForUserAction` | -                                    | Waits asynchronously for the user to take an action on the popup.         |

## 📦 Usage

### **Opening a Popup**

To display a popup, call the `openPopUp` method with the desired message and button text.

```typescript
PopUpService.openPopUp("Your session is about to expire.", "Extend");
```

### **Updating User Action**

When the user interacts with the popup (e.g., clicks the button), call `updatePopUp` to record this action.

```typescript
// Example of updating the user action upon a button click
PopUpService.updatePopUp("extendSession");
```

### **Waiting for User Action**

To pause the execution until the user responds to the popup, use the `waitForUserAction` method. This method returns a promise that resolves with the user's action.

```typescript
async function handleUserResponse() {
  const action = await PopUpService.waitForUserAction();
  if (action === "extendSession") {
    // Extend the session
  }
}
```

## 🎨 Reactive References

- **isVisible:** Controls the visibility of the popup.
- **message:** Contains the message displayed in the popup.
- **buttonText:** Defines the text displayed on the popup button.
- **userAction:** Stores the user's action in response to the popup.

## 💡 Example Scenario

Consider a web application that needs to warn users about their session expiring soon. Using `PopUpService`, the application can show a popup notifying the user and offering an option to extend the session. Once the user decides, the application can take appropriate action based on the user's choice.

By leveraging Vue's reactivity and the asynchronous nature of JavaScript, `PopUpService` provides a flexible and efficient way to manage popups and user interactions in Vue applications.