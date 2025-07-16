# BC Student Wellness Management System - Desktop Application

## Project Overview
This is the desktop component of the BC Student Wellness Management System, implementing Milestone 2 of the PRG3781 Project 2025. The application provides a comprehensive Swing-based GUI for managing wellness services including appointments, counselors, and student feedback.

## Architecture
The application follows the **Model-View-Controller (MVC)** pattern with clean separation of concerns:

### Models (`desktop/src/main/java/com/bc/desktop/models/`)
- **StudentUser** - Wrapper for authenticated user with session management
- **Counselor** - Counselor entity with specialization and availability
- **Appointment** - Appointment management with status tracking
- **Feedback** - Student feedback with rating validation

### Views (`desktop/src/main/java/com/bc/desktop/views/`)
- **MainFrame** - Main application window with tabbed navigation
- **LoginPanel** - User authentication interface
- **HomePanel** - Welcome dashboard with user information
- **CounselorsPanel** - Counselor viewing and management
- **AppointmentsPanel** - Appointment booking and management
- **FeedbackPanel** - Feedback submission and history

### Controllers (`desktop/src/main/java/com/bc/desktop/controllers/`)
- **MainController** - Central application coordinator
- **LoginController** - Authentication logic and validation
- **CounselorController** - Counselor CRUD operations
- **AppointmentController** - Appointment management logic
- **FeedbackController** - Feedback handling and persistence

### Services (`desktop/src/main/java/com/bc/desktop/services/`)
- **AuthenticationService** - Web service integration for login
- **DatabaseService** - Local JavaDB database operations

## Features Implemented

### ✅ Core Java & OOP Requirements
- **Inheritance**: Base classes and specialized implementations
- **Polymorphism**: Interface implementations and method overriding
- **Encapsulation**: Private fields with public getters/setters
- **Abstraction**: Service interfaces and abstract concepts
- **Collections**: ArrayList for managing appointments and feedback
- **Exception Handling**: Comprehensive error handling throughout

### ✅ GUI with Swing
- **Tabbed Navigation**: Home, Counselors, Appointments, Feedback
- **Professional Layout**: GridBag, Border, and Flow layouts
- **Input Validation**: Real-time validation with user feedback
- **Table Views**: Sortable tables for data display
- **Dialog Windows**: Modal dialogs for data entry
- **Menu System**: Application menu with logout functionality

### ✅ Database Integration (JavaDB/Derby)
- **Automated Schema Creation**: Tables created on first run
- **CRUD Operations**: Full Create, Read, Update, Delete functionality
- **Sample Data**: Pre-populated counselor data
- **Connection Management**: Proper resource handling

### ✅ Authentication System
- **Web Service Integration**: Connects to careconnect.exequtech.com
- **Input Validation**: Student number and password validation
- **Session Management**: User session with automatic cleanup
- **Error Handling**: Clear error messages for authentication failures

## Prerequisites
- **Java 17** or higher
- **Maven 3.6+** for building
- **Internet Connection** for authentication (connects to careconnect.exequtech.com)

## Building and Running

### 1. Compile the Project
```bash
# From the project root directory
mvn clean compile
```

### 2. Run the Desktop Application
```bash
# From the project root directory
cd desktop
mvn exec:java
```

OR

```bash
# Alternative method
mvn exec:java -Dexec.mainClass="com.bc.desktop.Desktop"
```

## Usage Instructions

### 1. Login
- Enter your **6-digit student number** (e.g., 123456)
- Enter your **password**
- Click **Login** or press Enter

### 2. Navigation
Once logged in, use the tabs to navigate between sections:
- **Home**: Welcome dashboard with user information
- **Counselors**: View available counselors and their specializations
- **Appointments**: Book, reschedule, or cancel appointments
- **Feedback**: Submit and manage your feedback

### 3. Key Features
- **Appointment Booking**: Select counselors and available time slots
- **Feedback System**: Rate services from 1-5 stars with comments
- **Data Persistence**: All data is stored locally using JavaDB
- **Real-time Updates**: Tables refresh automatically after changes

## Database Schema
The application uses **Apache Derby (JavaDB)** with the following tables:

```sql
-- Counselors table
CREATE TABLE counselors (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Appointments table
CREATE TABLE appointments (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    student_number INT NOT NULL,
    counselor_id BIGINT NOT NULL,
    counselor_name VARCHAR(255) NOT NULL,
    appointment_datetime TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    notes CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Feedback table
CREATE TABLE feedback (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    student_number INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comments CLOB,
    category VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Web Service Integration
The desktop application integrates with the web application for authentication:
- **Endpoint**: `https://careconnect.exequtech.com/api/login`
- **Method**: POST
- **Parameters**: studentNumber, password
- **Response**: JSON with user data and authentication status

## Troubleshooting

### Common Issues

1. **Authentication Fails**
   - Ensure internet connection is active
   - Verify student number is exactly 6 digits
   - Check password is correct
   - Confirm web server is running at careconnect.exequtech.com

2. **Database Errors**
   - Database is created automatically on first run
   - Check file system permissions in the application directory
   - Ensure no other instances are running

3. **Build Errors**
   - Verify Java 17+ is installed: `java --version`
   - Confirm Maven is installed: `mvn --version`
   - Run `mvn clean` to clear any cached builds

### Performance Notes
- First startup may take longer due to database initialization
- Sample counselor data is populated automatically
- Application stores local preferences and session data

## Project Structure
```
desktop/
├── src/main/java/com/bc/desktop/
│   ├── controllers/          # MVC Controllers
│   ├── models/              # Data Models
│   ├── services/            # Business Logic Services
│   ├── views/               # Swing GUI Components
│   └── Desktop.java         # Main Application Entry Point
├── pom.xml                  # Maven Dependencies
└── target/                  # Compiled Classes
```

## Dependencies
- **Shared Module**: Common models and utilities
- **Apache Derby**: Embedded database
- **Jackson**: JSON processing for web service communication
- **Java Swing**: GUI framework
- **Java HTTP Client**: Web service communication

## Assessment Criteria Met
- ✅ **Core Java & OOP**: All four pillars implemented
- ✅ **Collections**: ArrayList for data management
- ✅ **Exception Handling**: Comprehensive error management
- ✅ **MVC Architecture**: Clean separation of concerns
- ✅ **Swing GUI**: Professional tabbed interface
- ✅ **CRUD Operations**: Full database functionality
- ✅ **Input Validation**: Real-time validation with feedback
- ✅ **Database Integration**: JavaDB with proper schema

## Author
**CareConnect Development Team**  
PRG3781 Project 2025  
Belgium Campus