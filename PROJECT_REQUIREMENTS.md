# CareConnect - Project Requirements Documentation

## 📋 Project Information

- **Subject**: Programming 37(8)1
- **Assessment**: Project 2025
- **Total Marks**: 50 Marks
- **Project Type**: Group Project (4 students per group)
- **Institution**: Belgium Campus

## 🎯 Project Overview

The CareConnect Student Wellness Management System is a comprehensive Java-based application designed to manage student wellness services at Belgium Campus. The system consists of two integrated components that work together to provide a complete wellness management solution.

### System Components

1. **Web Application (Milestone 1)**: JSP-based registration and authentication gateway
2. **Desktop Application (Milestone 2)**: Swing-based wellness management system with integrated authentication

## 📅 Project Timeline

- **Milestone 1 Due**: July 14, 2025 (Web Application)
- **Milestone 2 Due**: July 17, 2025 (Desktop Application)
- **Final Presentation**: July 18, 2025 (Integrated System Demonstration)

---

## 🌐 MILESTONE 1: Web Application (20 Marks)

### 📋 Overview

Create a web application that serves as the authentication gateway and user registration portal for the CareConnect system.

### 🛠️ Technology Requirements

- **Frontend**: JSP pages with HTML, CSS, JavaScript
- **Backend**: Java Servlets
- **Database**: PostgreSQL
- **Server**: Tomcat or Glassfish
- **Security**: Password hashing and session management

### 📄 Required JSP Pages

1. **index.jsp**: Home page with navigation links to login and registration
2. **register.jsp**: User registration form for new students
3. **login.jsp**: Authentication form for existing users
4. **dashboard.jsp**: Welcome page displayed after successful login

### ⚙️ Required Servlets

1. **RegisterServlet**: Handles user registration and stores data in PostgreSQL
2. **LoginServlet**: Authenticates users against PostgreSQL database

### 🔧 Functional Requirements

#### Registration System

- ✅ Validate all input fields with server-side validation:
  - Email format validation
  - Password strength requirements
  - Phone number format validation
  - Student number format validation
- ✅ Check for duplicate usernames, emails, and student numbers
- ✅ Provide comprehensive user feedback:
  - "Registration successful" for successful registrations
  - "Email already exists" for duplicate emails
  - Specific validation error messages
- ✅ Store user data securely using password hashing

#### Authentication System

- ✅ Validate user credentials against PostgreSQL database
- ✅ Support login by email OR student number
- ✅ Provide clear error messages for incorrect login attempts
- ✅ Redirect to dashboard.jsp upon successful authentication
- ✅ Maintain session state using HttpSession

#### Dashboard Features

- ✅ Display personalized welcome message ("Welcome, [Student Name]")
- ✅ Provide logout functionality that:
  - Invalidates the current session
  - Redirects to login.jsp
- ✅ Show user profile information

#### Database Requirements

- ✅ PostgreSQL database with Users table containing:
  - `student_number` (Primary Key)
  - `name` (NOT NULL)
  - `surname` (NOT NULL)
  - `email` (UNIQUE, NOT NULL)
  - `phone` (NOT NULL)
  - `password` (NOT NULL, hashed)
  - `role` (DEFAULT 'student')
  - `created_at` (TIMESTAMP)
  - `is_active` (BOOLEAN)

### 📊 Grading Criteria (20 Marks)

1. **Functional login form with server-side validation** - 5 marks
2. **Functional registration form with server-side validation** - 5 marks
3. **Correct setup of PostgreSQL database and database validation** - 5 marks
4. **Application can Register new user and Login existing user** - 5 marks

---

## 💻 MILESTONE 2: Desktop Application (30 Marks)

### 📋 Overview

Develop a comprehensive desktop application using Java Swing and NetBeans GUI Builder that integrates with the web application's authentication system and provides complete wellness management functionality.

### 🛠️ Technology Requirements

- **GUI Framework**: Java Swing with NetBeans GUI Builder
- **Database**: JavaDB (Derby) for wellness data
- **Authentication**: PostgreSQL (shared with web application)
- **Architecture**: Model-View-Controller (MVC)
- **Programming**: Core Java with OOP principles

### 🏗️ Architecture Requirements

#### Core Java & OOP Implementation

- ✅ **Inheritance**: User types, entity hierarchies, GUI component inheritance
- ✅ **Polymorphism**: Different user roles, operation interfaces
- ✅ **Encapsulation**: Data protection, secure access methods
- ✅ **Abstraction**: Database operations, business logic interfaces
- ✅ **Collections**: ArrayList, HashMap for managing appointments and feedback
- ✅ **Exception Handling**: Comprehensive error management throughout application
- ✅ **MVC Architecture**: Clear separation of Model, View, and Controller components

#### GUI Requirements (NetBeans GUI Builder)

- ✅ **Main Dashboard**: Central navigation hub accessible after authentication
- ✅ **Navigation Options**:
  - Appointment Management
  - Counselor Management
  - Feedback Management
  - User Profile Management
- ✅ **User Interface Standards**:
  - Tab-based or menu-based navigation
  - Input validation with real-time feedback
  - Confirmation dialogs for delete/destructive actions
  - Error handling for database connection issues

### 🔧 Functional Requirements

#### Authentication Integration

