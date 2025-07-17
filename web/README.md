# CareConnect Web Module (Milestone 1)

## Architecture Overview

Jakarta EE web application providing JSP-based login/registration system with PostgreSQL database integration. Serves as authentication backend for both web users and desktop application API calls.

## Module Structure

```
web/
├── src/main/java/com/bc/web/
│   ├── config/          # Database configuration
│   │   └── DatabaseConfig.java
│   ├── dao/             # Data Access Objects
│   │   ├── UserDao.java
│   │   └── EmailUtil.java
│   ├── servlets/        # HTTP request handlers
│   │   ├── LoginServlet.java
│   │   ├── RegisterServlet.java
│   │   ├── DashboardServlet.java
│   │   ├── ApiLoginServlet.java
│   │   ├── LogoutServlet.java
│   │   └── SessionTestServlet.java
│   ├── filters/         # Request/response filters
│   │   └── CharacterEncodingFilter.java
│   └── listeners/       # Application lifecycle
│       └── DatabaseInitializationListener.java
├── src/main/webapp/
│   ├── *.jsp            # JSP pages
│   ├── css/styles.css   # Styling
│   ├── Resources/       # Images, assets
│   └── WEB-INF/
│       ├── web.xml      # Servlet configuration
│       └── context.xml  # Database context
└── pom.xml
```

## Core Components

### Servlets (HTTP Request Handlers)

#### LoginServlet.java

**Purpose**: Handles user authentication for web interface

- `doGet()`: Displays login form
- `doPost()`: Processes login credentials, validates against PostgreSQL
- **Features**: Session creation, input validation, error handling
- **Security**: PreparedStatement, BCrypt verification
- **Flow**: Form → Validation → Database lookup → Session creation → Dashboard redirect

#### RegisterServlet.java

**Purpose**: Handles user registration

- `doGet()`: Displays registration form
- `doPost()`: Processes registration data, validates, stores in PostgreSQL
- **Features**: Duplicate checking, password hashing, comprehensive validation
- **Security**: Input sanitization, BCrypt hashing, SQL injection prevention
- **Flow**: Form → Validation → Duplicate check → Hash password → Database insert

#### ApiLoginServlet.java

**Purpose**: API endpoint for desktop application authentication

- `doPost()`: Processes JSON/form-encoded login requests
- **Features**: JSON response, desktop app integration
- **Security**: Same validation as web login
- **Flow**: API request → Validation → Database lookup → JSON response

#### DashboardServlet.java

**Purpose**: Protected dashboard page

- `doGet()`: Displays user dashboard after successful login
- **Features**: Session validation, user information display
- **Security**: Login requirement, session timeout handling

### Data Access Layer

#### DatabaseConfig.java

**Purpose**: Database connection management

- Connection factory for PostgreSQL
- Connection pooling configuration
- Database URL, credentials management
- **Configuration**: `jdbc:postgresql://localhost:5432/careconnect`

#### UserDao.java

**Purpose**: User data access operations

- `findByStudentNumber()`: Lookup user by student number
- `save()`: Insert new user record
- `existsByEmail()`: Check email uniqueness
- `existsByStudentNumber()`: Check student number uniqueness
- **Features**: PreparedStatement usage, result set mapping
- **Security**: SQL injection prevention, proper resource cleanup

### Configuration & Security

#### CharacterEncodingFilter.java

**Purpose**: Request/response character encoding and security headers

- UTF-8 encoding enforcement
- Security headers: X-Content-Type-Options, X-Frame-Options, X-XSS-Protection
- Content Security Policy configuration

#### DatabaseInitializationListener.java

**Purpose**: Application startup database initialization

- Database schema creation
- Connection validation
- Application lifecycle management

### JSP Pages (View Layer)

#### index.jsp

**Purpose**: Application home page

- Welcome message
- Navigation to login/register
- Basic styling and layout

#### login.jsp

**Purpose**: Login form interface

- Student number and password fields
- Client-side validation
- Error message display
- Links to registration

#### register.jsp

**Purpose**: Registration form interface

- Complete user information form
- Real-time validation feedback
- Error/success message display
- Password confirmation

#### dashboard.jsp

**Purpose**: Protected user dashboard

- Welcome message with user name
- Session information display
- Logout functionality
- Navigation menu

## Database Integration

### PostgreSQL Schema

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    student_number INTEGER UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Connection Management

- Connection per request pattern
- Proper resource cleanup (try-with-resources)
- PreparedStatement usage for all queries
- Transaction management for complex operations

## Security Features

### Authentication

- BCrypt password hashing with salt
- Session-based authentication
- Session timeout (30 minutes)
- Secure cookie configuration

### Input Validation

- Server-side validation for all inputs
- Student number format: 6 digits (000000-999999)
- Email format validation
- Password strength requirements
- Input sanitization

### Security Headers

- X-Content-Type-Options: nosniff
- X-Frame-Options: DENY
- X-XSS-Protection: 1; mode=block
- Content-Security-Policy configuration

## API Integration

### Desktop Application Support

- `/api/login` endpoint for desktop authentication
- JSON response format
- Cross-platform authentication support
- Error handling with proper HTTP status codes

### Response Format

```json
{
  "success": true,
  "user": {
    "id": 1,
    "studentNumber": 123456,
    "name": "John",
    "surname": "Doe",
    "email": "john.doe@example.com"
  }
}
```

## Dependencies

- Jakarta EE 10.0.0 (Servlets, JSP, JSTL)
- PostgreSQL 42.7.2 (Database driver)
- BCrypt 0.4 (Password hashing)
- Jackson 2.15.2 (JSON processing)
- Shared module 1.0-SNAPSHOT (Common utilities)

## Build & Deploy

```bash
# Build WAR file
mvn clean package

# Deploy to servlet container
# Copy target/web-1.0-SNAPSHOT.war to container webapps/
```

## Configuration

- Database connection: `web/src/main/java/com/bc/web/config/DatabaseConfig.java`
- Web configuration: `web/src/main/webapp/WEB-INF/web.xml`
- Database context: `web/src/main/webapp/WEB-INF/context.xml`
