# CareConnect - BC Student Wellness Management System

![Java](https://img.shields.io/badge/Java-17-orange) ![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue) ![Docker](https://img.shields.io/badge/Docker-Ready-blue) ![License](https://img.shields.io/badge/License-Educational-green)

## 📋 PRG3781 Project 2025 - Belgium Campus

This is a comprehensive Java project implementing Core Java, OOP principles, GUI development with Swing, and database integration for the BC Student Wellness Management System.

### Project Structure

- **🌐 Milestone 1 (Web)**: JSP login/registration system with PostgreSQL (20 marks)
- **🖥️ Milestone 2 (Desktop)**: Java Swing wellness management app with JavaDB (30 marks)

- **📦 Shared Module**: Common models, DTOs, and utilities

**Group_P2 8**: `577881`, `600271`, `600576, 600586`

**GitHub Repo Link**: https://github.com/jeremiafourie/careconnect-student-wellness-system

**Live Demo**: [https://careconnect.exequtech.com](https://careconnect.exequtech.com)

**Admin Credentials for full CRUD access on Desktop App**: Student Number: `000000` and Password: `13Password79.`

## 🏗️ Architecture & Technologies

**Core Stack**: Java 17, Jakarta EE 10, PostgreSQL 15, Apache Derby (JavaDB), Docker
**Frontend**: JSP/JSTL, HTML5/CSS3, Java Swing
**Security**: BCrypt hashing, PreparedStatements, input validation
**Build**: Maven multi-module, CI/CD with Woodpecker

## 🎯 PRG3781 Requirements Implemented

### Milestone 1: Web Application (JSP + PostgreSQL)

- **JSP Pages**: index.jsp, login.jsp, register.jsp, dashboard.jsp
- **Servlets**: RegisterServlet, LoginServlet with full validation
- **Database**: PostgreSQL with users table, constraints, hashed passwords
- **Session Management**: HttpSession with logout functionality

### Milestone 2: Desktop Application (Swing + JavaDB)

- **Core Java/OOP**: Inheritance, polymorphism, encapsulation, abstraction, collections, exception handling
- **MVC Architecture**: Controllers, Models, Views with clean separation
- **GUI**: Swing with tabs, navigation, input validation, confirmation dialogs
- **CRUD Operations**: Appointments, Counselors, Feedback with JavaDB integration
- **Features**: Appointment booking, counselor management, feedback system (1-5 rating)

## 🏗️ Project Structure

```
careconnect/
├── shared/           # Common models, DTOs, utilities
├── web/             # Milestone 1: JSP/Servlet application
│   ├── servlets/    # RegisterServlet, LoginServlet
│   ├── webapp/      # JSP pages, CSS, resources
│   └── config/      # DatabaseConfig for PostgreSQL
└── desktop/         # Milestone 2: Swing application
    ├── controllers/ # MVC controllers
    ├── models/      # Appointment, Counselor, Feedback
    ├── views/       # Swing GUI components
    └── services/    # Database and authentication services
```

## 🚀 Quick Start

### Prerequisites

- Java 17+, Maven 3.6+, PostgreSQL 15+
- For Docker: Docker Engine 20.10+ & Docker Compose V2

### Local Development

```bash
# 1. Setup PostgreSQL database
psql -U postgres -c "CREATE DATABASE careconnect;"
psql -U postgres -d careconnect -f database_schema.sql

# 2. Configure database connection in:
# web/src/main/java/com/bc/web/config/DatabaseConfig.java

# 3. Build and run
mvn clean compile

# Web app: Deploy to servlet container
# Desktop app: cd desktop && mvn exec:java
```

### Docker Deployment

```bash
docker compose up -d
```

## 🔧 Key Features

### Web Application (Milestone 1)

- **Authentication**: Registration/login with BCrypt password hashing
- **Validation**: Student number, email, password strength
- **Database**: PostgreSQL with proper constraints and SQL injection prevention
- **Session Management**: HttpSession with logout functionality

### Desktop Application (Milestone 2)

- **Wellness Management**: Appointments, counselors, feedback system
- **Database**: JavaDB with full CRUD operations
- **GUI**: Professional Swing interface with tabs, validation, confirmation dialogs
- **Core Java**: Complete OOP implementation with MVC architecture

## 🌐 API Endpoints

| Method | Endpoint     | Description          | Auth Required |
| ------ | ------------ | -------------------- | ------------- |
| `GET`  | `/`          | Home page            | No            |
| `GET`  | `/register`  | Registration form    | No            |
| `POST` | `/register`  | Process registration | No            |
| `GET`  | `/login`     | Login form           | No            |
| `POST` | `/login`     | Process login        | No            |
| `GET`  | `/dashboard` | User dashboard       | Yes           |
| `GET`  | `/logout`    | Logout               | Yes           |
| `GET`  | `/health`    | System health check  | No            |

## 🎓 Educational Project

**PRG3781 Project 2025 - Belgium Campus**  
Comprehensive Java assessment covering Core Java, OOP, GUI development, and database integration.

**Group Project Requirements**: 4-member teams, GitHub collaboration, presentation-based assessment (50 marks total)

**Deployment**: Fully containerized with CI/CD pipeline, live at [careconnect.exequtech.com](https://careconnect.exequtech.com)
