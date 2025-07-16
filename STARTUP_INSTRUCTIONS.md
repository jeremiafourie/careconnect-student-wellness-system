# BC Student Wellness Management System - Startup Instructions

## 🚀 Quick Start Guide

### Prerequisites
- **Java 17+** installed
- **Maven 3.6+** available
- **Internet connection** for authentication

### Running the Application

#### Method 1: From Project Root
```bash
# Navigate to project directory
cd ~/Desktop/careconnect

# Start the desktop application
mvn exec:java -pl desktop -Dexec.mainClass="com.bc.desktop.Desktop"
```

#### Method 2: From Desktop Module
```bash
# Navigate to desktop module
cd ~/Desktop/careconnect/desktop

# Start the application  
mvn exec:java
```

### First Time Setup

1. **Application Startup**
   - Login screen will appear
   - Database will be created automatically
   - Sample counselors will be populated

2. **Admin User Setup**
   - First register/login with Student Number: `000000`
   - Set any password during registration
   - This becomes the system administrator account

3. **Student Users**
   - Use any 6-digit student number (100000-999999)
   - Register through the web application first
   - Then login to desktop application

### Test Accounts

#### Admin Account
- **Student Number**: `000000`
- **Access Level**: Full administrative privileges
- **Capabilities**: 
  - Add/Edit/Delete counselors
  - View all appointments and feedback
  - Access system statistics

#### Student Account (Example)
- **Student Number**: `123456` (any 6-digit number)
- **Access Level**: Standard user privileges
- **Capabilities**:
  - View counselors (read-only)
  - Manage personal appointments
  - Submit and manage personal feedback

### Navigation

Once logged in, use the tabs to navigate:
- **Home**: Welcome dashboard with user info
- **Counselors**: View/manage counselor information
- **Appointments**: Book and manage appointments
- **Feedback**: Submit and view feedback

### Troubleshooting

#### Common Issues

1. **Database Errors**
   ```bash
   # Remove existing database if corrupted
   rm -rf careconnect_db/
   # Restart application - database will be recreated
   ```

2. **Authentication Fails**
   - Ensure internet connection is active
   - Verify server is running at careconnect.exequtech.com
   - Check student number format (6 digits)

3. **Build Errors**
   ```bash
   # Clean and rebuild
   mvn clean compile
   ```

4. **Permission Issues**
   ```bash
   # Ensure write permissions in directory
   chmod 755 ~/Desktop/careconnect
   ```

### Features to Test

#### Admin Features (Student Number: 000000)
- [ ] Login with admin credentials
- [ ] See "Administrator" label in welcome message
- [ ] Add new counselor in Counselors tab
- [ ] Edit existing counselor details
- [ ] Delete counselor (verify referential integrity)
- [ ] View all system data

#### Student Features (6-digit student number)
- [ ] Login with student credentials
- [ ] Verify CRUD buttons are hidden in Counselors tab
- [ ] Book appointment with counselor
- [ ] Submit feedback with rating
- [ ] View personal appointment history

### Performance Notes

- First startup may take 10-15 seconds (database initialization)
- Subsequent startups are faster (database already exists)
- Internet connection required for authentication only
- Local operations work offline after login

### Success Indicators

✅ **Application Started Successfully** when you see:
- Login screen appears
- No error messages in console
- Database files created in `careconnect_db/` directory

✅ **Admin Login Working** when you see:
- "Welcome, [Name] (Administrator)!" message
- Add/Edit/Delete buttons visible in Counselors tab
- Admin-specific content in Home tab

✅ **Student Login Working** when you see:
- "Welcome, [Name]!" message (no Administrator label)
- CRUD buttons hidden in Counselors tab
- Student-specific interface

---

**Ready for demonstration!** 🎉

The system is fully functional and meets all PRG3781 project requirements.