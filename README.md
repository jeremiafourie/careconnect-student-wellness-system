# CareConnect - BC Student Wellness Management System

![Java](https://img.shields.io/badge/Java-17-orange) ![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue) ![Docker](https://img.shields.io/badge/Docker-Ready-blue) ![License](https://img.shields.io/badge/License-Educational-green)

## 🌟 Project Overview

CareConnect is a multi-module Maven project designed for BC Student Wellness Management consisting of:

- **🌐 Web Application** (Milestone 1): JSP-based login/registration system with PostgreSQL
- **🖥️ Desktop Application** (Milestone 2): Java Swing app that will communicate with the web app via REST API
- **📦 Shared Module**: Common models, DTOs, and utilities used by both applications

## 🚀 Live Demo

Visit the live web application at: **[https://careconnect.exequtech.com](https://careconnect.exequtech.com)**

## 🛠️ Technology Stack

- **Backend**: Jakarta EE 10, JSP/Servlets, JSTL
- **Database**: PostgreSQL 15
- **Frontend**: HTML5, CSS3 Grid/Flexbox, Vanilla JavaScript
- **Build Tool**: Maven 3.9+
- **Security**: BCrypt password hashing, PreparedStatements (SQL injection prevention)
- **Application Server**: Jetty 12 (containerized)
- **Containerization**: Docker & Docker Compose
- **CI/CD**: Woodpecker CI
- **Reverse Proxy**: Traefik (with automatic SSL)

## Project Structure

```
careconnect/
├── pom.xml                    # Parent POM
├── shared/                    # Shared module
│   ├── pom.xml
│   └── src/main/java/com/bc/shared/
│       ├── models/            # Entity models
│       ├── dto/               # Data Transfer Objects
│       └── utils/             # Utility classes
├── web/                       # Web module
│   ├── pom.xml
│   ├── src/main/java/com/bc/web/
│   │   ├── config/            # Configuration classes
│   │   ├── dao/               # Data Access Objects
│   │   ├── servlets/          # Servlet controllers
│   │   └── filters/           # Servlet filters
│   └── src/main/webapp/
│       ├── css/               # Stylesheets
│       ├── *.jsp              # JSP pages
│       └── WEB-INF/
└── desktop/                   # Desktop module (Milestone 2)
    └── pom.xml
```

## Database Setup

### 1. Install PostgreSQL

- Download and install PostgreSQL from https://www.postgresql.org/download/
- Default port: 5432
- Remember your postgres user password

### 2. Create Database

```sql
-- Connect to PostgreSQL as postgres user
psql -U postgres

-- Create database
CREATE DATABASE careconnect;

-- Switch to the database
\c careconnect;
```

### 3. Run Database Schema

Execute the SQL commands in `database_schema.sql`:

```bash
psql -U postgres -d careconnect -f database_schema.sql
```

### 4. Configure Database Connection

Update the database connection settings in:
`web/src/main/java/com/bc/web/config/DatabaseConfig.java`

```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/careconnect";
private static final String DB_USERNAME = "postgres";
private static final String DB_PASSWORD = "your_password";
```

## 🐳 Docker Deployment (Recommended)

The application is fully containerized and ready for production deployment.

### Quick Start with Docker

```bash
# Clone the repository
git clone https://github.com/jeremiafourie/careconnect.git
cd careconnect

# Set environment variables
export DOMAIN=careconnect.exequtech.com
export NETWORK=web
export TLS=true
export ENTRYPOINTS=websecure

# Create .env file
echo "ENABLE_TRAEFIK=true" > .env
echo "TLS=true" >> .env
echo "HOST_DOMAIN=$DOMAIN" >> .env
echo "ENTRYPOINTS=$ENTRYPOINTS" >> .env
echo "NETWORK=$NETWORK" >> .env
echo "NETWORK_EXTERNAL=true" >> .env

# Deploy with Docker Compose
docker compose up -d
```

### Prerequisites for Docker Deployment

- Docker Engine 20.10+
- Docker Compose V2
- Traefik reverse proxy (for SSL termination)
- External Docker network named `web`

## Features

### Authentication & Authorization

- User registration with validation
- Secure login with BCrypt password hashing
- Session management
- Input validation and sanitization

### User Management

- Student number format validation (A12345678)
- Email and phone number validation
- Password strength requirements
- Duplicate user prevention

### Security Features

- SQL injection prevention using PreparedStatements
- XSS protection
- Session timeout configuration
- Password hashing with BCrypt

### Validation Rules

- **Student Number**: Must be in format A12345678 (1 letter + 8 digits)
- **Email**: Valid email format
- **Phone**: 10 digits or international format (optional)
- **Password**: 8-100 characters, must contain uppercase, lowercase, digit, and special character
- **Names**: 2-50 characters, letters, spaces, hyphens, apostrophes only

## 🔄 CI/CD Pipeline

The project features automated deployment using Woodpecker CI.

### Pipeline Configuration

- **Trigger**: Push to `main` branch with changes to `web/`, `shared/`, `Dockerfile`, or `docker-compose.yml`
- **Build**: Multi-stage Docker build with Maven and Jetty
- **Deploy**: Automatic deployment to production server
- **Monitoring**: Health checks and deployment notifications

### Required Woodpecker Secrets

```bash
HOST_DOMAIN=careconnect.exequtech.com
TRAEFIK_ENTRYPOINTS=websecure
TRAEFIK_TLS_RESOLVER=letsencrypt
NETWORK_NAME=web
NETWORK_EXTERNAL=true
```

### Deployment Process

1. Code push triggers Woodpecker CI
2. Maven builds and tests the application
3. Docker builds multi-stage container image
4. Deployment creates `.env` file with configuration
5. Docker Compose deploys with zero-downtime

## 📊 Monitoring & Health Checks

### Health Check Endpoint

Visit `/health` for comprehensive system status:

```
✓ Database connection: SUCCESS
✓ Users table exists
✓ Users table accessible: 5 users found
```

### Application Monitoring

- **Health Checks**: Built-in health endpoint at `/health`
- **Database Monitoring**: Connection status and table accessibility
- **Environment Validation**: Configuration verification
- **Container Health**: Docker health checks every 30 seconds

## 🌐 API Endpoints

| Method | Endpoint     | Description          | Authentication |
| ------ | ------------ | -------------------- | -------------- |
| `GET`  | `/`          | Home page            | None           |
| `GET`  | `/register`  | Registration form    | None           |
| `POST` | `/register`  | Process registration | None           |
| `GET`  | `/login`     | Login form           | None           |
| `POST` | `/login`     | Process login        | None           |
| `GET`  | `/dashboard` | User dashboard       | Required       |
| `GET`  | `/logout`    | Logout               | Required       |
| `GET`  | `/health`    | System health check  | None           |

## Error Handling

- Custom 404 and 500 error pages
- Client-side form validation
- Server-side validation with user-friendly error messages
- Database connection error handling

## Configuration

### Session Configuration

- Session timeout: 30 minutes
- HTTP-only cookies enabled
- Secure cookies (set to false for development)

### Database Configuration

- Connection pooling can be added for production
- Database credentials should be externalized for production

## Production Considerations

1. **Database**

   - Use connection pooling
   - Externalize database credentials
   - Enable SSL connections

2. **Security**

   - Enable HTTPS
   - Set secure cookies to true
   - Add CSRF protection
   - Implement rate limiting

3. **Performance**

   - Add database indexes
   - Optimize queries
   - Implement caching

4. **Monitoring**
   - Add logging
   - Health checks
   - Performance monitoring

## License

This project is for educational purposes as part of the BC Student Wellness Management System coursework.

## Support

For support and questions, please refer to the project documentation or contact the development team.# CareConnect Deployment Mon 14 Jul 20:14:02 SAST 2025
