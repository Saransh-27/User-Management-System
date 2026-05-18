# User Management System (UMS)

## 📌 Project Overview
This is a full-stack User Management System:
- 🔧 **Backend**: Spring Boot 3.5.10 (REST APIs)  
- 🎨 **Frontend**: React.js (Modernized UI)
- 🔐 **Authentication**: JWT + OTP Verification
- 📝 **Task Management**: Full lifecycle with file attachments
- ☁️ **Deployment**: Render (Backend) & Netlify (Frontend)

## 🌐 Frontend
**Live**: https://user-mng-system.netlify.app

**Code**: https://github.com/Saransh-27/User-Management-System-frontend

A comprehensive backend User Management System built with Spring Boot 3.5.10 and MongoDB, featuring JWT-based authentication, role-based authorization, email notifications with OTP verification, comprehensive activity logging, and RESTful APIs for complete user lifecycle management.

---

## 🚀 Tech Stack

### Core Framework
- **Java 17** - Programming language
- **Spring Boot 3.5.10** - Main application framework
- **Maven** - Build and dependency management

### Database & Security
- **MongoDB** - NoSQL database with optimized indexes
- **Spring Security 6.x** - Security framework
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password encryption
- **Role-based Access Control** - ADMIN and USER roles
- **OTP Verification** - 6-digit one-time password system

### Web & Communication
- **Spring Boot Starter Web** - REST API development
- **Spring Boot Starter Validation** - Input validation
- **Spring Boot Starter Mail** - Email sending capabilities
- **Swagger/OpenAPI 3.0** - Interactive API documentation

### Development Tools
- **Spring Boot DevTools** - Hot reload and development utilities
- **Lombok** - Code generation and boilerplate reduction
- **Spring Boot Starter AOP** - Aspect-Oriented Programming for logging

---

## 📂 Project Architecture

```
src/main/java/com/project/Ums/
├── UmsApplication.java              # Main Spring Boot application class
├── config/
│   ├── CorsConfig.java             # CORS configuration for cross-origin requests
│   ├── SpringSecurity.java         # Security configuration and JWT setup
│   └── SwaggerConfig.java          # Swagger configuration and API documentation
├── controller/
│   ├── AdminController.java        # Admin-only user management endpoints
│   ├── AuthController.java         # Authentication, JWT, and password reset endpoints
│   ├── LogController.java          # Activity log management endpoints
│   ├── PublicController.java       # Public user endpoints
│   ├── ReportController.java       # CSV reporting and analytics endpoints
│   └── VerificationController.java # Email verification endpoints
├── dto/
│   ├── ApiResponse.java            # Generic API response wrapper
│   ├── ErrorResponse.java          # Error response DTO with validation mapping
│   ├── ForgotPasswordDto.java      # Forgot password request DTO with validation
│   ├── LoginDto.java               # Login request DTO
│   ├── LoginResponse.java          # Login response with JWT token
│   ├── ResetPasswordDto.java       # Reset password request DTO with validation
│   ├── UserProfileDto.java         # User profile management DTO
│   ├── UserRequestDto.java         # User creation/update DTO with validation
│   ├── UserResponseDto.java        # User response DTO
│   ├── UserUpdateDto.java          # User profile update DTO
│   └── VerificationResponse.java   # Email verification response DTO
├── entity/
│   ├── PasswordResetToken.java     # Password reset token entity with expiry tracking
│   ├── Task.java                   # MongoDB Task entity with file attachments
│   ├── User.java                   # MongoDB User entity with roles and OTP
│   └── VerificationToken.java      # Email verification token entity
├── exception/
│   ├── AccountVerificationException.java    # Account verification error handler
│   ├── EmailServiceException.java           # Email service failure handler
│   ├── FileUploadException.java             # File upload operation handler
│   ├── GlobalExceptionHandler.java          # Global exception handler
│   ├── InsufficientPermissionException.java # Permission access handler
│   ├── InvalidPasswordException.java        # Invalid password handler
│   ├── InvalidTokenException.java           # Invalid token handler
│   ├── PasswordResetException.java          # Password reset operation handler
│   ├── ResourceNotFoundException.java       # Resource not found handler
│   ├── TokenExpiredException.java           # Token expiry handler
│   └── UserNotFoundException.java           # User lookup failure handler
├── filter/
│   └── JwtFilter.java              # JWT authentication filter
├── logging/
│   ├── ActivityLog.java            # Activity log entity
│   ├── ActivityLogAspect.java      # AOP aspect for automatic logging
│   ├── ActivityLogRepository.java  # MongoDB repository for logs
│   ├── ActivityLogService.java     # Log management service
│   └── LogActivity.java            # Log annotation for AOP
├── mapper/
│   ├── TaskMapper.java             # Task-DTO mapping utilities
│   └── UserMapper.java             # Entity-DTO mapping utilities
├── repository/
│   ├── PasswordResetTokenRepository.java # Password reset token repository
│   ├── TaskRepository.java         # MongoDB repository for tasks
│   └── UserRepository.java         # MongoDB repository interface
├── service/
│   ├── AdminService.java           # Admin business logic
│   ├── TaskService.java            # Task management logic
│   └── ... (Other services)
└── utils/
    └── JwtUtil.java                # JWT utilities

src/main/resources/
├── .env                            # Environment variables configuration
├── static/                         # Static resources
└── templates/                      # Template files

src/test/java/com/project/Ums/
├── UmsApplicationTests.java        # Main application test class
└── service/                        # Service layer tests
```

