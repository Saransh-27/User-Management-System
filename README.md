# User Management System (UMS)

A comprehensive backend User Management System built with Spring Boot 3.5.10 and MongoDB, featuring JWT-based authentication, role-based authorization, email notifications, and RESTful APIs for complete user lifecycle management.

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

### Security & Authentication
- **Spring Security 6.x** - Security framework
- **JWT (JSON Web Tokens)** - Stateless authentication
- **JJWT 0.12.5** - JWT library (api, jackson, impl modules)
- **BCrypt** - Password encryption
- **Role-based Access Control** - ADMIN and USER roles

### Web & API
- **Spring Boot Starter Web** - REST API development
- **Spring Boot Starter Validation** - Input validation using Jakarta Bean Validation
- **RESTful APIs** - Complete CRUD operations

### Email & Communication
- **Spring Boot Starter Mail** - Email sending capabilities
- **JavaMailSender** - Email service implementation

### Development Tools
- **Spring Boot DevTools** - Hot reload and development utilities
- **Lombok** - Code generation and boilerplate reduction
- **Spring Boot Configuration Processor** - Configuration metadata generation

### Testing
- **Spring Boot Starter Test** - Testing framework with JUnit 5, Mockito, etc.

---

## 📂 Project Architecture

```
src/main/java/com/project/Ums/
├── UmsApplication.java              # Main Spring Boot application class
├── config/
│   └── SpringSecurity.java          # Security configuration and JWT setup
├── controller/
│   ├── AdminController.java          # Admin-only user management endpoints
│   ├── AuthController.java          # Authentication and JWT token generation
│   └── PublicController.java        # Public user endpoints
├── dto/
│   ├── LoginDto.java               # Login request DTO
│   ├── UserRequestDto.java         # User creation/update DTO with validation
│   └── UserResponseDto.java        # User response DTO
├── entity/
│   └── User.java                   # MongoDB User entity with roles
├── filter/
│   └── JwtFilter.java              # JWT authentication filter
├── repository/
│   └── UserRepository.java         # MongoDB repository interface
├── service/
│   ├── AdminService.java           # Admin business logic
│   ├── EmailService.java           # Email notification service
│   └── UserDetailServiceImpl.java  # User details service for Spring Security
└── utils/
    └── JwtUtil.java                # JWT token generation and validation utilities

src/main/resources/
├── application.yaml                # Application configuration (gitignored)
├── static/                         # Static resources
└── templates/                      # Template files
```

---

## ⚙️ Configuration

### Application Configuration
The application uses `application.yaml` for configuration (gitignored for security):

- **Database**: MongoDB connection settings
- **Email**: SMTP configuration for email notifications
- **JWT**: Secret key and token expiration settings
- **Server**: Port and other server configurations

### Security Configuration
- **JWT Secret**: Custom signing key for token validation
- **Token Expiration**: 1 hour (3600 seconds)
- **Password Encoding**: BCrypt with default strength
- **CORS**: Configured for cross-origin requests
- **Session Management**: Stateless (JWT-based)

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
   - Default admin user needs to be created via database

---

## 🔐 Authentication & Authorization

### JWT Token Flow
1. User sends credentials to `/auth/login`
2. Server validates credentials and generates JWT
3. Client includes JWT in `Authorization: Bearer <token>` header
4. Server validates token for each protected request

### Role-Based Access Control
- **ADMIN**: Full access to all endpoints including user management
- **USER**: Limited access to public endpoints
- **PUBLIC**: Access to login endpoint only

### Security Features
- Password hashing with BCrypt
- JWT token validation
- Role-based endpoint protection
- CORS configuration
- Stateless session management

---

## 📡 API Documentation

### Base URL
```
http://localhost:8081
```

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

### Public Endpoints (Requires authentication)

#### Get Current User Profile
```http
GET /public/profile
Authorization: Bearer <jwt-token>
```

#### Update User Profile
```http
PUT /public/update
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "userName": "updated_username",
  "email": "updated@example.com"
}
```

---

## 📊 Data Models

### User Entity
```json
{
  "id": "string (MongoDB ObjectId)",
  "userName": "string (unique)",
  "email": "string (unique, validated)",
  "password": "string (BCrypt hashed)",
  "roles": ["string array (ROLE_USER, ROLE_ADMIN)"]
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

---

## 📧 Email Features

### Welcome Email Service
- Automatically sends welcome emails when new users are created
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
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### Test Coverage
- Unit tests for service layer
- Integration tests for repositories
- Security configuration tests
- API endpoint tests

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

---

## 📈 Performance Considerations

- MongoDB indexing on `userName` and `email` fields
- Connection pooling for database
- Lazy loading for collections
- Efficient JWT token validation
- Proper exception handling to prevent resource leaks

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
- Email: [your-email@example.com]
- Documentation: Check the API documentation above

---

## 🗺️ Roadmap

### Upcoming Features
- [ ] Password reset functionality
- [ ] Account email verification
- [ ] User profile image upload
- [ ] Audit logging for user actions
- [ ] Rate limiting for API endpoints
- [ ] Multi-factor authentication (MFA)
- [ ] OAuth2 integration (Google, GitHub)
- [ ] Advanced user search and filtering
- [ ] Bulk user operations
- [ ] User activity dashboard
- [ ] API documentation with Swagger/OpenAPI
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

Perfect for learning:
- Enterprise Java development
- Microservices architecture
- Security implementation
- Database integration
- API development
- DevOps practices

