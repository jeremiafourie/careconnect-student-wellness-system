# CareConnect Desktop Application (Milestone 2)

## 📋 Overview

The desktop application provides comprehensive wellness management functionality with integrated authentication from the web application. Built using Java Swing with NetBeans GUI Builder and JavaDB.

**Grade Weight**: 30 marks (60% of total project)

## 🎯 Features

- Integrated authentication with web application
- Complete appointment management (CRUD operations)
- Comprehensive counselor management
- Student feedback system
- User-friendly Swing interface
- MVC architecture implementation

## 🛠️ Technology Stack

- **GUI**: Java Swing (NetBeans GUI Builder)
- **Database**: JavaDB (Derby)
- **Authentication**: PostgreSQL (shared with web app)
- **Architecture**: Model-View-Controller (MVC)
- **Collections**: ArrayList, HashMap for data management

## 📁 Project Structure

```
milestone2-desktop-app/
├── src/                    # Java source code
│   └── com/bc/careconnect/ # Package structure
│       ├── gui/           # Swing GUI components
│       ├── models/        # Data models
│       ├── services/      # Business logic
│       ├── controllers/   # MVC controllers
│       └── utils/         # Utility classes
├── resources/             # Application resources
├── database/              # JavaDB scripts
└── lib/                   # External libraries
```

## 🚀 Setup Instructions

### Prerequisites

- Java 8 or higher
- NetBeans IDE (recommended)
- PostgreSQL (for shared authentication)
- JavaDB/Derby (embedded)

### Database Setup

1. **PostgreSQL** (shared authentication):

   - Ensure web application database is set up
   - Configure connection in `resources/database.properties`

2. **JavaDB** (wellness data):
   ```bash
   # JavaDB will be created automatically on first run
   # Or manually run: database/create_javadb_schema.sql
   ```

### Running the Application

1. Open project in NetBeans
2. Configure database connections in `resources/`
3. Build and run `CareConnectApp.java`
4. Login with credentials created in web application

## 📊 Grading Criteria (30 marks)

- **OOP principles, collections, exception handling** (5 marks)
- **User-friendly GUI with NetBeans GUI Builder** (5 marks)
- **Complete CRUD operations for wellness entities** (5 marks)
- **All functionality working properly** (5 marks)
- **Presentation quality** (5 marks)
- **Peer evaluation** (5 marks)

## 🏗️ Architecture Overview

### MVC Implementation

- **Models**: Data entities (User, Appointment, Counselor, Feedback)
- **Views**: Swing GUI components and panels
- **Controllers**: Business logic and data flow management

### Key Components

- **LoginFrame**: Authentication interface
- **MainDashboard**: Central navigation hub
- **AppointmentPanel**: Appointment management interface
- **CounselorPanel**: Counselor management interface
- **FeedbackPanel**: Feedback submission and management

## 🔧 Integration Features

- Shared authentication with web application
- User session management across components
- Consistent data validation
- Error handling and user feedback

## 📱 User Interface

- Tab-based navigation for main functions
- Input validation with real-time feedback
- Confirmation dialogs for destructive operations
- Responsive layout design

## 🐛 Troubleshooting

- **Database Connection Issues**: Check `resources/database.properties`
- **Authentication Problems**: Verify PostgreSQL connection
- **GUI Issues**: Ensure NetBeans GUI Builder components are properly configured

## 📝 Development Notes

- Built using NetBeans GUI Builder for consistency
- Implements proper exception handling throughout
- Uses Java Collections (ArrayList, HashMap) for data management
- MVC architecture ensures separation of concerns
- Integration-ready with web application authentication