---

## ⚙️ Configuration

### Application Configuration
The application uses `.env` file for environment variables configuration:

#### Database Configuration
```env
MONGODB_URI=mongodb://localhost:27017/ums_db
# Or MongoDB Atlas connection string
# MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/ums_db
```

#### Email Configuration
```env
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password
```

#### Security Configuration
```env
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=3600000
```

#### Server Configuration
```env
SERVER_PORT=8081
```

#### Logging Configuration
```env
LOGGING_LEVEL_COM_PROJECT_UMS=INFO
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=DEBUG
LOG_FILE_NAME=logs/ums.log
```

#### Activity Log Configuration
```env
ACTIVITY_LOG_RETENTION_DAYS=90
ACTIVITY_LOG_CLEANUP_BATCH_SIZE=1000
ACTIVITY_LOG_CLEANUP_ENABLED=true
```

### Security Configuration
- **JWT Secret**: Custom signing key for token validation
- **Token Expiration**: 1 hour (3600000 milliseconds)
- **Password Encoding**: BCrypt with default strength
- **CORS**: Configured for cross-origin requests
- **Session Management**: Stateless (JWT-based)
- **Endpoint Security**: 
  - `/auth/**` - Public access
  - `/admin/**` - ADMIN role required
  - `/public/**` - Authentication required
  - Swagger endpoints - Public access

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MongoDB 4.4 or higher
- SMTP server (for email functionality)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/Saransh-27/User-Management-System.git
   cd User-Management-System
   ```

2. **Configure MongoDB**
   - Install and start MongoDB
   - Create database `ums_db`
   - Update `.env` file with your MongoDB connection string

3. **Configure Email Settings**
   - Update `.env` file with your SMTP settings
   - Configure email host, port, username, and password

4. **Build and Run**
   ```bash
   # Using Maven Wrapper
   ./mvnw clean install
   ./mvnw spring-boot:run
   
   # Or using Maven
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application**
   - **Local Development**: Application runs on `http://localhost:8081`
   - **Live Backend**: `https://ums-deployment-latest.onrender.com`
   - **Live Frontend**: `https://user-mng-system.netlify.app/`
   - **API Documentation**: `http://localhost:8081/swagger-ui.html` (local) or `https://ums-deployment-latest.onrender.com/swagger-ui.html` (live)

6. **Default Admin Credentials**
   - **Username**: `Admin`
   - **Password**: `Admin@123`
   - These credentials provide full administrative access to the system

**Important Notes**:
- New user accounts must be created by the administrator
- For new user access requests, please contact the system administrator
- Email functionality is limited in the live deployment due to SMTP restrictions on Render's free tier. See the [Known Issues](#-known-issues) section for details.

---

## 🔐 Authentication & Authorization

### JWT Token Flow
1. User sends credentials to `/auth/login`
2. Server validates credentials and generates JWT
3. Client includes JWT in `Authorization: Bearer <token>` header
4. Server validates token for each protected request

### Role-Based Access Control
- **ADMIN**: Full access to all endpoints including user management and logs
- **USER**: Limited access to public endpoints and profile management
- **PUBLIC**: Access to login endpoint only

### Security Features
- Password hashing with BCrypt
- JWT token validation
- Role-based endpoint protection
- CORS configuration
- Stateless session management
- OTP-based user verification
- Activity logging for audit trails

