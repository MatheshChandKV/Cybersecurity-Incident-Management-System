# Cybersecurity Incident Management System

A simple **Java-based Cybersecurity Incident Management System** for reporting, tracking, prioritizing, and investigating cybersecurity incidents.

The project is designed as a **small academic Java application** that demonstrates Object-Oriented Programming, JDBC, MySQL, exception handling, collections, authentication, and basic cybersecurity concepts.

---

## Features

* User authentication
* Role-based access
* Three user roles:

  * Admin
  * Security Analyst
  * User
* Report cybersecurity incidents
* View reported incidents
* Assign incidents to analysts
* Update incident status
* Add investigation details
* Automatic risk calculation
* Incident severity classification
* Priority-based incident management
* Basic audit logging
* Simple console reports
* MySQL database integration

---

## Technologies Used

* **Java 17**
* **Maven**
* **JDBC**
* **MySQL 8**
* **Git & GitHub**

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

## User Roles

### User

A normal user can:

* Login
* Report an incident
* View their reported incidents
* Logout

### Security Analyst

An analyst can:

* View assigned incidents
* Update incident status
* Add investigation details
* View high-priority incidents
* Logout

### Admin

An administrator can:

* View all incidents
* Assign incidents to analysts
* View users
* View reports
* View audit logs
* Logout

---

## Incident Types

The system supports five common cybersecurity incident categories:

* Phishing
* Malware
* Unauthorized Access
* Data Breach
* Denial of Service

---

## Incident Lifecycle

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

The status represents the current stage of an incident.

---

## Risk Assessment

Each incident is evaluated using three factors:

* **Impact** – 1 to 5
* **Likelihood** – 1 to 5
* **Exposure** – 1 to 5

The risk score is calculated automatically:

```text
Risk Score = Impact × Likelihood × Exposure
```

### Severity

| Risk Score | Severity |
| ---------: | -------- |
|       1–20 | LOW      |
|      21–50 | MEDIUM   |
|      51–75 | HIGH     |
|     76–125 | CRITICAL |

### Example

```text
Impact     = 5
Likelihood = 4
Exposure   = 3

Risk Score = 5 × 4 × 3
           = 60

Severity   = HIGH
```

The user does not manually enter the risk score or severity.

---

## Priority Management

The project uses Java's **`PriorityQueue`** to prioritize incidents.

Incidents with higher risk scores are displayed first.

Example:

```text
Data Breach          Risk: 100
Malware              Risk: 80
Phishing             Risk: 45
Unauthorized Access  Risk: 20
```

This demonstrates the practical use of:

* Java Collections
* `PriorityQueue`
* `Comparator`

---

## Database

The application uses MySQL with four main tables:

```text
users
incidents
investigations
audit_logs
```

### Users

Stores user authentication and role information.

### Incidents

Stores reported cybersecurity incidents, risk information, status, and assignment.

### Investigations

Stores analyst findings and actions taken during an investigation.

### Audit Logs

Records important system activities such as incident creation, assignment, status updates, and investigations.

---

## Database Relationships

```text
users
  │
  ├─────────── reported_by ────────┐
  │                                ↓
  ├─────────── assigned_to ──── incidents
  │                                │
  │                                ↓
  └────────── analyst_id ─── investigations
  │
  └──────────────────────── audit_logs
```

---

## Security Features

The project demonstrates basic secure programming practices:

* Password hashing
* Role-based access
* Input validation
* Prepared SQL statements
* Basic audit logging
* Restricted access to role-specific operations
* Database credentials kept outside source code

> This is an educational project and is not intended to be a production-grade cybersecurity system.

---

## Java Concepts Demonstrated

The project demonstrates several important Java concepts:

### Object-Oriented Programming

* Classes and objects
* Encapsulation
* Constructors
* Methods
* Enums

### Collections

* `List`
* `PriorityQueue`
* `Comparator`

### Exception Handling

* `SQLException`
* `NumberFormatException`
* Input validation
* User-friendly error handling

### JDBC

* `Connection`
* `PreparedStatement`
* `ResultSet`
* `DriverManager`
* Try-with-resources

### Database Programming

* SQL queries
* CRUD operations
* Primary keys
* Foreign keys
* Relationships

---

## Application Flow

```text
        ┌──────────────┐
        │    Login     │
        └──────┬───────┘
               │
       ┌───────┴────────┐
       │                │
     USER            ANALYST           ADMIN
       │                │                │
       ↓                ↓                ↓
 Report Incident   View Assigned    View All
 View Incidents    Update Status    Assign Analyst
                   Investigation    Reports
                   Priority         Audit Logs
```

---

## Requirements

Before running the project, install:

* Java 17 or later
* MySQL 8 or later
* Maven
* Git

---

## Database Setup

### 1. Create the database

Open MySQL and run:

```sql
SOURCE database/cims.sql;
```

Or open `cims.sql` in MySQL Workbench and execute it.

The script creates the required database tables and sample data.

---

## Configuration

Configure the MySQL connection in the project.

Example:

```text
Database: cims
Host: localhost
Port: 3306
Username: your_username
Password: your_password
```

Do not commit real database passwords to GitHub.

---

## Running the Project

Clone the repository:

```bash
git clone https://github.com/Mathesh-Chand-K-V/Cybersecurity-Incident-Management-System.git
```

Move into the project:

```bash
cd Cybersecurity-Incident-Management-System
```

Build the project:

```bash
mvn clean package
```

Run the application from your IDE or using the generated Java classes/JAR.

---

## Example Console

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

---

## Sample Workflow

### 1. User reports an incident

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

### 2. Admin assigns the incident

The administrator assigns the incident to a security analyst.

### 3. Analyst investigates

The analyst records:

```text
Findings:
User received a suspicious login email.

Actions:
Password reset recommended and malicious link blocked.
```

### 4. Analyst updates the status

```text
REPORTED
    ↓
OPEN
    ↓
INVESTIGATING
    ↓
RESOLVED
```

---

## Limitations

This project intentionally keeps the scope small.

It does not include:

* GUI
* Web application
* REST API
* Spring Boot
* AI/ML
* Real-time monitoring
* Network packet capture
* Malware execution
* Vulnerability scanning
* SIEM integration
* Cloud deployment
* Email/SMS notifications
* Multi-factor authentication
* Advanced digital forensics

These features are outside the scope of the academic MVP.

---

## Future Enhancements

Possible future improvements include:

* Web interface using Spring Boot
* Improved password hashing using bcrypt or Argon2
* Multi-factor authentication
* Email notifications
* Advanced incident reports
* Real-time security monitoring
* SIEM integration
* Advanced cybersecurity analytics

---

## Academic Purpose

This project was developed to demonstrate practical knowledge of:

```text
Java
  ↓
OOP
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
Basic Cybersecurity
```

The project focuses on implementing these concepts in a **small, understandable, and practical application** rather than building an unnecessarily complex system.

---

## License

This project is licensed under the **MIT License**.

See the `LICENSE` file for details.

---

## Author

**Mathesh Chand K V**

B.Tech Computer Science and Engineering

VIT Bhopal University
