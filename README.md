# User Management System (UMS)

A comprehensive backend User Management System built with Spring Boot 3.5.10 and MongoDB, featuring JWT-based authentication, role-based authorization, email notifications with OTP verification, comprehensive activity logging, and RESTful APIs for complete user lifecycle management.

---

## 🚀 Tech Stack

### Core Framework
- **Java 17** - Programming language
- **Spring Boot 3.5.10** - Main application framework
- **Maven** - Build and dependency management
- **Spring AI 1.1.2** - AI integration capabilities (configured for future use)

### Database & Persistence
- **MongoDB** - NoSQL database for data storage
- **Spring Data MongoDB** - MongoDB integration and repository support
- **MongoDB Atlas Compatible** - Optimized for cloud deployment
- **Indexed Fields**: userName, email, status, otp for optimized queries
- **MongoDB Indexing** - Optimized indexes on userName, email, status, and otp fields

### Security & Authentication
- **Spring Security 6.x** - Security framework
- **JWT (JSON Web Tokens)** - Stateless authentication
- **JJWT 0.12.5** - JWT library (api, jackson, impl modules)
- **BCrypt** - Password encryption
- **Role-based Access Control** - ADMIN and USER roles
- **OTP Verification** - 6-digit one-time password system

### Web & API
- **Spring Boot Starter Web** - REST API development
- **Spring Boot Starter Validation** - Input validation using Jakarta Bean Validation
- **RESTful APIs** - Complete CRUD operations
- **Swagger/OpenAPI 3.0** - Interactive API documentation

### Email & Communication
- **Spring Boot Starter Mail** - Email sending capabilities
- **JavaMailSender** - Email service implementation
- **SMTP Integration** - Gmail and other SMTP providers support
- **Template-based Emails** - Structured email templates for user communications
- **SMTP Integration** - Configurable email server support

### Development Tools
- **Spring Boot DevTools** - Hot reload and development utilities
- **Lombok** - Code generation and boilerplate reduction
- **Spring Boot Configuration Processor** - Configuration metadata generation
- **Spring Boot Starter AOP** - Aspect-Oriented Programming for logging

### API Documentation
- **SpringDoc OpenAPI 3** - Interactive API documentation (version 2.8.15)
- **Swagger UI** - API testing and exploration interface
- **OpenAPI Specification** - Standard API documentation format
- **Security Documentation** - JWT bearer authentication support

### Testing
- **Spring Boot Starter Test** - Testing framework with JUnit 5, Mockito, etc.
- **JUnit Jupiter** - Modern testing framework
- **Test Coverage** - Unit and integration testing support

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
│   └── UserMapper.java             # Entity-DTO mapping utilities
├── repository/
│   ├── PasswordResetTokenRepository.java # Password reset token repository
│   └── UserRepository.java         # MongoDB repository interface
├── service/
│   ├── AdminService.java           # Admin business logic
│   ├── EmailService.java           # Email notification service
│   ├── LogCleanupService.java      # Automated log cleanup service
│   ├── OtpService.java             # OTP generation and verification
│   ├── PasswordResetService.java  # Complete password reset service
│   ├── UserDetailServiceImpl.java  # User details service for Spring Security
│   └── VerificationService.java    # Email verification token service
└── utils/
    └── JwtUtil.java                # JWT token generation and validation utilities

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
| VerificationController | `/auth` | Public | Email verification endpoints |

