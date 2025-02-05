# ChImp - Instant Messaging Application

---

## Index

1. [Introduction](#introduction)  
2. [Features](#features)  
   - 2.1 [User Authentication](#User-Authentication)  
   - 2.2 [Messaging Channels](#messaging-channels)  
   - 2.3 [Profile Management](#profile-management)  
   - 2.4 [Invitation Management](#invitation-management)  
   - 2.5 [Local Persistence](#local-persistence)  
   - 2.6 [Reusable Components](#reusable-components)  
   - 2.7 [Real-time Updates](#real-time-updates)  
   - 2.8 [Mock Service](#mock-service)  
3. [Project Structure](#project-structure)  
   - 3.1 [Directories and Files](#directories-and-files)  
4. [Dependencies](#dependencies)  
5. [ChImp Activity and Screen Diagram](#chimp-activity-and-screen-diagram)  
6. [General Overview](#general-overview)  
7. [Key Elements](#key-elements)  
   - 7.1 [Main Activities](#main-activities)  
   - 7.2 [State Management](#state-management)  
   - 7.3 [Activity Relationships](#activity-relationships)  
   - 7.4 [Integration with Resources](#integration-with-resources)  
   - 7.5 [Architectural Pattern](#architectural-pattern)  
8. [Flow Details](#flow-details)  
   - 8.1 [State Transitions](#state-transitions)  
   - 8.2 [Navigation](#navigation)  
   - 8.3 [Real-Time Updates](#real-time-updates)  
   - 8.4 [Mock Service for Testing](#mock-service-for-testing)  
9. [Robustness](#robustness)  
10. [Background work](#worker-configuration)
11. [Conclusions](#conclusions)

---

## Introduction

**ChImp** is an instant messaging application designed to offer **real-time communication** through modern Android development practices. It incorporates features like user authentication, message channels, and profile management, utilizing best practices for both local and networked data management. The application communicates with a backend server via API requests to fetch, send, and process events, ensuring seamless **real-time interaction between users**.

---

## Features

1. **User Authentication**:
   - Login, registration, and secure session management.
   - Token-based authentication with cookies for session persistence.

2. **Messaging Channels**:
   - Real-time messaging using Server-Sent Events (SSE).
   - Support for multiple channels.

3. **Profile Management**:
   - View and edit user profiles.

4. **Invitation Management**:
   - View and accept/reject pending invitations to channels.

5. **Local Persistence**:
   - Uses Room Database for offline data storage.
   - DataStore Preferences for storing lightweight user preferences and cookies.

6. **Reusable Components**:
   - Modular and reusable UI components for faster development.

7. **Real-time updates**:  
   - Achieved through Server-Sent Events (SSE) for receiving updates and events.

8. **Mock Service**:
   - A service mock was created to emulate backend APIs, enabling faster development and testing without relying on a live server.

---

## Project Structure

This diagram illustrates the overall architecture of the ChImp application, including the main components and their interactions.

![Architecture Diagram](<architecture.drawio%20(3).png>)

---

### Directories and Files

- **app/**: Main source code and resources.
  - **src/**: Source code.
    - **main/**: Main source code and resources.
      - **java/pt/isel/chimp/**: Kotlin source files.
        - **about/**: About screen.
        - **authentication/**: Authentication screens.
        - **channels/**: Channel screens.
        - **components/**: Reusable UI components.
        - **domain/**: Domain models.
        - **home/**: Home screen.
        - **infrastructure/**: Data persistence using DataStore
        - **invitationList/**: Invitation List screen.
        - **menu/**: Menu screen.
        - **profile/**: Profile screen.
        - **repository/**: Data access layer following the Repository Pattern.
        - **service/**: API communication, including the mock service.
        - **storage/**: Local data storage using room.
        - **ui/**: UI classes and themes.
        - **utils/**: Utility classes.
      - **res/**: Resources (layouts, drawables, etc.).
      - **AndroidManifest.xml**: Android manifest file.

## Dependencies

- **Ktor Client**:
  - For network communication with cookies and Server sent events.
- **Datastore Preferences**:
  - For secure and efficient storage of user preferences..
- **Room Database**:
  - For local data persistence, enabling offline capabilities.

- **Server-Sent Events**:
  - For real-time updates and push notifications from the server.

---

## ChImp Activity and Screen Diagram

![](<pdm.drawio%20(2).png>)

## General Overview

This diagram illustrates the navigation structure and state flows of the **Activities** and **Screens** in the **ChImp** application. It shows the interactions between Activities, ViewModels, and data persistence layers, as well as the transitions between different states.

---

## Key Elements

### 1. **Main Activities**

- **Representation**: Activities are highlighted in **blue**.
- Each Activity acts as a primary navigation point in the application, managing user interactions and connecting different features.

### 2. **State Management**

- **Representation**: Initial states are marked with **yellow circles** (e.g., **Idle**).
- Common states include:
  - **Loading**: Indicates data is being fetched or processed.
  - **Error**: Represents failures such as network issues or validation errors.
  - **Success**: Marks the completion of an operation.
- State transitions are clearly defined to ensure robust handling of various scenarios.

### 3. **Activity Relationships**

- The diagram maps the transitions between Activities, showing how users navigate through different screens and features in the application.

### 4. **Integration with Resources**

- Each Activity is supported by a **ViewModel** that manages:
  - UI state and logic.
  - Access to the local database (**Room**).
  - Communication with backend services through APIs.
  - State and data updates using **Kotlin Flows** for reactive and asynchronous data streams.
- These integrations ensure seamless data synchronization and responsiveness.

### 5. **Repository Pattern**

- The Repository Pattern ensures that business logic is independent of the data sources, making it easier to test and modify data management strategies in the future.

- The Repository Pattern ensures a Single Source of Truth by centralizing data access and maintaining consistency throughout the application. This approach decouples business logic from data sources, making it easier to test, maintain, and adapt to evolving data management strategies.

---

## Flow Details

### State Transitions

- Each Activity and its corresponding ViewModel handle multiple states using **Flows**, ensuring the application responds predictably to user actions, network events, and other triggers.

### Navigation

- Navigation between Activities is structured and intuitive, reflecting clear user workflows throughout the application.

### Real-Time Updates

- Certain screens implement real-time functionality, leveraging **Flows** to handle dynamic state transitions and live data updates efficiently.

---

## Robustness

- The inclusion of error states ensures that the application can gracefully handle failures, such as network errors or database access issues.
- Defined state transitions, combined with **Flows**, contribute to predictable and reliable application behavior.

---
This diagram provides a clear overview of the **ChImp** application’s structure and behavior, highlighting:

- State and navigation management.
- The use of **Kotlin Flows** for reactive data handling.
- Integration of modern architectural practices, such as **MVVM**.
- Robust and scalable design principles.
  
---

## SSE Background Worker Documentation - `CoroutineSseWorkItem`

The `CoroutineSseWorkItem` class is a **background worker** that handles **Server-Sent Events (SSE)** in the **ChImp** app. It listens for real-time events from the server, processes them in the background, and updates the app’s state accordingly. This worker operates as a foreground service to ensure it continues running even when the app is in the background.

The application uses a **Worker** to listen for real-time events (SSE) via **WorkManager**. The worker is configured to run only when the network is connected and is responsible for processing events from the server, when the Activity **ChannelListActivity** is created.

### **Worker Configuration:**

- **Constraints**: The worker runs only when the network is connected (`NetworkType.CONNECTED`).
- **One-Time Work**: Uses `ExistingWorkPolicy.KEEP` to ensure the work is not replaced.

```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()
val workRequest = OneTimeWorkRequestBuilder<CoroutineSseWorkItem>()
    .setConstraints(constraints)
    .addTag("sse")
    .build()

WorkManager.getInstance(applicationContext).enqueueUniqueWork(
    "SseWork",
    ExistingWorkPolicy.KEEP,
    workRequest
)
```

## Key Concepts

### 1. SSE Listener

- The worker listens for **Server-Sent Events (SSE)** by connecting to a specific endpoint: `/api/sse/listen`.
- As events are received from the server, they are handled in real time, allowing the app to stay updated without needing to constantly poll the server.
  
### 2. Foreground Service

- **Foreground services** are used for long-running tasks that should not be killed by the system (like waiting for real-time updates).
- While the worker is active, a **persistent notification** is displayed to the user, showing that the app is listening for updates in the background.

### 3. Event Handling

The worker processes various types of events, including but not limited to:

- **New Messages**: Notifies the user about new messages in a channel.
- **Channel Updates**: Handles updates like channel name changes or new members joining.
- **Invitations**: Listens for incoming invitations and their statuses (e.g., accepted, rejected).

Each event is mapped to a handler function that updates the app’s state or database. For instance:

- A **new message** event triggers the `messageMapper` to insert the message into the database.
- A **channel name update** triggers the `channelNameUpdateMapper` to update the channel name in the repository.

### 4. Notification Management

- Each event that requires user attention (like a new message) triggers a **notification**.
- Notifications include essential information such as the sender's name and the content of the message. They use **unique IDs** (generated from the timestamp) to prevent overwriting.
- The user can interact with notifications to open specific parts of the app (such as the messaging screen).

### 5. Repository Interaction

- The worker interacts with various repositories:
  - **Message Repository**: To insert or update messages.
  - **Channel Repository**: To update channel data, such as new members or name changes.
  - **Invitation Repository**: To manage invitations (e.g., insert new ones or delete accepted ones).
- The worker uses repository methods to ensure the local data remains synchronized with the server.

### 6. Error Handling and Retries

- If an error occurs during event handling (e.g., network failure), the worker will **retry** the operation after a short delay (`Result.retry()`).
- Errors are logged, and the worker will keep attempting to receive events unless explicitly stopped.

---

## Conclusions

While the current implementation of the app handles core functionality effectively, such as SSE integration, navigation, and background task management, there is a notable gap in the automated testing coverage. 

Some views already have automated tests in place, but **unit tests** and **UI tests** for the **Screen ViewModels** and **Database operations** are still missing. These tests are crucial for ensuring the stability of business logic, data handling, and UI interactions as the app evolves.

Moving forward, it will be important to prioritize the creation of:

- **Unit Tests** for ViewModels to validate business logic.
- **UI Tests** to ensure correct behavior of screens and user interactions.
- **Database Tests** to guarantee data integrity and proper handling of offline scenarios.

Implementing comprehensive testing will help maintain the app’s reliability and prevent regressions during future development.
