# EmployeeManagementSystem
An employee management system based on java fx and built using maven
# Employee Management System

## Table of Contents

- [Introduction]
- [Features]
- [Technologies Used]
- [Setup Instructions]
- [Project Structure]
- [Database Schema]
- [Usage]
- [Screenshots]
- [Contributors]

## Introduction

The Employee Management System is a JavaFX application designed to manage various employee-related operations. It provides functionality for managing employee profiles, leave applications, task assignments, performance reviews, and payroll details.

## Features

- View Employee List
- Employee Profile Management
- Leave Application Management
- Task Management for Employees and Managers
- Performance Management
- Report Generation:
    - Employee Performance Summary Report
    - Leave Management Analysis Report
    - Task Completion Efficiency Report
    - Departmental Productivity Report
    - View Employee Payroll Details
    - Generate Monthly Payroll Report

## Technologies Used

- Java
- JavaFX
- MySQL
- Maven

## Setup Instructions

### Prerequisites

- Java JDK 17 or higher
- MySQL
- Maven
- JavaFX SDK

### Steps

1. **Clone the repository:**
    
    ```bash
    git clone <https://github.com/junainanur/EmployeeManagementSystem.git>
    cd EmployeeManagementSystem
    
    ```
    
2. **Set up the MySQL Database:**
    - Create a database named `EmployeeManagement`.
    - Execute the SQL scripts to create tables and insert initial data.
3. **Configure Database Connection:**
    - Update the `DB_URL`, `DB_USER`, and `DB_PASSWORD` in `MainController.java` and `EmployeeListController.java`.
4. **Build and Run the Application:**
    
    ```bash
    mvn clean install
    mvn javafx:run
    ```
    

## Database Schema

- **employees**
    - id (INT, PRIMARY KEY)
    - name (VARCHAR)
    - position (VARCHAR)
    - department (VARCHAR)
    - base_salary (DOUBLE)
    - performance_factor (DOUBLE)
- **leave_requests**
    - id (INT, PRIMARY KEY)
    - employee_id (INT, FOREIGN KEY)
    - start_date (DATE)
    - end_date (DATE)
    - status (VARCHAR)
- **tasks**
    - id (INT, PRIMARY KEY)
    - employee_id (INT, FOREIGN KEY)
    - task_description (VARCHAR)
    - completion_status (VARCHAR)

## Usage

### Main Features

### View Employees

- Click on the "View Employees" button to see the list of all employees.

### Employee Profile

- Click on the "Employee Profile" button to view and edit the profile of the selected employee.

### Leave Application

- Click on the "Leave Application" button to submit a leave request.

### Manager Leave Functions

- Click on the "Manager Leave Functions" button to view, approve, or reject leave requests.

### Task Management

- Click on the "Employee Task Management" or "Manager Task Management" buttons to manage tasks.

### Performance Management

- Click on the "Performance Management" button to manage employee performance reviews.

### Report Generation

- Click on the respective report generation buttons to generate various reports.

### View Employee Payroll Details

- Click on the "View Employee Payroll Details" button to view payroll details of a selected employee.

### Generate Monthly Payroll Report

- Click on the "Generate Monthly Payroll Report" button to generate the monthly payroll report.

## Contributors

- Esha Riaz
- Warda Aziz
- Amna Noor
- Nur Junaina
