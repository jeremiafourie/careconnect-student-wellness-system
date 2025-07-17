# CareConnect Shared Module

## Architecture Overview
Common code library shared between web and desktop applications, ensuring consistency and reducing duplication.

## Module Structure
```
shared/
├── models/              # Entity classes
│   └── User.java        # Core user entity
├── dto/                 # Data Transfer Objects
│   ├── UserLoginDto.java
│   └── UserRegistrationDto.java
└── utils/               # Utility classes
    ├── PasswordUtils.java
    └── ValidationUtils.java
```

## Core Components

### User.java
**Purpose**: Core user entity representing PostgreSQL users table
- Fields: id, studentNumber (int), name, surname, email, phone, passwordHash, timestamps
- Business methods: getFullName(), equals(), hashCode(), toString()
- **Used by**: Web (all servlets), Desktop (authentication)

### PasswordUtils.java
**Purpose**: BCrypt password hashing and verification
- `hashPassword()`: BCrypt hash with salt (work factor 12)
- `verifyPassword()`: Secure password verification
- `isValidPassword()`: Password strength validation (8-100 chars, mixed case, digit, special)
- **Used by**: Web (registration/login), Desktop (available)

### ValidationUtils.java
**Purpose**: Input validation and sanitization
- `isValidStudentNumber()`: 6-digit format (000000-999999, allows 000000 for admin)
- `isValidEmail()`: Standard email format validation
- `isValidName()`: 2-50 chars, letters/spaces/hyphens/apostrophes
- `isValidPhone()`: 10 digits (optional field)
- `isValidPassword()`: 8-100 chars, uppercase, lowercase, digit, special character
- **Used by**: Web (form validation), Desktop (not used)

### DTOs
**UserLoginDto**: Login request structure (identifier, password)
**UserRegistrationDto**: Registration request structure with validation

## Integration Points
- **Web**: Uses User entity, PasswordUtils, ValidationUtils, DTOs
- **Desktop**: Uses User entity, PasswordUtils (available)

## Dependencies
- jbcrypt 0.4 (BCrypt hashing)
- jakarta.validation-api 3.0.2 (Bean validation)

## Build
```bash
mvn clean install  # Required for other modules
```