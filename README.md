# To-Do List API

A secure RESTful API built with Spring Boot that allows users to manage their daily tasks. Unlike standard CRUD applications, this API implements basic authentication, ensuring that users must register and log in to manage their own private to-do lists. It supports full task management capabilities including creating, updating status, and deleting tasks securely.

## Technologies Used

  * **Java**
  * **Spring Boot**
  * **Spring Security**
  * **Maven**

-----

## API Reference

**Base URLs:** 
* Users: `/api/v1/users`
* Tasks: `/api/v1/tasks`

*(The application will start by default on `http://localhost:8080`)*

**Authentication:** Except for the user registration endpoint, all endpoints are secured using **Basic Authentication**. You must pass the base64-encoded `username:password` in the `Authorization` header of your requests. The API also utilizes method-level security to ensure users can only access and modify their own tasks.

### 1. Register a New User

Creates a new user account with a securely hashed password. No authentication is required for this route.

  * **URL:** `/register` (appended to Users Base URL)
  * **Method:** `POST`
  * **Request Body:**
    ```json
    {
      "username": "johndoe",
      "password": "mySecurePassword123"
    }
    ```
  * **Success Response:**
      * **Code:** `201 CREATED`
      * **Content:** `"User registered successfully"` (String)
  * **Error Response:**
      * **Code:** `409 Conflict` (If the username already exists)

### 2. Get All Tasks

Retrieves a list of all tasks belonging to the authenticated user.

  * **URL:** `/` (appended to Tasks Base URL)
  * **Method:** `GET`
  * **Headers:** `Authorization: Basic <credentials>` (Required)
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `List<TaskResponse>` (JSON)

### 3. Get a Single Task

Retrieves a specific task by its unique ID, verifying ownership against the authenticated user.

  * **URL:** `/{taskId}`
  * **Method:** `GET`
  * **Headers:** `Authorization: Basic <credentials>` (Required)
  * **URL Parameters:** `taskId=[Long]` (Required)
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `TaskResponse` (JSON)
  * **Error Responses:**
      * **Code:** `404 Not Found` (If the task does not exist)
      * **Code:** `403 Forbidden` (If the user tries to access a task they do not own)

### 4. Create a New Task

Creates a new task linked to the currently authenticated user.

  * **URL:** `/`
  * **Method:** `POST`
  * **Headers:** `Authorization: Basic <credentials>` (Required)
  * **Request Body:**
    ```json
    {
      "title": "Buy groceries",
      "description": "Milk, Eggs, Bread, and Coffee"
    }
    ```
  * **Success Response:**
      * **Code:** `201 CREATED`
      * **Content:** `TaskResponse` (JSON)

### 5. Update a Task

Updates an existing task's details or completion status. The user must own the task to update it.

  * **URL:** `/{taskId}`
  * **Method:** `PUT`
  * **Headers:** `Authorization: Basic <credentials>` (Required)
  * **URL Parameters:** `taskId=[Long]` (Required)
  * **Request Body:**
    ```json
    {
      "title": "Buy groceries",
      "description": "Milk, Eggs, Bread, and Decaf Coffee",
      "isCompleted": true
    }
    ```
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** The updated `TaskResponse` object.
  * **Error Responses:**
      * **Code:** `404 Not Found` (If the task does not exist)
      * **Code:** `403 Forbidden` (If the user tries to update a task they do not own)

### 6. Delete a Task

Deletes a specific task by its ID, verifying ownership first.

  * **URL:** `/{taskId}`
  * **Method:** `DELETE`
  * **Headers:** `Authorization: Basic <credentials>` (Required)
  * **URL Parameters:** `taskId=[Long]` (Required)
  * **Success Response:**
      * **Code:** `204 NO CONTENT`
  * **Error Responses:**
      * **Code:** `404 Not Found` (If the task does not exist)
      * **Code:** `403 Forbidden` (If the user tries to delete a task they do not own)

-----

## Data Models

### UserRequest

Payload used for registering a new user account.
| Field | Type | Description |
| :--- | :--- | :--- |
| `username` | String | The desired username for the account |
| `password` | String | The raw password (hashed before saving) |

### TaskRequest

Payload used for creating a new task.
| Field | Type | Description |
| :--- | :--- | :--- |
| `title` | String | The title/name of the task |
| `description` | String | Detailed information about the task |

### TaskUpdateRequest

Payload used for updating an existing task, including its status.
| Field | Type | Description |
| :--- | :--- | :--- |
| `title` | String | The title/name of the task |
| `description` | String | Detailed information about the task |
| `isCompleted` | boolean | The current status of the task (`true` for done, `false` for pending) |

### TaskResponse

The standard response object representing a task.
| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | Unique identifier for the task |
| `title` | String | The title/name of the task |
| `description` | String | Detailed information about the task |
| `isCompleted` | boolean | Indicates whether the task has been finished |
