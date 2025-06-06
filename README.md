# Agreema API Documentation

## File Management APIs

### Upload File

- **Endpoint:** `POST /api/v1/files`
- **Description:** Upload a single file
- **Request Parameters:**
  - `file`: MultipartFile (form-data)
  - `fileInfo`: Request body with file metadata
- **Response:** FileInfo object
- **Status Code:** 201 (Created)

### Delete File

- **Endpoint:** `DELETE /api/v1/files/{id}`
- **Description:** Delete a file by ID
- **Path Parameters:**
  - `id`: File ID
- **Response:** No content
- **Status Code:** 200 (OK)

### Get All Files

- **Endpoint:** `GET /api/v1/files`
- **Description:** Get paginated list of files
- **Query Parameters:**
  - Supports filtering via @Filter specification
  - Supports pagination parameters
- **Response:** Paginated list of FileInfo objects
- **Status Code:** 200 (OK)

### Supported File Types

The system accepts the following file extensions:

- PDF (.pdf)
- JPEG (.jpg, .jpeg)
- PNG (.png)
- Microsoft Word (.doc)

## Contract Management APIs

### Create Contract

- **Endpoint:** `POST /api/v1/contracts`
- **Description:** Create a new contract with items
- **Request Body:** Contract object with items array
- **Example Request:**
  ```json
  {
  	"contractId": "HD001",
  	"name": "Office Equipment Supply Contract",
  	"signDate": "2024-01-10T09:00:00Z",
  	"status": "UNLIQUIDATED",
  	"items": [
  		{
  			"itemId": "VT001",
  			"name": "A4 Paper",
  			"unit": "Ream",
  			"quantity": 100,
  			"unitPrice": 50.0,
  			"total": 5000.0
  		}
  	]
  }
  ```
- **Response:** Created contract object
- **Status Code:** 201 (Created)

### Update Contract

- **Endpoint:** `PUT /api/v1/contracts/{id}`
- **Description:** Update contract status
- **Path Parameters:**
  - `id`: Contract ID
- **Request Body:** Contract object with updated status
- **Example Request:**
  ```json
  {
  	"id": 1,
  	"contractId": "HD001",
  	"status": "LIQUIDATED"
  }
  ```
- **Response:** Updated contract object
- **Status Code:** 200 (OK)

### Get All Contracts

- **Endpoint:** `GET /api/v1/contracts`
- **Description:** Get paginated list of contracts
- **Query Parameters:**
  - Supports filtering via @Filter specification
  - Supports pagination parameters
  - `page`: Page number (default: 1)
  - `size`: Items per page (default: 10)
- **Response:**
  ```json
  {
    "meta": {
      "page": 1,
      "pageSize": 10,
      "total": 100,
      "pages": 10
    },
    "result": [Contract objects]
  }
  ```
- **Status Code:** 200 (OK)

### Get Contract by ID

- **Endpoint:** `GET /api/v1/contracts/{contractId}`
- **Description:** Get contract by contract ID
- **Path Parameters:**
  - `contractId`: Contract ID
- **Response:** Contract object
- **Status Code:** 200 (OK)

### Contract Status

The system supports the following contract statuses:

- UNLIQUIDATED
- LIQUIDATED

// ...existing code...

## Item Management APIs

### Create Item

- **Endpoint:** `POST /api/v1/items`
- **Description:** Create a new item in a contract
- **Request Body:** Item object with contract reference
- **Example Request:**
  ```json
  {
  	"itemId": "VT001",
  	"name": "A4 Paper",
  	"unit": "Ream",
  	"quantity": 100,
  	"unitPrice": 50.0,
  	"total": 5000.0,
  	"contract": {
  		"contractId": "HD001"
  	}
  }
  ```
- **Response:** Created item object
- **Status Code:** 201 (Created)

### Update Item

- **Endpoint:** `PUT /api/v1/items/{id}`
- **Description:** Update an existing item
- **Path Parameters:**
  - `id`: Item ID
- **Request Body:** Item object with updated fields
- **Example Request:**
  ```json
  {
  	"id": 1,
  	"itemId": "VT001",
  	"quantity": 150,
  	"unitPrice": 45.0,
  	"total": 6750.0
  }
  ```
- **Response:** Updated item object
- **Status Code:** 200 (OK)

### Delete Item

- **Endpoint:** `DELETE /api/v1/items/{id}`
- **Description:** Delete an item by ID
- **Path Parameters:**
  - `id`: Item ID
- **Response:** No content
- **Status Code:** 200 (OK)

### Get All Items

- **Endpoint:** `GET /api/v1/items`
- **Description:** Get paginated list of items
- **Query Parameters:**
  - Supports filtering via @Filter specification
  - Supports pagination parameters
  - `page`: Page number (default: 1)
  - `size`: Items per page (default: 10)
- **Response:**
  ```json
  {
    "meta": {
      "page": 1,
      "pageSize": 10,
      "total": 100,
      "pages": 10
    },
    "result": [Item objects]
  }
  ```
- **Status Code:** 200 (OK)

### Get Item by ID

- **Endpoint:** `GET /api/v1/items/{itemId}`
- **Description:** Get item by item ID
- **Path Parameters:**
  - `itemId`: Item ID
- **Response:** Item object
- **Status Code:** 200 (OK)

## Authentication APIs

### Register User

- **Endpoint:** `POST /api/v1/auth/register`
- **Description:** Register a new user
- **Request Body:** User object
- **Example Request:**
  ```json
  {
  	"email": "admin@gmail.com",
  	"password": "123456"
  }
  ```
- **Response:** Created user object
- **Status Code:** 201 (Created)

### Login

- **Endpoint:** `POST /api/v1/auth/login`
- **Description:** Authenticate user and get tokens
- **Request Body:** Login credentials
- **Example Request:**
  ```json
  {
  	"username": "admin@gmail.com",
  	"password": "123456"
  }
  ```
- **Response:**
  ```json
  {
  	"user": {
  		"id": 1,
  		"email": "admin@gmail.com"
  	},
  	"accessToken": "eyJhbGciOiJIUzI1..."
  }
  ```
- **Cookie Set:** HTTP-only secure cookie with refresh token
- **Status Code:** 200 (OK)

### Get Account

- **Endpoint:** `GET /api/v1/auth/account`
- **Description:** Get current user account details
- **Headers Required:** Bearer token
- **Response:** User account details
- **Status Code:** 200 (OK)

### Refresh Token

- **Endpoint:** `GET /api/v1/auth/refresh`
- **Description:** Get new access token using refresh token
- **Cookie Required:** refresh_token
- **Response:**
  ```json
  {
  	"user": {
  		"id": 1,
  		"email": "admin@gmail.com"
  	},
  	"accessToken": "eyJhbGciOiJIUzI1..."
  }
  ```
- **Cookie Set:** New HTTP-only secure cookie with refresh token
- **Status Code:** 200 (OK)

### Logout

- **Endpoint:** `POST /api/v1/auth/logout`
- **Description:** Logout user and invalidate tokens
- **Headers Required:** Bearer token
- **Response:** No content
- **Cookie Set:** Clears refresh_token cookie
- **Status Code:** 200 (OK)
