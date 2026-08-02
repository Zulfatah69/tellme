# REST API Reference

Base URL: `http://localhost:8081` (default, configurable via `SERVER_PORT`)

## Authentication

### Login
- **Method:** POST
- **Path:** `/api/auth/login`
- **Auth required:** No
- **Description:** Authenticate user and receive a token.
- **Request Body:** `{ "username": "...", "password": "..." }`
- **Response Format:** JSON object containing user details and `token`.
- **Status Codes:** 200 OK, 401 Unauthorized

### Logout
- **Method:** POST
- **Path:** `/api/auth/logout`
- **Auth required:** Yes
- **Description:** Invalidates the current session token.
- **Response Format:** Empty
- **Status Codes:** 200 OK, 401 Unauthorized

## Users

### Get All Users
- **Method:** GET
- **Path:** `/api/users`
- **Auth required:** Yes (Admin)
- **Description:** Retrieve a list of all registered users.
- **Status Codes:** 200 OK, 403 Forbidden

### Get User by ID
- **Method:** GET
- **Path:** `/api/users/{id}`
- **Auth required:** Yes
- **Description:** Get specific user details.
- **Status Codes:** 200 OK, 404 Not Found

### Create User
- **Method:** POST
- **Path:** `/api/users`
- **Auth required:** No (for registration) / Yes (Admin for manual creation)
- **Request Body:** `{ "username": "...", "password": "...", "email": "..." }`
- **Status Codes:** 201 Created, 400 Bad Request

### Update User
- **Method:** PUT
- **Path:** `/api/users/{id}`
- **Auth required:** Yes
- **Status Codes:** 200 OK, 404 Not Found

### Delete User
- **Method:** DELETE
- **Path:** `/api/users/{id}`
- **Auth required:** Yes (Admin)
- **Status Codes:** 204 No Content, 404 Not Found

## Submissions (Aspirasi)

### Get All Submissions
- **Method:** GET
- **Path:** `/api/aspirasi`
- **Auth required:** Yes
- **Description:** Get list of all submissions, supporting pagination and filtering.
- **Status Codes:** 200 OK

### Get My Submissions
- **Method:** GET
- **Path:** `/api/aspirasi/my`
- **Auth required:** Yes
- **Description:** Get submissions created by the authenticated user.
- **Status Codes:** 200 OK

### Create Submission
- **Method:** POST
- **Path:** `/api/aspirasi`
- **Auth required:** Yes
- **Description:** Create a new submission. Accepts multipart/form-data for file uploads.
- **Status Codes:** 201 Created, 400 Bad Request

### Update Status
- **Method:** PUT
- **Path:** `/api/aspirasi/{id}/status`
- **Auth required:** Yes (Admin)
- **Request Body:** `{ "statusId": 2 }`
- **Status Codes:** 200 OK, 404 Not Found

### Process Submission
- **Method:** PUT
- **Path:** `/api/aspirasi/{id}/proses`
- **Auth required:** Yes (Admin)
- **Description:** Marks submission as being processed and triggers email notification.
- **Status Codes:** 200 OK, 404 Not Found

### Add Feedback
- **Method:** PUT
- **Path:** `/api/aspirasi/{id}/feedback`
- **Auth required:** Yes (Admin)
- **Description:** Provide final resolution feedback and closes submission.
- **Status Codes:** 200 OK, 404 Not Found

### Delete Submission
- **Method:** DELETE
- **Path:** `/api/aspirasi/{id}`
- **Auth required:** Yes (Admin)
- **Status Codes:** 204 No Content, 404 Not Found

### Dashboard Stats
- **Method:** GET
- **Path:** `/api/aspirasi/dashboard`
- **Auth required:** Yes
- **Description:** Summary statistics for the dashboard.
- **Status Codes:** 200 OK

### Top Categories
- **Method:** GET
- **Path:** `/api/aspirasi/top-kategori`
- **Auth required:** Yes
- **Status Codes:** 200 OK

## Forum

### Get Forum Posts
- **Method:** GET
- **Path:** `/api/forum-posts`
- **Auth required:** Yes
- **Status Codes:** 200 OK

### Create Forum Post
- **Method:** POST
- **Path:** `/api/forum-posts`
- **Auth required:** Yes
- **Status Codes:** 201 Created

### Delete Forum Post
- **Method:** DELETE
- **Path:** `/api/forum-posts/{id}`
- **Auth required:** Yes (Admin or Author)
- **Status Codes:** 204 No Content

### Get Comments
- **Method:** GET
- **Path:** `/api/forum-comments?postId={id}`
- **Auth required:** Yes
- **Status Codes:** 200 OK

### Create Comment
- **Method:** POST
- **Path:** `/api/forum-comments`
- **Auth required:** Yes
- **Status Codes:** 201 Created

### Delete Comment
- **Method:** DELETE
- **Path:** `/api/forum-comments/{id}`
- **Auth required:** Yes (Admin or Author)
- **Status Codes:** 204 No Content

## Categories & Statuses

### Get Categories
- **Method:** GET
- **Path:** `/api/kategori`
- **Auth required:** Yes
- **Status Codes:** 200 OK

### Create Category
- **Method:** POST
- **Path:** `/api/kategori`
- **Auth required:** Yes (Admin)
- **Status Codes:** 201 Created

### Delete Category
- **Method:** DELETE
- **Path:** `/api/kategori/{id}`
- **Auth required:** Yes (Admin)
- **Status Codes:** 204 No Content

### Get Statuses
- **Method:** GET
- **Path:** `/api/status`
- **Auth required:** Yes
- **Status Codes:** 200 OK

### Create Status
- **Method:** POST
- **Path:** `/api/status`
- **Auth required:** Yes (Admin)
- **Status Codes:** 201 Created

## Upload

### Upload File
- **Method:** POST
- **Path:** `/api/upload`
- **Auth required:** Yes
- **Description:** Standalone file upload endpoint.
- **Request Body:** `multipart/form-data` with field `file`.
- **Response Format:** File URL/Path
- **Status Codes:** 200 OK, 400 Bad Request
