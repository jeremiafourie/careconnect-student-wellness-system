# CareConnect Desktop Module (Milestone 2)

## Architecture Overview
Java Swing desktop application implementing MVC pattern with JavaDB integration. Authenticates via web service and provides local wellness management functionality with full CRUD operations.

## Module Structure
```
desktop/
├── src/main/java/com/bc/desktop/
│   ├── controllers/      # MVC Controllers
│   │   ├── MainController.java
│   │   ├── LoginController.java
│   │   ├── AppointmentController.java
│   │   ├── CounselorController.java
│   │   └── FeedbackController.java
│   ├── models/           # Data Models
│   │   ├── StudentUser.java
│   │   ├── Appointment.java
│   │   ├── Counselor.java
│   │   ├── Feedback.java
│   │   └── UserRole.java
│   ├── services/         # Business Services
│   │   ├── AuthenticationService.java
│   │   └── DatabaseService.java
│   ├── views/            # Swing GUI Components
│   │   ├── MainFrame.java
│   │   ├── LoginPanel.java
│   │   ├── HomePanel.java
│   │   ├── AppointmentsPanel.java
│   │   ├── CounselorsPanel.java
│   │   └── FeedbackPanel.java
│   └── Desktop.java      # Main entry point
└── pom.xml
```

## Core Components

### MVC Architecture

#### Controllers (Business Logic)
**MainController.java**: Central application coordinator
- Initializes services and sub-controllers
- Manages application lifecycle
- Handles login/logout flow
- Coordinates between components

**LoginController.java**: Authentication logic
- Input validation (6-digit student number format)
- Async authentication via web service
- Error handling with registration redirect
- Session management

**AppointmentController.java**: Appointment management
- CRUD operations for appointments
- Counselor selection and scheduling
- Status management (SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED)
- Conflict detection and validation

**CounselorController.java**: Counselor management
- CRUD operations for counselors
- Specialization and availability management
- Admin-only operations
- Sample data population

**FeedbackController.java**: Feedback system
- Rating submission (1-5 stars)
- Comment management
- Feedback history display
- Edit/delete operations

#### Models (Data Layer)
**StudentUser.java**: Session management wrapper
- Wraps shared User entity
- Session state (isLoggedIn, role)
- User role management (STUDENT, ADMIN)
- Session validation and cleanup

**Appointment.java**: Appointment entity
- Status enum (SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED)
- Business logic: `isUpcoming()`, `canBeCancelled()`
- Automatic timestamp updates
- Counselor relationship management

**Counselor.java**: Counselor entity
- Specialization management
- Availability status
- Contact information
- Admin-managed entity

**Feedback.java**: Feedback entity
- Rating validation (1-5 range)
- Comment storage
- Category classification
- Student ownership

#### Views (User Interface)
**MainFrame.java**: Main application window
- JFrame with BorderLayout
- Tabbed navigation (JTabbedPane)
- Login/main view switching
- Menu bar with user actions
- Status bar for notifications

**LoginPanel.java**: Authentication interface
- GridBagLayout for form fields
- Real-time validation feedback
- Enter key support
- Error message display

**AppointmentsPanel.java**: Appointment management
- JTable with custom TableModel
- CRUD operation buttons
- Appointment booking dialog
- Status-based cell rendering

**CounselorsPanel.java**: Counselor management
- JTable for counselor display
- Add/edit/delete operations
- Admin-only functionality
- Specialization filtering

**FeedbackPanel.java**: Feedback interface
- Rating selection (1-5 stars)
- Comment text area
- Submission validation
- Feedback history table

### Services Layer

#### AuthenticationService.java
**Purpose**: Web service integration for authentication
- HTTP client configuration
- POST request to `/api/login` endpoint
- JSON response parsing
- Custom exception handling
- User registration redirect support

**Key Methods**:
- `authenticate()`: Validates credentials via web service
- `validateStudentNumber()`: 6-digit format (000000-999999)
- `validatePassword()`: Minimum 6 characters
- Custom `AuthenticationException` with user-not-found flag

#### DatabaseService.java
**Purpose**: JavaDB/Derby database operations
- Singleton pattern for connection management
- Embedded database (`jdbc:derby:careconnect_desktop;create=true`)
- Schema initialization and sample data
- Transaction management
- CRUD operations for all entities

**Database Schema**:
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

## OOP Implementation

### Inheritance
- `MainFrame extends JFrame`: GUI window functionality
- `All panels extend JPanel`: Container functionality
- `AuthenticationException extends Exception`: Custom error handling

### Polymorphism
- Method overriding: `equals()`, `hashCode()`, `toString()`
- Interface implementation: `ActionListener`, `TableModel`
- Event handling: Multiple implementations of same interface

### Encapsulation
- Private fields with public getters/setters
- Controlled access to internal state
- Data validation in setters

### Abstraction
- Service layer hides implementation details
- Database operations abstracted behind simple methods
- HTTP communication hidden behind authentication service

## Event-Driven Programming

### Event Flow
1. User action (button click, menu selection)
2. View fires event (ActionEvent)
3. Controller handles event
4. Service processes business logic
5. Model updates
6. View refreshes

### Asynchronous Processing
- SwingWorker for non-blocking operations
- Background authentication
- Database operations without UI freeze
- Progress indication during long operations

## Security Features

### Authentication
- Web service integration for credential validation
- Session-based authentication
- Role-based access control (STUDENT, ADMIN)
- Automatic session timeout

### Input Validation
- Client-side validation for user experience
- Student number format validation
- Password strength checking
- Form field validation

### Data Protection
- Local database for user-specific data only
- No sensitive authentication data stored locally
- Secure communication with web service

## Dependencies
- Shared module 1.0-SNAPSHOT (Common models and utilities)
- Apache Derby 10.15.2.0 (Embedded database)
- Jackson 2.15.2 (JSON processing)
- Java Swing (GUI framework)
- Java HTTP Client (Web service communication)

## Build & Run
```bash
# Build the application
mvn clean compile

# Run the desktop application
mvn exec:java

# Alternative run command
mvn exec:java -Dexec.mainClass="com.bc.desktop.Desktop"
```

## Configuration
- Database: Embedded JavaDB (auto-created)
- Web service URL: `https://careconnect.exequtech.com`
- Authentication endpoint: `/api/login`