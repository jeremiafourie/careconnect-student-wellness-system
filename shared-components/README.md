# CareConnect Shared Components

## 📋 Overview

This module contains shared utilities, models, and services used by both the web application and desktop application to ensure consistency and code reusability.

## 🎯 Purpose

- Unified authentication logic
- Shared data models
- Common utility functions
- Consistent validation rules
- Database connection management

## 📁 Structure

```

shared-components/
└── src/com/bc/careconnect/shared/
├── models/ # Shared data models
├── services/ # Authentication and user services
├── utils/ # Utility classes
├── exceptions/ # Custom exception classes
└── config/ # Configuration files

```

## 🔧 Components

### Models

- **User.java**: User entity with validation
- **UserRole.java**: User role enumeration

### Services

- **AuthenticationService.java**: Core authentication logic
- **UserService.java**: User management operations

### Utilities

- **DatabaseConnection.java**: PostgreSQL connection management
- **PasswordUtils.java**: BCrypt password hashing/verification
- **ValidationUtils.java**: Input validation methods
- **Constants.java**: Application-wide constants

### Exceptions

- **AuthenticationException.java**: Authentication-related errors
- **ValidationException.java**: Input validation errors
- **DatabaseException.java**: Database operation errors

## 🚀 Usage

### In Web Application

```java
import com.bc.careconnect.shared.services.AuthenticationService;
import com.bc.careconnect.shared.utils.PasswordUtils;

// In servlets
AuthenticationService auth = new AuthenticationService();
User user = auth.authenticate(username, password);
```

### In Desktop Application

```java
import com.bc.careconnect.shared.models.User;
import com.bc.careconnect.shared.utils.UserSession;

// In GUI controllers
User currentUser = UserSession.getCurrentUser();
```

## 🔒 Security Features

- BCrypt password hashing
- Input sanitization and validation
- SQL injection prevention
- Session management utilities

## 📝 Integration Notes

- Shared between both applications for consistency
- Database connections configured via properties files
- Exception handling provides detailed error information
- Validation rules ensure data integrity across platforms

## 🛠️ Configuration

Configuration files in `config/` directory:

- `database.properties`: Database connection settings
- `application.properties`: Application-wide settings

## 📋 Dependencies

- PostgreSQL JDBC Driver
- BCrypt library for password hashing
- Java Collections Framework
