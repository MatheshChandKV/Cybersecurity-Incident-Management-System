# Cybersecurity Incident Management System

A **Java-based command-line application** for reporting, tracking, prioritizing, and investigating cybersecurity incidents.

The project is developed as a **small academic application** to demonstrate practical implementation of Java programming, Object-Oriented Programming, JDBC, MySQL, collections, exception handling, authentication, and basic cybersecurity concepts.

The system allows users to report incidents, administrators to assign incidents to security analysts, and analysts to investigate and update incidents throughout their lifecycle.

---

## Features

- User authentication
- Role-based access control
- Three user roles:
  - Admin
  - Security Analyst
  - User
- Report cybersecurity incidents
- View reported incidents
- Assign incidents to security analysts
- Update incident status
- Record investigation findings
- Automatic risk-score calculation
- Automatic severity classification
- Priority-based incident management
- Basic audit logging
- Console-based reports
- MySQL database integration

---

## Technologies Used

| Technology | Purpose |
|---|---|
| **Java 17** | Application development |
| **Maven** | Project and dependency management |
| **JDBC** | Java–MySQL database connectivity |
| **MySQL 8** | Persistent data storage |
| **Git & GitHub** | Version control and repository hosting |

---

## Project Structure

```text
Cybersecurity-Incident-Management-System/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── cims/
│                   ├── Main.java
│                   ├── Database.java
│                   ├── User.java
│                   ├── Incident.java
│                   ├── Investigation.java
│                   ├── UserDAO.java
│                   ├── IncidentDAO.java
│                   ├── InvestigationDAO.java
│                   ├── AuthService.java
│                   ├── RiskCalculator.java
│                   ├── Role.java
│                   ├── IncidentType.java
│                   └── IncidentStatus.java
│
├── database/
│   └── cims.sql
│
├── pom.xml
├── README.md
├── .gitignore
└── LICENSE
```

---

# System Overview

The application follows a simple incident-management workflow:

```text
User
 │
 │ Reports Incident
 ▼
Incident
 │
 │ Risk Assessment
 ▼
Severity Classification
 │
 │ Admin Assignment
 ▼
Security Analyst
 │
 │ Investigation
 ▼
Status Updates
 │
 ▼
Resolution
```

The application is intentionally implemented as a **command-line system** so that it can be executed directly from a terminal without requiring a graphical interface.

---

# User Roles

## User

A regular user can:

- Log in
- Report a cybersecurity incident
- View incidents reported by them
- Log out

## Security Analyst

A security analyst can:

- View assigned incidents
- Update incident status
- Add investigation details
- View prioritized incidents
- Log out

## Admin

An administrator can:

- View all incidents
- Assign incidents to analysts
- View registered users
- View reports
- View audit logs
- Log out

Role-specific operations are restricted according to the authenticated user's role.

---

# Incident Types

The system supports the following incident categories:

1. Phishing
2. Malware
3. Unauthorized Access
4. Data Breach
5. Denial of Service

---

# Incident Lifecycle

An incident progresses through the following states:

```text
REPORTED
    ↓
  OPEN
    ↓
INVESTIGATING
    ↓
 RESOLVED
    ↓
  CLOSED
```

The incident status represents its current stage in the investigation process.

---

# Risk Assessment

Each reported incident is evaluated using three factors:

- **Impact** — 1 to 5
- **Likelihood** — 1 to 5
- **Exposure** — 1 to 5

The system calculates the risk score automatically:

```text
Risk Score = Impact × Likelihood × Exposure
```

The user does not manually enter the calculated risk score.

## Severity Classification

| Risk Score | Severity |
|---:|---|
| 1–20 | LOW |
| 21–50 | MEDIUM |
| 51–75 | HIGH |
| 76–125 | CRITICAL |

### Example

```text
Impact     = 5
Likelihood = 4
Exposure   = 3

Risk Score = 5 × 4 × 3
           = 60

Severity   = HIGH
```

---

# Priority Management

The application uses Java's `PriorityQueue` to organize incidents according to their risk scores.

Higher-risk incidents receive higher priority.

For example:

```text
Data Breach          Risk: 100
Malware              Risk: 80
Phishing             Risk: 45
Unauthorized Access  Risk: 20
```

This demonstrates the practical use of:

