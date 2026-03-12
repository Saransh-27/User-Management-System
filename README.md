# User Management System (UMS)

A comprehensive backend User Management System built with Spring Boot 3.5.10 and MongoDB, featuring JWT-based authentication, role-based authorization, email notifications with OTP verification, comprehensive activity logging, and RESTful APIs for complete user lifecycle management.

---

## 🚀 Tech Stack

### Core Framework
- **Java 17** - Programming language
- **Spring Boot 3.5.10** - Main application framework
- **Maven** - Build and dependency management
- **Spring AI 1.1.2** - AI integration capabilities

### Database & Persistence
- **MongoDB** - NoSQL database for data storage
- **Spring Data MongoDB** - MongoDB integration and repository support
- **Spring Boot Starter Data MongoDB** - MongoDB auto-configuration
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
- **SMTP Integration** - Configurable email server support

### Development Tools
- **Spring Boot DevTools** - Hot reload and development utilities
- **Lombok** - Code generation and boilerplate reduction
- **Spring Boot Configuration Processor** - Configuration metadata generation
- **Spring Boot Starter AOP** - Aspect-Oriented Programming for logging

### API Documentation
- **SpringDoc OpenAPI 3** - Interactive API documentation
- **Swagger UI** - API testing and exploration interface
- **OpenAPI Specification** - Standard API documentation format

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
│   ├── SpringSecurity.java          # Security configuration and JWT setup
│   └── SwaggerConfig.java           # Swagger configuration and API documentation
├── controller/
│   ├── AdminController.java          # Admin-only user management endpoints
│   ├── AuthController.java          # Authentication and JWT token generation
│   ├── LogController.java          # Activity log management endpoints
│   └── PublicController.java        # Public user endpoints
├── dto/
│   ├── LoginDto.java               # Login request DTO
│   ├── UserProfileDto.java         # User profile management DTO
│   ├── UserRequestDto.java         # User creation/update DTO with validation
│   └── UserResponseDto.java        # User response DTO
├── entity/
│   └── User.java                   # MongoDB User entity with roles and OTP
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
│   └── UserRepository.java         # MongoDB repository interface
├── service/
│   ├── AdminService.java           # Admin business logic
│   ├── EmailService.java           # Email notification service
│   ├── LogCleanupService.java      # Automated log cleanup service
│   ├── OtpService.java             # OTP generation and verification
│   └── UserDetailServiceImpl.java  # User details service for Spring Security
└── utils/
    └── JwtUtil.java                # JWT token generation and validation utilities

src/main/resources/
├── application.yaml                # Application configuration (gitignored)
├── static/                         # Static resources
└── templates/                      # Template files

src/test/java/com/project/Ums/
├── UmsApplicationTests.java        # Main application test class
└── service/                        # Service layer tests
```

---

## ⚙️ Configuration

### Application Configuration
The application uses `application.yaml` for configuration (gitignored for security):

- **Database**: MongoDB connection settings with database name
- **Email**: SMTP configuration for email notifications
- **JWT**: Secret key and token expiration settings
- **Server**: Port configuration (default: 8081)
- **Logging**: Activity log retention and cleanup settings
- **OTP**: One-time password configuration for user verification
- **SpringDoc**: OpenAPI documentation configuration

### Key Configuration Sections
```yaml
server:
  port: 8081

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/ums_db
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password

jwt:
  secret: your-secret-key
  expiration: 3600000  # 1 hour

logging:
  retention:
    days: 90
  cleanup:
    batch-size: 1000

otp:
  length: 6
  expiry-minutes: 5
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
   - Update `application.yaml` with your MongoDB connection string

3. **Configure Email Settings**
   - Update `application.yaml` with your SMTP settings
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
   - Application runs on `http://localhost:8081`
   - API Documentation: `http://localhost:8081/swagger-ui.html`
   - Default admin user needs to be created via database
   - New users require OTP verification for activation

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
| AuthController | `/auth` | Public | Login and OTP verification |
| AdminController | `/admin` | ADMIN role | User management operations |
| PublicController | `/public` | Authenticated users | User profile management |
| LogController | `/admin/logs` | ADMIN role | Activity log management |

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
  "password": "new_password123"
}
```

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
POST /auth/request-otp?id={id}&email={email}
```

**Parameters:**
- `id` (path param): User ID
- `email` (path param): User email address

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
  "status": "string (ACTIVE, INACTIVE, indexed)",
  "otp": "string (6-digit, indexed, for verification)",
  "otpExpiry": "LocalDateTime (OTP expiration time)"
}
```

### DTOs

#### UserRequestDto
- `id`: String (optional for updates)
- `userName`: String (required, @NotBlank)
- `email`: String (required, @Email, @NotBlank)
- `password`: String (required, @NotBlank)
- `roles`: List<String> (default: empty)

#### LoginDto
- `userName`: String
- `password`: String

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

## 📊 Activity Logging & Audit

### Automatic Activity Logging
- **AOP-Based Logging**: Automatic logging using Spring AOP
- **@LogActivity Annotation**: Custom annotation for logging specific methods
- **Comprehensive Tracking**: Logs user actions, IP addresses, timestamps
- **Success/Failure Tracking**: Records both successful and failed operations

### Log Management Features
- **MongoDB Storage**: Logs stored in dedicated `activity_logs` collection
- **Automatic Indexing**: Optimized indexes for efficient querying
- **Paginated Retrieval**: Efficient log browsing with pagination
- **User-Specific Logs**: Filter logs by username
- **Recent Activity**: Quick access to latest 50 activities

### Log Cleanup Service
- **Automated Cleanup**: Scheduled removal of old logs
- **Configurable Retention**: Default 90-day retention period
- **Batch Processing**: Efficient bulk deletion operations
- **MongoDB Atlas Optimization**: Optimized for Atlas performance
- **Manual Cleanup**: Admin-triggered cleanup available

### Log Configuration
```yaml
logging:
  retention:
    days: 90
  cleanup:
    batch-size: 1000
```

---

## 📧 Email Features

### Email Services
- **Registration Email**: Sends OTP for user verification
- **Welcome Email**: Sends login credentials after successful verification
- **Password Notifications**: Email notifications for password changes

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
Configure in `application.yaml`:
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
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
- `LOGGING_RETENTION_DAYS` - Log retention period (default: 90)
- `LOGGING_CLEANUP_BATCH_SIZE` - Log cleanup batch size (default: 1000)

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

### Upcoming Features
- [ ] Password reset functionality
- [ ] Account email verification improvements
- [ ] User profile image upload
- [ ] Rate limiting for API endpoints
- [ ] Multi-factor authentication (MFA)
- [ ] OAuth2 integration (Google, GitHub)
- [ ] Advanced user search and filtering
- [ ] Bulk user operations
- [ ] User activity dashboard
- [ ] GraphQL API support
- [ ] Redis caching for improved performance
- [ ] Microservices architecture migration

### Technical Improvements
- [ ] Comprehensive unit and integration tests
- [ ] CI/CD pipeline setup
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

