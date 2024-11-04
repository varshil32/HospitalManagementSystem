# Hospital Management System

## Overview
The **Hospital Management System** is a Java-based console application designed to manage hospital operations efficiently. This system provides functionalities for adding and viewing patients, doctors, and appointments, as well as booking and managing appointments. It includes user roles for administrators and receptionists, each with distinct access privileges.

## Features
- **Admin Functions**:
  - Add and delete patients and doctors
  - View patient and doctor details
  - Book and manage appointments
  - Delete appointments
- **Receptionist Functions**:
  - Add and view patient records
  - View doctor details
  - Book appointments
- **Secure Login**:
  - Role-based access control for admin and receptionist.

## Technology Stack
- **Programming Language**: Java
- **Database**: MySQL
- **JDBC Driver**: MySQL Connector/J
- **IDE**: IntelliJ IDEA
- **Database Management**: MySQL Workbench 

## Requirements
- **Java**: JDK 8 or higher
- **MySQL**: Version 5.7 or higher
- **JDBC Driver**: MySQL Connector/J version compatible with the MySQL server
- **IDE**: IntelliJ IDEA, Eclipse, or similar

## Setup and Installation
1. **Database Configuration**:
   - Create a MySQL database named `hospital`.
   - Execute SQL scripts given in folder `Database` to create tables for `patients`, `doctors`, `appointments`.
   - Ensure the database user credentials match those in the code.

2. **Project Setup**:
   - Clone or download the project repository.
   - Import the project into your preferred IDE.
   - Ensure the JDBC driver (`mysql-connector-java-x.x.x.jar`) is in the classpath.
   - Update database connection parameters in the `HospitalManagementSystem.java` file if necessary.

3. **Compile and Run**:
   - Compile the Java files.
   - Run the `HospitalManagementSystem` class as a Java application.

## How to Use
- **Main Menu**:
  - Admins can access all features, including adding and removing records.
  - Receptionists have limited access, primarily to manage patients and appointments.

## Code Structure
- **HospitalManagementSystem.java**: Main class handling authentication, role-based menu, and overall logic.
- **Patient.java**: Class for managing patient-related operations.
- **Doctor.java**: Class for doctor-related operations.
- **AppointmentViewer.java**: Class to display booked appointments.