- Java Collections
- `PriorityQueue`
- `Comparator`

---

# Database Design

The application uses MySQL for persistent storage.

The database contains four main tables:

```text
users
incidents
investigations
audit_logs
```

## Users

Stores:

- User information
- Authentication details
- User roles

## Incidents

Stores:

- Incident details
- Incident type
- Risk factors
- Risk score
- Severity
- Status
- Reporter
- Assigned analyst

## Investigations

Stores:

- Investigation details
- Analyst findings
- Actions taken
- Related incident

## Audit Logs

Records important system activities such as:

- Incident creation
- Incident assignment
- Status changes
- Investigation updates

---

# Database Relationships

The main relationships are:

```text
USERS
  │
  ├── reported_by ────────► INCIDENTS
  │
  ├── assigned_to ────────► INCIDENTS
  │
  └── analyst_id ─────────► INVESTIGATIONS

INCIDENTS
  │
  └───────────────────────► INVESTIGATIONS

USERS
  │
  └───────────────────────► AUDIT_LOGS
```

A user can report multiple incidents, while an analyst can be assigned multiple incidents.

---

# Security Features

The project demonstrates basic secure programming practices appropriate for an academic application:

- Password hashing
- Role-based access control
- Input validation
- Prepared SQL statements
- Basic audit logging
- Role-restricted operations
- Database credentials kept outside the source code

> **Note:** This is an academic project and is not intended to represent a production-grade cybersecurity incident response platform.

---

# Java Concepts Demonstrated

## Object-Oriented Programming

The project demonstrates:

- Classes and objects
- Encapsulation
- Constructors
- Methods
- Enums
- Separation of responsibilities

## Collections

The application uses:

- `List`
- `PriorityQueue`
- `Comparator`

## Exception Handling

Examples include:

- `SQLException`
- `NumberFormatException`
- Input validation
- Error handling for invalid operations

## JDBC

The project uses:

- `Connection`
- `PreparedStatement`
- `ResultSet`
- `DriverManager`
- Try-with-resources

## Database Programming

The application demonstrates:

- SQL queries
- CRUD operations
- Primary keys
- Foreign keys
- Table relationships

---

# Application Flow

```text
                    ┌──────────────┐
                    │    LOGIN     │
                    └──────┬───────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
              ▼            ▼            ▼
            USER        ANALYST        ADMIN
              │            │            │
              ▼            ▼            ▼
       Report Incident  Assigned     View All
       View Incidents   Incidents    Incidents
                         │            │
                         ▼            ▼
                    Investigation   Assign Analyst
                         │          View Users
                         ▼          View Reports
                    Update Status   Audit Logs
```

---

# Requirements

Install the following before running the application:

- **Java Development Kit (JDK) 17 or later**
- **Maven**
- **MySQL 8 or later**
- **Git**

The application is designed to run from the **command line** and does not require a GUI framework.

---

# Installation and Setup

## 1. Clone the Repository

Open a terminal and run:

```bash
git clone https://github.com/Mathesh-Chand-K-V/Cybersecurity-Incident-Management-System.git
```

Move into the project directory:

```bash
cd Cybersecurity-Incident-Management-System
```

---

## 2. Verify Java Installation

Run:

```bash
java -version
```

The project requires Java 17 or later.

Also verify the Java compiler:

```bash
javac -version
```

---

## 3. Verify Maven Installation

Run:

```bash
mvn -version
```

Make sure Maven is using Java 17 or later.

---

# Database Setup

## 1. Start MySQL

Make sure the MySQL server is running.

The application expects a MySQL database named:

```text
cims
```

## 2. Create the Database

The repository contains the database script:

```text
database/cims.sql
```

You can execute it using MySQL:

```sql
SOURCE database/cims.sql;
```

Alternatively, open `database/cims.sql` in **MySQL Workbench** and execute the script.

The script creates the required database tables and sample data.

---

# Database Configuration

Configure the application's MySQL connection using your local database credentials.

The expected configuration is:

```text
Database: cims
Host: localhost
Port: 3306
Username: your_username
Password: your_password
```

Do **not** commit actual database passwords or other sensitive credentials to GitHub.

If the project uses environment variables or a local configuration file, configure those values before running the application.

---

# Build the Project

From the project root directory, run:

```bash
mvn clean package
```

