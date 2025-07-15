# CareConnect Database Documentation

## Database Overview

CareConnect uses PostgreSQL 15 as its primary database system. The database is designed to store student wellness management data with a focus on security, data integrity, and performance.

## Connection Details

### Production (Docker)
- **Host**: `postgres` (Docker container name)
- **Port**: `5432`
- **Database**: `careconnect`
- **Username**: `postgres`
- **Password**: Set via environment variable `DB_PASSWORD`

### Development (Local)
- **Host**: `localhost`
- **Port**: `5432`
- **Database**: `careconnect`
- **Username**: `postgres`
- **Password**: `careconnect2024`

## Database Schema

### Users Table

The `users` table stores student information and authentication details.

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Column Descriptions

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | SERIAL | PRIMARY KEY | Auto-incrementing unique identifier |
| `student_number` | VARCHAR(20) | UNIQUE, NOT NULL | Student ID in format A12345678 |
| `name` | VARCHAR(100) | NOT NULL | Student's first name |
| `surname` | VARCHAR(100) | NOT NULL | Student's last name |
| `email` | VARCHAR(150) | UNIQUE, NOT NULL | Student's email address |
| `phone` | VARCHAR(20) | NULLABLE | Student's phone number (optional) |
| `password_hash` | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| `created_at` | TIMESTAMP | DEFAULT NOW() | Account creation timestamp |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | Last modification timestamp |

#### Constraints

- **Primary Key**: `id` (auto-incrementing)
- **Unique Constraints**: 
  - `student_number` (ensures no duplicate student IDs)
  - `email` (ensures no duplicate email addresses)
- **Not Null Constraints**: All fields except `phone`

### Indexes

Performance optimization indexes:

```sql
-- Index on student_number for fast lookups during login
CREATE INDEX idx_users_student_number ON users(student_number);

-- Index on email for fast lookups during email-based login
CREATE INDEX idx_users_email ON users(email);
```

### Triggers

Automatic timestamp update trigger:

```sql
-- Function to update the updated_at column
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger to automatically update updated_at on row changes
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
```

## Data Validation

### Application-Level Validation

The application enforces the following validation rules:

#### Student Number
- **Format**: One uppercase letter followed by 8 digits (e.g., `A12345678`)
- **Pattern**: `^[A-Z]\\d{8}$`
- **Examples**: 
  - ✅ `A12345678`
  - ✅ `B98765432`
  - ❌ `a12345678` (lowercase)
  - ❌ `A1234567` (too short)
  - ❌ `AB12345678` (two letters)

#### Email Address
- **Format**: Standard email format
- **Pattern**: `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$`
- **Examples**:
  - ✅ `student@example.com`
  - ✅ `john.doe+test@university.edu`
  - ❌ `invalid-email`
  - ❌ `@domain.com`

#### Phone Number
- **Format**: 10 digits (local) or international format
- **Pattern**: `^\\+?[1-9]\\d{1,14}$|^\\d{10}$`
- **Examples**:
  - ✅ `1234567890`
  - ✅ `+1234567890123`
  - ❌ `123-456-7890` (contains hyphens)
  - ❌ `phone` (non-numeric)

#### Password
- **Length**: 8-100 characters
- **Requirements**:
  - At least one uppercase letter
  - At least one lowercase letter
  - At least one digit
  - At least one special character
- **Hashing**: BCrypt with 12 rounds

#### Names (First Name, Surname)
- **Length**: 2-50 characters
- **Allowed Characters**: Letters, spaces, hyphens, apostrophes
- **Pattern**: `^[A-Za-z\\s\\-']{2,50}$`

## Database Initialization

### Automatic Schema Creation

The application includes automatic schema initialization via `DatabaseInitializationListener`:

1. **Startup Check**: On application startup, checks if `users` table exists
2. **Schema Creation**: If table doesn't exist, reads and executes `database_schema.sql`
3. **Idempotent Operations**: Uses `IF NOT EXISTS` clauses to prevent conflicts

### Manual Setup

For manual database setup:

```bash
# Create database
createdb -U postgres careconnect

# Run schema
psql -U postgres -d careconnect -f database_schema.sql
```

## Security Considerations

### Password Security
- **Hashing Algorithm**: BCrypt with 12 rounds
- **Salt**: Automatically generated per password
- **Storage**: Only hashed passwords stored, never plaintext

### SQL Injection Prevention
- **PreparedStatements**: All database queries use parameterized statements
- **Input Validation**: All user inputs validated before database operations
- **Escaping**: No string concatenation in SQL queries

### Data Integrity
- **Unique Constraints**: Prevent duplicate students and emails
- **Foreign Key Constraints**: Will be added for future table relationships
- **Transaction Management**: Ensure data consistency

## Backup and Recovery

### Docker Volume Backup
```bash
# Create backup
docker run --rm -v careconnect_postgres_data:/data -v $(pwd):/backup ubuntu tar czf /backup/careconnect-db-backup.tar.gz /data

# Restore backup
docker run --rm -v careconnect_postgres_data:/data -v $(pwd):/backup ubuntu tar xzf /backup/careconnect-db-backup.tar.gz -C /
```

### SQL Dump Backup
```bash
# Create SQL dump
docker exec careconnect-db pg_dump -U postgres careconnect > careconnect-backup.sql

# Restore from dump
docker exec -i careconnect-db psql -U postgres careconnect < careconnect-backup.sql
```

## Performance Monitoring

### Health Checks
- **Connection Test**: `/health` endpoint tests database connectivity
- **Table Verification**: Confirms `users` table exists and is accessible
- **Query Performance**: Monitor via application logs

### Query Optimization
- **Indexes**: Strategic indexes on frequently queried columns
- **Connection Pooling**: Recommended for production (not yet implemented)
- **Query Analysis**: Use EXPLAIN ANALYZE for complex queries

## Future Enhancements

### Planned Tables
- `wellness_records` - Student wellness tracking data
- `appointments` - Wellness appointment scheduling
- `resources` - Wellness resources and materials
- `notifications` - User notification system

### Performance Improvements
- Connection pooling implementation
- Read replicas for scaling
- Caching layer (Redis)
- Database partitioning for large datasets

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Check PostgreSQL service status
   - Verify network connectivity
   - Confirm credentials

2. **Table Not Found**
   - Check schema initialization
   - Verify database name
   - Run manual schema creation

3. **Duplicate Key Errors**
   - Student number or email already exists
   - Check unique constraints
   - Implement proper error handling

### Debug Commands

```sql
-- Check table existence
SELECT EXISTS (
    SELECT FROM information_schema.tables 
    WHERE table_name = 'users'
);

-- View table structure
\d users

-- Check user count
SELECT COUNT(*) FROM users;

-- View recent users
SELECT student_number, name, surname, email, created_at 
FROM users 
ORDER BY created_at DESC 
LIMIT 10;
```