### Authentication Endpoints

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "userName": "admin",
  "password": "password123"
}
```

**Response:**
```json
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.signature"
```

**Error Responses:**
- `400 Bad Request` - Invalid credentials
- `403 Forbidden` - User not verified (INACTIVE status)
- `404 Not Found` - User not found
- `500 Internal Server Error` - Server error

### Admin Endpoints (Requires ADMIN role)

#### Create User
```http
POST /admin/add
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "userName": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "roles": ["USER"]
}
```

#### Search Users
```http
GET /admin/search?query=john&searchType=all
Authorization: Bearer <jwt-token>
```

**Parameters:**
- `query` (required): Search term - can be name, email, or user ID
- `searchType` (optional): Search type - `name`, `email`, or `all` (default)

#### Get All Users
```http
GET /admin/all
Authorization: Bearer <jwt-token>
```

#### Get User by ID
```http
GET /admin/User/{id}
Authorization: Bearer <jwt-token>
```

#### Delete User
```http
DELETE /admin/delete-user/{id}
Authorization: Bearer <jwt-token>
```

### Activity Log Endpoints (Requires ADMIN role)

#### Get All Activity Logs
```http
GET /admin/logs?page=0&size=20&sortBy=timestamp&sortDir=desc
Authorization: Bearer <jwt-token>
```

#### Get Logs by Username
```http
GET /admin/logs/user/{username}?page=0&size=20
Authorization: Bearer <jwt-token>
```

#### Get Recent Logs
```http
GET /admin/logs/recent
Authorization: Bearer <jwt-token>
```

#### Get Log Statistics
```http
GET /admin/logs/stats
Authorization: Bearer <jwt-token>
```

#### Trigger Log Cleanup
```http
POST /admin/logs/cleanup
Authorization: Bearer <jwt-token>
```

### Public Endpoints (Requires authentication)

#### Get Current User Profile
```http
GET /public/view-profile
Authorization: Bearer <jwt-token>
```

#### Update User Profile
```http
PUT /public/update-user
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "userName": "updated_username",
  "email": "updated@example.com",
  "password": "new_password123",
  "currentPassword": "current_password123"
}
```

#### Change Password
```http
PUT /public/change-password
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "currentPassword": "current_password123",
  "newPassword": "new_password456"
}
```

#### Upload Profile Photo
```http
POST /public/upload-profile-photo
Authorization: Bearer <jwt-token>
Content-Type: multipart/form-data