---

## 📡 API Documentation

### Base URL
```
http://localhost:8081
```

### Interactive API Documentation
- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8081/v3/api-docs`
- **API Testing**: Built-in Swagger interface for testing all endpoints

### Endpoint Summary

| Controller | Base Path | Authentication | Description |
|------------|-----------|----------------|-------------|
| AuthController | `/auth` | Public | Login, OTP verification, and password reset |
| AdminController | `/admin` | ADMIN role | User management operations |
| PublicController | `/public` | Authenticated users | User profile management |
| LogController | `/admin/logs` | ADMIN role | Activity log management |
| ReportController | `/admin/reports` | ADMIN role | CSV reporting and analytics |
| TaskController | `/admin`, `/public` | Role-based | Task lifecycle and assignment |
| VerificationController | `/auth` | Public | Email verification endpoints |

> [!NOTE]  
> Detailed API request/response formats are available via the interactive Swagger UI.

---

## 🔐 OTP & Verification System
The system implements a secure **OTP-based User Activation** flow:
1. **Admin Creation**: Users are created with `PENDING` status.
2. **OTP Request**: User requests a 6-digit OTP via email.
3. **Verification**: User verifies OTP (5-minute expiry) to activate account.
4. **Login**: Only `ACTIVE` users can authenticate.

---

## 📊 Reporting & Analytics
Admins can export system data in **CSV format**:
- **System Reports**: Summary of users and logs.
- **User Reports**: Complete user demographics.
- **Activity Logs**: Audit trails of recent actions.

---

## 📊 Core Features & Data Models

### Data Transfer Objects (DTOs)
The application uses the **DTO Pattern** to decouple the API layer from the domain model. All requests and responses are mapped via standard DTOs (e.g., `UserRequestDto`, `TaskResponse`, `UserProfileDto`) using custom mappers.

### Core Entities
- **User**: Managed via MongoDB with fields for authentication (JWT), status (ACTIVE/PENDING), and profile info.
- **Task**: Stores task metadata, assignment links, and Base64-encoded file attachments.
- **ActivityLog**: Automated audit trail captured via AOP for all sensitive user and admin actions.

---

## 🗺️ Roadmap & Progress
- [x] JWT Authentication & OTP Verification
- [x] Multi-role Task Management with File Attachments
- [x] Profile Management with Photo Uploads
- [x] Automated Audit Logging & Log Cleanup
- [x] CSV Reporting & System Analytics
- [ ] Multi-factor Authentication (MFA)
- [ ] OAuth2 Social Login Integration

---

## 👤 Author & Support
**Saransh Dhiman**  
- GitHub: [Saransh-27](https://github.com/Saransh-27)  
- Support: Create an issue or email [saranshdhiman353@gmail.com]

## 🧪 Development & testing

```bash
mvn clean install   # Build project
mvn spring-boot:run # Run locally
mvn test            # Run tests
```

---

## 🔐 Security & Architecture
- **JWT & Stateless Auth**: Secure authentication via tokens.
- **DTO Pattern**: Separation of concerns using Data Transfer Objects.
- **Audit Logging**: Comprehensive activity tracking via Spring AOP.
- **RBAC**: Admin and User role-based authorization.

---

## 📈 Summary
The **User Management System (UMS)** is a stable, modernized Spring Boot 3 application designed for enterprise-grade user and task management. It features a rich API, secure authentication, and optimized MongoDB storage.

---

## 👤 Author & Support
**Saransh Dhiman**  
- GitHub: [Saransh-27](https://github.com/Saransh-27)  
- Support: [saranshdhiman353@gmail.com]

---

## 📚 Learning Resources

This project demonstrates:
- Spring Boot 3.x features and best practices
- MongoDB integration with Spring Data
- JWT authentication and authorization
- RESTful API design principles
- Email service integration
- Security configuration and implementation
- Clean architecture and design patterns
- Modern Java development practices
- **AOP and cross-cutting concerns**
- **Activity logging and audit trails**
- **OTP verification systems**
- **Swagger/OpenAPI documentation**
- **MongoDB Atlas optimization**

Perfect for learning:
- Enterprise Java development
- Microservices architecture
- Security implementation
- Database integration
- API development
- DevOps practices
- **Audit logging implementation**
- **Email verification workflows**
- **Performance optimization**