- ✅ Desktop login screen as application entry point
- ✅ Authenticate against shared PostgreSQL database
- ✅ Allow users registered via web app to login to desktop app
- ✅ Maintain user session throughout application usage
- ✅ Secure logout functionality

#### Appointment Management (CRUD Operations)

- ✅ **Create**: Book new appointments
  - Select counselor from available list
  - Choose date using calendar interface
  - Select time slot based on counselor availability
  - Set appointment status (e.g., "Scheduled")
- ✅ **Read**: View all appointments for logged-in user
- ✅ **Update**: Modify appointment details (reschedule)
- ✅ **Delete**: Cancel appointments with confirmation

#### Counselor Management (CRUD Operations)

- ✅ **Create**: Add new counselors
  - Full name and contact information
  - Specialization area
  - Availability schedule
- ✅ **Read**: Display comprehensive list of counselors
- ✅ **Update**: Modify counselor details
- ✅ **Delete**: Remove counselors from system

#### Feedback Management (CRUD Operations)

- ✅ **Create**: Submit feedback
  - Rating scale (1-5 stars)
  - Text comments
  - Anonymous option
  - Automatic timestamp
- ✅ **Read**: View feedback history for logged-in user
- ✅ **Update**: Edit user's own feedback entries
- ✅ **Delete**: Remove user's own feedback entries

#### Database Integration (JavaDB)

- ✅ **Tables Required**:
  - `appointments` (appointment_id, student_number, counselor_id, date, time, status)
  - `counselors` (counselor_id, name, specialization, availability, contact_info)
  - `feedback` (feedback_id, student_number, rating, comments, created_at)
- ✅ **Operations**: Full CRUD functionality for all entities
- ✅ **Relationships**: Proper foreign key relationships and constraints

### 📊 Grading Criteria (30 Marks)

1. **Proper use of collections, exception handling, and OOP principles** - 5 marks
2. **User-friendly and functional GUI with NetBeans GUI Builder** - 5 marks
3. **Complete CRUD operations for wellness management entities** - 5 marks
4. **Application achieves all functionality with seamless integration** - 5 marks
5. **Presentation demonstrating integrated system** - 5 marks
6. **Peer Evaluation** - 5 marks

---

## 🔗 INTEGRATION REQUIREMENTS

### System Integration

The key innovation of this project is the seamless integration between the web and desktop applications through shared authentication and consistent user experience.

#### Shared Authentication System

- ✅ Both applications connect to the same PostgreSQL database for user authentication
- ✅ Users register once in the web application
- ✅ Same credentials work for both web and desktop applications
- ✅ Consistent password hashing across platforms
- ✅ Unified user session management

#### Data Consistency

- ✅ User profile information shared between applications
- ✅ Consistent validation rules across platforms
- ✅ Unified error handling and user feedback
- ✅ Synchronized user status and permissions

#### Technical Integration

- ✅ Shared utility classes for common operations
- ✅ Consistent database connection management
- ✅ Unified exception handling approach
- ✅ Common configuration management

---

## 👥 TEAM REQUIREMENTS

### Group Composition

- ✅ Each group must consist of exactly 4 students
- ✅ Groups are auto-created on Moodle
- ✅ All group members must create GitHub accounts
- ✅ GitHub repository must be demonstrated during presentation

### Individual Responsibilities

- ✅ All group members MUST understand all Java concepts being assessed
- ✅ All students will be tested on all concepts during group presentations
- ✅ Each member must contribute meaningfully to the codebase
- ✅ Individual peer evaluation will assess contributions

### Collaboration Requirements

- ✅ Use GitHub for version control and collaboration
- ✅ Meaningful commit messages and proper branching
- ✅ Code review process for quality assurance
- ✅ Regular team meetings and progress updates

---

## 📚 TECHNICAL SPECIFICATIONS

### Development Standards

- ✅ **Code Quality**: Follow Java coding conventions and best practices
- ✅ **Documentation**: Comprehensive inline comments and README files
- ✅ **Error Handling**: Robust exception management throughout
- ✅ **Security**: Secure password storage and session management
- ✅ **Performance**: Efficient database operations and resource management

### Database Design

- ✅ **PostgreSQL Schema**: Properly normalized with appropriate constraints
- ✅ **JavaDB Schema**: Efficient design for wellness management data
- ✅ **Data Integrity**: Foreign key relationships and validation rules
- ✅ **Performance**: Proper indexing and query optimization

### User Interface Standards

- ✅ **Web Interface**: Responsive design with modern styling
- ✅ **Desktop Interface**: Professional Swing GUI with intuitive navigation
- ✅ **Consistency**: Unified design language across platforms
- ✅ **Accessibility**: Clear error messages and user feedback

---

## 🎯 SUCCESS CRITERIA

### Technical Achievement

- ✅ Fully functional web application with robust authentication
- ✅ Complete desktop application with all CRUD operations
- ✅ Seamless integration between both applications
- ✅ Professional-grade code quality and documentation

### Academic Excellence

- ✅ Demonstration of advanced Java programming concepts
- ✅ Proper implementation of OOP principles
- ✅ Effective use of multiple database systems
- ✅ Integration of web and desktop technologies

### Professional Development

- ✅ Real-world application architecture
- ✅ Team collaboration and version control
- ✅ Project management and timeline adherence
- ✅ Professional presentation and communication skills

---
