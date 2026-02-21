# User Management System (UMS)

A backend User Management System built using Spring Boot and MongoDB.
This project provides REST APIs for managing users such as create, update,
fetch, and delete operations.

---

## 🚀 Tech Stack
- Java 17+
- Spring Boot
- Spring Data MongoDB
- Maven
- MongoDB

---

## 📂 Project Structure
    src/main/java
    └── com.example.ums    
    ├── controller
    ├── service
    ├── repository
    ├── model
    └── UmsApplication.java


---

## ⚙️ Configuration
Application configuration is managed using `application.yml`.

> ⚠️ Sensitive data should NOT be committed to GitHub.

---

## ▶️ How to Run
1. Clone the repository
```bash
git clone https://github.com/Saransh-27/User-Management-System.git
```
2. Configure MongoDB (see section below)
3. Run the application
```bash
mvn spring-boot:run
```
---
## 📡 API Features

- Create User

 - Get User by ID

- Get All Users

- Update User

- Delete User

## 📡 API Endpoints

Base URL:
``` bash
 http://localhost:8081
 ```
## 👤 User APIs
    Method     Endpoint        Description
    POST	   /users	       Create a new user
    GET	       /users	       Get all users
    GET	       /users/{id}	   Get user by ID
    PUT	       /users/{id}	   Update user by ID
    DELETE	   /users/{id}	   Delete user by ID

## 📥 Sample Request – Create User
    POST /users
    Content-Type: application/json

    {
    "name": "John Doe",
    "email": "john.doe@example.com"
    }

## 📤 Sample Response
    {
    "id": "65c1f8c2a12e4b9f9e21c001",
    "name": "John Doe",
    "email": "john.doe@example.com"
    }
---
## ⚠️ Error Responses (recommended)
    HTTP Status	Meaning
    400	Bad Request
    404	User Not Found
    500	Internal Server Error

## 🧪 Test Using Tools

    You can test these APIs using:    
    Postman    
    curl
    browser (GET APIs)

## 📌 Notes

    All endpoints return JSON
    IDs are MongoDB ObjectId strings
    Base path can be versioned later (/api/v1/users)
---
## 🧠 Learning Purpose

This project is created for learning Spring Boot, REST APIs, and MongoDB
following industry-level practices.

---
## 👤 Author

Saransh Dhiman