file: [image file (max 5MB, image/* only)]
```

**Response:** User profile with updated photo URL

#### Get Profile Photo
```http
GET /public/profile-photos/{filename}
```

**Response:** Image file with appropriate Content-Type header

#### Get My Activity Logs
```http
GET /public/my-logs?page=0&size=20
Authorization: Bearer <jwt-token>
```

**Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

#### Delete User Account
```http
DELETE /public/delete-user
Authorization: Bearer <jwt-token>
```

### OTP Verification Endpoints

#### Verify OTP
```http
POST /auth/verify-otp?id={id}&email={email}&otp={otp}
```

**Parameters:**
- `id` (path param): User ID
- `email` (path param): User email address  
- `otp` (path param): One-Time Password sent to email

#### Request OTP
```http
POST /auth/resend-otp?id={id}&email={email}
```

**Parameters:**
- `id` (path param): User ID
- `email` (path param): User email address

### Password Reset Endpoints

#### Forgot Password
```http
POST /auth/forgot-password
Content-Type: application/json

{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "message": "Password reset link has been sent to your email address"
}
```

**Process:**
- Validates email exists in database
- Generates unique UUID token
- Invalidates any existing tokens for the user
- Sends password reset email with token
- Token expires in 1 hour

#### Reset Password
```http
POST /auth/reset-password
Content-Type: application/json

{
  "token": "uuid-token-here",
  "newPassword": "newSecurePassword123"
}
```

**Response:**
```json
{
  "message": "Password has been reset successfully"
}
```

**Process:**
- Validates token exists and is unused
- Checks token hasn't expired
- Updates user password with BCrypt encryption
- Marks token as used
- Cleans up any other tokens for the user

#### Validate Reset Token
```http
GET /auth/validate-reset-token?token={token}
```

**Response:**
```json
{
  "valid": true
}
```

---

## 📊 Reporting & Analytics

### CSV Report Generation
The system provides comprehensive reporting capabilities with CSV export functionality.

#### System Report
```http
GET /admin/reports/system
Authorization: Bearer <jwt-token>
```

**Includes:**
- System summary with user statistics
- All users with roles and status
- Recent 500 activity logs
- Generated timestamp

#### Users Report
```http
GET /admin/reports/users
Authorization: Bearer <jwt-token>
```

**Includes:**
- Complete user list
- User details: ID, username, email, roles, status, creation date

#### Activity Logs Report
```http
GET /admin/reports/activity-logs
Authorization: Bearer <jwt-token>
```

**Includes:**
- Recent 1000 activity logs
- Log details: username, action, description, timestamp, success status, IP address, user agent

**Response Format:**
- Content-Type: `text/csv`
- Content-Disposition: `attachment; filename="UMS_Report_YYYYMMDD_HHMMSS.csv"`
- Proper CSV escaping for commas, quotes, and newlines

---

## 🔐 OTP Verification System

### Complete User Activation Workflow

#### Step 1: User Creation (Admin)
1. Admin creates user via `/admin/add` endpoint
2. User is created with `status: PENDING`
3. Account creation email sent to user's email address
4. User receives notification to request OTP verification

#### Step 2: OTP Request (User)
```http
POST /auth/request-otp?id={userId}&email={userEmail}
```

**Validation:**
- User must exist with provided ID and email combination
- User status must be `PENDING` (already verified users rejected)
- 6-digit OTP generated and stored in database
- OTP expiry set to 5 minutes from generation time
- OTP email sent to user's registered email address

#### Step 3: OTP Verification (User)
```http
POST /auth/verify-otp?id={userId}&email={userEmail}&otp={6-digit-code}
```

**Verification Process:**
- Validate user exists with provided ID and email
- Check user status is `PENDING`
- Verify OTP exists and hasn't been used
- Validate OTP hasn't expired (5-minute window)
- Match provided OTP with stored OTP
- On success: Update status to `ACTIVE`, clear OTP fields
- Send welcome email with login credentials

#### Step 4: Login (User)
```http
POST /auth/login
Content-Type: application/json

{
  "userName": "username",
  "password": "password"
}
```

**Login Requirements:**
- User status must be `ACTIVE`
- Valid credentials required
- JWT token returned on successful authentication

### OTP Security Features
- **6-digit Secure OTP**: Generated using `SecureRandom`
- **5-minute Expiry**: Automatic OTP expiration
- **One-time Use**: OTP cleared after successful verification
- **Email Delivery**: Sent via configured SMTP server
- **Failed Attempts**: Invalid OTP rejected with appropriate message
- **Account Status Tracking**: Clear PENDING → ACTIVE flow

### Email Templates

#### Account Creation Email
- Subject: "Account Created - Verification Required"
- Content: User ID, email, username, verification instructions

#### OTP Email
- Subject: "OTP Verification - User Management System"
- Content: 6-digit OTP, validity period, security notice

#### Welcome Email
- Subject: "Welcome to User Management System!"
- Content: Login credentials, user ID, system access information

---

## 📊 Data Models

### User Entity
```json
{
  "id": "string (MongoDB ObjectId)",
  "userName": "string (unique, indexed)",
  "email": "string (unique, validated)",
  "password": "string (BCrypt hashed)",
  "roles": ["string array (ROLE_USER, ROLE_ADMIN)"],
  "status": "string (PENDING, ACTIVE, indexed)",
  "otp": "string (6-digit, indexed, for verification)",
  "otpExpiry": "LocalDateTime (OTP expiration time)",
  "profilePhoto": "string (path to uploaded profile photo)",
  "createdAt": "LocalDateTime (account creation timestamp)"
}
```

### PasswordResetToken Entity
```json
{
  "id": "string (MongoDB ObjectId)",
  "token": "string (unique UUID token)",
  "userId": "string (associated user ID)",
  "expiryDate": "LocalDateTime (token expiration - 1 hour)",
  "used": "boolean (token usage status)",
  "createdAt": "LocalDateTime (token creation timestamp)"
}
```

### VerificationToken Entity
```json
{
  "id": "string (MongoDB ObjectId)",
  "token": "string (unique verification token)",
  "userEmail": "string (associated user email)",
  "expiryDate": "LocalDateTime (token expiration)",
  "verified": "boolean (verification status)",
  "createdAt": "LocalDateTime (token creation timestamp)"
}
```

### DTOs

#### ApiResponse
- `message`: String (response message)
- `status`: Integer (HTTP status code)
- `data`: T (generic response data)
- `timestamp`: LocalDateTime (response timestamp)
- `success`: Boolean (operation success status)

#### ErrorResponse
- `message`: String (error message)
- `status`: Integer (HTTP status code)
- `error`: String (error type)
- `timestamp`: LocalDateTime (error timestamp)
- `errors`: Map<String, String> (validation errors)

#### ForgotPasswordDto
- `email`: String (required, @Email, @NotBlank)

#### LoginDto
- `userName`: String
- `password`: String

#### LoginResponse
- `token`: String (JWT authentication token)
- `user`: UserProfileDto (authenticated user details)

#### ResetPasswordDto
- `token`: String (required, @NotBlank)
- `newPassword`: String (required, @Size min=6)

#### UserRequestDto
- `id`: String (optional for updates)
- `userName`: String (required, @NotBlank)
- `email`: String (required, @Email, @NotBlank)
- `password`: String (required, @NotBlank)
- `roles`: List<String> (default: empty)

#### UserResponseDto
- `id`: String
- `userName`: String
- `email`: String
- `roles`: List<String>

#### UserProfileDto
- `id`: String
- `userName`: String
- `email`: String
- `password`: String
- `roles`: List<String>
- `status`: String
- `profilePhoto`: String (path to profile photo)
- `createdAt`: LocalDateTime

#### UserUpdateDto
- `userName`: String (optional)
- `email`: String (optional)
- `password`: String (optional)
- `currentPassword`: String (required when updating password)

#### VerificationResponse
- `message`: String (verification result message)
- `success`: Boolean (verification status)

### Activity Log Entity
```json
{
  "id": "string (MongoDB ObjectId)",
  "userId": "string (user ID who performed action)",
  "username": "string (username of user)",
  "action": "string (type of action: LOGIN, CREATE, UPDATE, DELETE)",
  "methodName": "string (method name called)",
  "description": "string (action description)",
  "ipAddress": "string (client IP address)",
  "userAgent": "string (client user agent)",
  "success": "boolean (action success status)",
  "errorMessage": "string (error message if failed)",
  "timestamp": "Instant (when action occurred)"
}
```

### MongoDB Indexes
The application uses optimized indexes for performance:
- **Users Collection**: userName, email, status, otp
- **Activity Logs Collection**: userId, username, timestamp, action
- **Password Reset Tokens Collection**: token (unique), userId
- **Verification Tokens Collection**: token (unique), userEmail

## 📊 Activity Logging & Audit

### AOP-Based Logging System
The application implements comprehensive activity logging using Spring AOP:

#### @LogActivity Annotation
```java
@LogActivity(action="LOGIN", description="User login attempt")
@PostMapping("/login")
public String login(@RequestBody LoginDto dto) {
    // Method implementation
}
```

#### Automatic Log Capture
- **User Identification**: Extracts user details from SecurityContext
- **Request Context**: Captures IP address and User-Agent headers
- **Method Details**: Logs method names and custom descriptions
- **Success/Failure Tracking**: Automatic exception handling and error logging
- **Timestamp**: Precise Instant-based timestamp for each action

#### Logged Actions
- **LOGIN**: User authentication attempts
- **VERIFY_OTP**: Email verification via OTP
- **REQUEST_OTP**: User requested OTP verification
- **CREATE_USER**: Admin created new user
- **VIEW_ALL_USERS**: Retrieved all users list
- **VIEW_USER**: Retrieved specific user details
- **DELETE_USER**: User account deletion
- **UPDATE_PROFILE**: User profile updates

### Log Management Features

#### MongoDB Storage Schema
```json
{
  "id": "MongoDB ObjectId",
  "userId": "string (user ID who performed action)",
  "username": "string (username of user)",
  "action": "string (type of action)",
  "methodName": "string (method name called)",
  "description": "string (action description)",
  "ipAddress": "string (client IP address)",
  "userAgent": "string (client user agent)",
  "success": "boolean (action success status)",
  "errorMessage": "string (error message if failed)",
  "timestamp": "Instant (when action occurred)"
}
```

#### Query Optimization
- **Compound Indexes**: userId + timestamp for efficient user-specific queries
- **Action Index**: Fast filtering by action types
- **Username Index**: Quick user activity lookups
- **Timestamp Index**: Efficient chronological sorting

#### Admin Log Management Endpoints
- `GET /admin/logs` - Paginated log retrieval with sorting
- `GET /admin/logs/user/{username}` - User-specific activity logs
- `GET /admin/logs/recent` - Latest 50 activities
- `GET /admin/logs/stats` - Log statistics and metrics
- `POST /admin/logs/cleanup` - Manual log cleanup trigger

### Log Cleanup Service

#### Automated Cleanup
- **Scheduled Service**: Automatic cleanup of old logs
- **Configurable Retention**: 90-day default retention period
- **Batch Processing**: 1000-record batches for efficient deletion
- **MongoDB Atlas Optimized**: Bulk operations for cloud performance

#### Cleanup Configuration
```yaml
activity-log:
  retention:
    days: 90
  cleanup:
    batch-size: 1000
    enabled: true
```

#### Performance Considerations
- **Bulk Operations**: Uses MongoDB bulk write operations
- **Index Utilization**: Optimized query plans for cleanup
- **Memory Efficiency**: Streaming result processing
- **Error Handling**: Comprehensive logging of cleanup operations

---

## 📧 Email Features

### Email Services
- **Registration Email**: Sends OTP for user verification
- **Welcome Email**: Sends login credentials after successful verification
- **Password Notifications**: Email notifications for password changes
- **Password Reset Emails**: Sends secure reset tokens with expiry information

### Password Reset Email System
- **Secure Token Generation**: UUID-based tokens with 1-hour expiry
- **Email Delivery**: Reset instructions sent via configured SMTP
- **Security Features**: Token invalidation after use, automatic cleanup
- **User-Friendly**: Direct reset links and clear instructions

### OTP Verification System
- **6-digit OTP**: Secure one-time password generation
- **5-minute Expiry**: Automatic OTP expiration for security
- **Email Delivery**: OTP sent via configured SMTP server
- **Account Activation**: Users activated only after OTP verification

### Welcome Email Service
- Automatically sends welcome emails when new users are verified
- Includes username and password in the email body
- Uses JavaMailSender with SMTP configuration

### Email Configuration
Configure in `.env` file:
```env
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password
```

---

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UmsApplicationTests

# Run with coverage
mvn clean test jacoco:report

# Run tests with specific profile
mvn test -Dspring.profiles.active=test
```

### Test Structure
- **UmsApplicationTests.java** - Main application context test
- **Service Tests** - Located in `src/test/java/com/project/Ums/service/`
- **Integration Tests** - Repository and controller testing
- **Security Tests** - Authentication and authorization testing

### Test Coverage Areas
- Unit tests for service layer business logic
- Integration tests for MongoDB repositories
- Security configuration tests for JWT and role-based access
- API endpoint tests for all controllers
- Email service testing with mock configurations
- OTP service testing for generation and validation
- Activity logging tests for AOP functionality

### Testing Dependencies
- **Spring Boot Starter Test** - Core testing framework
- **JUnit Jupiter** - Modern testing framework
- **JUnit Jupiter Params** - Parameterized tests
- **Mockito** - Mocking framework for unit tests
- **TestContainers** - Integration testing with real MongoDB (if configured)

---

## 🔧 Development

### Hot Reload
Spring Boot DevTools provides automatic restart on code changes.

### Lombok Integration
Project uses Lombok annotations:
- `@Data` - Getters, setters, toString, equals, hashCode
- `@Builder` - Builder pattern
- `@NoArgsConstructor` - Default constructor
- `@AllArgsConstructor` - All arguments constructor
- `@RequiredArgsConstructor` - Required arguments constructor

### Code Quality
- Jakarta Bean Validation for input validation
- Custom exception handling
- Proper logging with SLF4J
- Clean architecture with separation of concerns
- AOP for cross-cutting concerns (logging)
- DTO pattern for API data transfer
- Repository pattern for data access

---

## 📝 Error Handling

### HTTP Status Codes
- `200 OK` - Successful operation
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid input data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Error Response Format
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for object='userRequestDto'",
  "path": "/admin/add"
}
```

---

## ⚠️ Known Issues

### EmailService SMTP Configuration on Render

**Issue**: The EmailService functionality works perfectly in local development environments but encounters SMTP connectivity issues when deployed on Render cloud platform.

**Root Cause**: 
- Render's free tier and certain network configurations block outbound SMTP traffic on standard ports (587, 465, 25)
- This is a common limitation among cloud providers to prevent spam and abuse
- Local development environments typically don't have these restrictions

**Affected Features**:
- User registration emails with OTP verification
- Welcome emails after account activation
- Password reset emails
- Account verification notifications

**Current Status**:
- ✅ **Local Development**: All email features work perfectly with configured SMTP
- ❌ **Render Deployment**: SMTP connections are blocked, causing email failures

**Recommended Solutions**:

1. **Use Render's Paid Tier**: Upgrade to a paid plan that allows outbound SMTP traffic
2. **Alternative Email Services**: Consider using cloud email APIs that work within Render's restrictions:
   - SendGrid (HTTP API-based)
   - Mailgun (HTTP API-based) 
   - AWS SES (with proper configuration)
3. **Environment-Specific Configuration**: Implement conditional email service activation based on deployment environment

**Technical Note**: The EmailService code is implemented correctly and follows best practices. The issue is purely infrastructure-related to Render's network policies, not a code problem.

---

## 🚀 Deployment

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/Ums-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables
- `MONGODB_URI` - MongoDB connection string
- `SPRING_MAIL_HOST` - SMTP server host
- `SPRING_MAIL_PORT` - SMTP server port
- `SPRING_MAIL_USERNAME` - SMTP username
- `SPRING_MAIL_PASSWORD` - SMTP password
- `JWT_SECRET` - JWT signing secret
- `JWT_EXPIRATION` - JWT token expiration time in milliseconds
- `SERVER_PORT` - Application server port
- `LOGGING_LEVEL_COM_PROJECT_UMS` - Logging level for application
- `LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY` - Logging level for security
- `LOG_FILE_NAME` - Log file path
- `ACTIVITY_LOG_RETENTION_DAYS` - Log retention period (default: 90)
- `ACTIVITY_LOG_CLEANUP_BATCH_SIZE` - Log cleanup batch size (default: 1000)
- `ACTIVITY_LOG_CLEANUP_ENABLED` - Enable/disable log cleanup (default: true)

---

## 🔄 API Versioning

Current version uses base paths without versioning. Future versions can implement:
- `/api/v1/auth/login`
- `/api/v1/admin/add`
- `/api/v1/public/profile`

---

## 🛡️ Security Best Practices

1. **Password Security**: BCrypt hashing with salt
2. **JWT Security**: Strong secret key, token expiration
3. **Input Validation**: Jakarta Bean Validation
4. **CORS Configuration**: Proper cross-origin setup
5. **Role-Based Access**: Principle of least privilege
6. **Stateless Authentication**: JWT-based sessions
7. **Environment Variables**: Sensitive data in configuration
8. **OTP Verification**: Two-factor authentication for user activation
9. **Activity Logging**: Complete audit trail for security monitoring
10. **IP Address Tracking**: Monitor access patterns and detect anomalies

---

## 📈 Performance Considerations

### Database Optimizations
- **MongoDB Indexing**: Optimized indexes on `userName`, `email`, `status`, and `otp` fields
- **Activity Log Indexing**: Compound indexes for efficient log queries
- **Connection Pooling**: MongoDB connection pooling for optimal performance
- **Lazy Loading**: Efficient data loading strategies

### Application Performance
- **JWT Token Validation**: Efficient token validation with minimal overhead
- **AOP Logging**: Optimized aspect-oriented programming for logging
- **Batch Operations**: Efficient bulk operations for log cleanup
- **Memory Management**: Proper exception handling to prevent resource leaks

### Log Management Performance
- **Batch Log Cleanup**: Efficient bulk deletion operations (1000 records per batch)
- **Pagination**: Memory-efficient log retrieval with configurable page sizes
- **Scheduled Cleanup**: Automated log cleanup to maintain database performance
- **Index Optimization**: MongoDB Atlas-optimized indexes for log queries

### Caching Strategies
- **JWT Token Caching**: In-memory token validation cache (if implemented)
- **User Data Caching**: Potential for Redis integration (future enhancement)
- **Configuration Caching**: Spring Boot configuration caching

### Monitoring & Metrics
- **Activity Log Statistics**: Real-time log analytics
- **Performance Metrics**: Request/response time tracking
- **Database Performance**: Query optimization and monitoring

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👤 Author

**Saransh Dhiman**
- GitHub: [Saransh-27](https://github.com/Saransh-27)
- Project: User Management System

---

## 📞 Support

For support and queries:
- Create an issue in the GitHub repository
- Email: [saranshdhiman353@gmail.com]
- Documentation: Check the API documentation above

---

## 🗺️ Roadmap

### ✅ Completed Features
- [x] JWT-based authentication system
- [x] Role-based access control (ADMIN/USER)
- [x] Email notifications and OTP verification
- [x] Activity logging with AOP
- [x] Automated log cleanup service
- [x] Swagger/OpenAPI documentation
- [x] User status management (ACTIVE/INACTIVE)
- [x] MongoDB Atlas optimization
- [x] Comprehensive audit trails
- [x] **Profile photo upload system**
- [x] **Advanced user search functionality**
- [x] **CSV reporting and analytics**
- [x] **Password change with current password verification**
- [x] **Email verification token system**
- [x] **User activity self-service logs**
- [x] **Secure file upload with validation**
- [x] **Complete password reset functionality with token-based email verification**
- [x] **Account email verification improvements**
- [x] **Advanced user search with filters and pagination**
- [x] **User activity dashboard**
- [x] **Real-time notifications system**
- [x] **Data export in multiple formats (JSON, XML, PDF)**
- [x] **CI/CD pipeline setup**
- [x] **Docker image deployment**

### Upcoming Features
- [ ] **Enhanced profile management (bio, social links, etc.)**
- [ ] Rate limiting for API endpoints
- [ ] Multi-factor authentication (MFA)
- [ ] OAuth2 integration (Google, GitHub)
- [ ] Bulk user operations
- [ ] GraphQL API support
- [ ] Redis caching for improved performance
- [ ] Microservices architecture migration

### Technical Improvements
- [ ] Comprehensive unit and integration tests
- [ ] Container orchestration with Kubernetes
- [ ] Monitoring and logging with ELK stack
- [ ] Performance optimization and load testing
- [ ] Security scanning and vulnerability assessment

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