If the build succeeds, Maven will compile the project and create the required build output.

---

# Run the Application

The application can be launched from the command line or through an IDE.

If the Maven configuration provides an executable JAR, run it using:

```bash
java -jar target/<generated-jar-name>.jar
```

Otherwise, run the `Main` class:

```text
src/main/java/com/cims/Main.java
```

The application starts with the login menu.

---

# Example Console

```text
========================================
 CYBERSECURITY INCIDENT MANAGEMENT SYSTEM
========================================

1. Login
2. Exit

Enter choice: 1

Username: analyst
Password: ******

Login successful.

--------- ANALYST MENU ---------

1. View Assigned Incidents
2. Update Incident Status
3. Add Investigation
4. View Priority Incidents
5. Logout
```

The exact menu options may vary depending on the implementation.

---

# Example Workflow

## Step 1 — Report an Incident

A user reports a suspicious email:

```text
Title: Suspicious Email

Type: PHISHING

Impact: 4
Likelihood: 5
Exposure: 3
```

The system calculates:

```text
Risk Score = 4 × 5 × 3
           = 60

Severity = HIGH
```

The user does not need to manually calculate or enter the severity.

---

## Step 2 — Assign the Incident

An administrator reviews the incident and assigns it to a security analyst.

```text
Incident
   ↓
Admin
   ↓
Security Analyst
```

The assignment is recorded in the database and audit log.

---

## Step 3 — Investigate

The analyst reviews the assigned incident and records investigation information.

Example:

```text
Findings:
User received a suspicious login email.

Actions:
Password reset recommended and malicious link blocked.
```

---

## Step 4 — Update the Status

The analyst updates the incident as the investigation progresses:

```text
REPORTED
    ↓
OPEN
    ↓
INVESTIGATING
    ↓
RESOLVED
```

The relevant changes are recorded by the system.

---

# Sample Data Flow

```text
User
 │
 │ Report
 ▼
Incident
 │
 ├── Impact
 ├── Likelihood
 └── Exposure
        │
        ▼
   Risk Calculator
        │
        ▼
 Risk Score + Severity
        │
        ▼
      Admin
        │
        ▼
 Assign Analyst
        │
        ▼
    Investigation
        │
        ▼
   Status Updates
        │
        ▼
     Resolution
```

---

# Limitations

The project intentionally keeps the implementation within the scope of a small academic application.

The current version does **not** include:

- GUI
- Web application
- REST API
- Spring Boot
- AI/ML
- Real-time security monitoring
- Network packet capture
- Malware execution
- Vulnerability scanning
- SIEM integration
- Cloud deployment
- Email/SMS notifications
- Multi-factor authentication
- Advanced digital forensics

These features are outside the scope of the current academic MVP.

---

# Future Enhancements

Potential future improvements include:

- Web interface using Spring Boot
- Stronger password hashing such as bcrypt or Argon2
- Multi-factor authentication
- Email notifications
- More detailed incident reporting
- Real-time security monitoring
- SIEM integration
- Advanced cybersecurity analytics

These are possible extensions and are not required for the current version.

---

# Academic Purpose

The project applies multiple concepts covered through Java and database programming:

```text
Java
  ↓
Object-Oriented Programming
  ↓
Collections
  ↓
Exception Handling
  ↓
JDBC
  ↓
MySQL
  ↓
Authentication
  ↓
Role-Based Access
  ↓
Basic Cybersecurity
```

The goal is to demonstrate these concepts through a **small, executable, command-line application** rather than introducing unnecessary frameworks or features.

---

# Submission Notes

This repository is intended to satisfy the project submission requirements by providing:

- A public GitHub repository
- A `README.md` at the repository root
- Step-by-step setup instructions
- Database setup instructions
- Dependency/build instructions
- Command-line execution instructions
- Source code
- Database script
- Project configuration files

The repository root URL for submission should follow this format:

```text
https://github.com/{github-username}/{repo-name}
```

Do **not** submit URLs containing:

```text
/tree/main/
```

or

```text
/blob/
```

The repository should be publicly accessible before submission.

---

# License

This project is licensed under the **MIT License**.

See the `LICENSE` file for details.

---

# Author

**Mathesh Chand K V**

B.Tech Computer Science and Engineering  
VIT Bhopal University

Academic Project
