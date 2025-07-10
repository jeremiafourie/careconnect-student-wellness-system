# CareConnect Web Application (Milestone 1)

## 📋 Overview

The web application serves as the authentication gateway and user registration portal for the CareConnect system. Built using JSP/Servlets with PostgreSQL database integration.

**Grade Weight**: 20 marks (40% of total project)

## 🎯 Features

- User registration with comprehensive validation
- Secure user authentication
- Session management
- Responsive web interface
- Integration-ready for desktop application

## 🛠️ Technology Stack

- **Frontend**: JSP, HTML5, CSS3, JavaScript
- **Backend**: Java Servlets
- **Database**: PostgreSQL
- **Server**: Tomcat/Glassfish
- **Security**: BCrypt password hashing

## 📁 Project Structure

```
milestone1-web-app/
├── src/main/java/          # Java source code
│   └── com/bc/careconnect/ # Package structure
├── src/main/webapp/        # Web resources (JSP, CSS, JS)
├── database/               # SQL scripts and setup
└── deployment/            # Server configuration
```

## 🚀 Setup Instructions

### Prerequisites

- Java 8 or higher
- PostgreSQL 12 or higher
- Tomcat 9 or Glassfish 5

### Database Setup

1. Install PostgreSQL
2. Run scripts in `database/` folder:
   ```bash
   psql -U postgres -f database/create_database.sql
   psql -U postgres -d careconnect -f database/create_tables.sql
   ```

### Application Deployment

1. Configure database connection in `src/main/resources/database.properties`
2. Build the WAR file
3. Deploy to Tomcat/Glassfish server
4. Access at `http://localhost:8080/careconnect-web`

## 📊 Grading Criteria (20 marks)

- **Functional login form with server-side validation** (5 marks)
- **Functional registration form with server-side validation** (5 marks)
- **Correct PostgreSQL database setup and validation** (5 marks)
- **Complete registration and login functionality** (5 marks)

## 🐛 Known Issues

- None currently identified

## 📝 Development Notes

- All passwords are hashed using BCrypt
- Session timeout set to 30 minutes
- Input validation implemented both client and server-side
- Database connection pooling configured for optimal performance
