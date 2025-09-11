# Employee Management System

A Spring Boot RESTful API for managing employees with CRUD operations.

## Features

- Create new employees
- Retrieve employee information by ID
- Get all employees
- Update existing employee details
- Delete employees
- Input validation and error handling
- H2 in-memory database for development
- Comprehensive unit and integration tests

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (for development)
- **Lombok** (to reduce boilerplate code)
- **JUnit 5** and **Mockito** (for testing)
- **Gradle** (build tool)

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7.x or higher

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd employee-management
```

2. Build and run the application:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### H2 Database Console

You can access the H2 database console at: `http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:employeedb`
- **Username**: `sa`
- **Password**: `password`

## API Endpoints

### Base URL: `/api/v1/employees`

### 1. Create Employee
- **POST** `/api/v1/employees`
- **Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Engineering",
  "position": "Software Engineer"
}
```
- **Response**: `201 Created`
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Engineering",
  "position": "Software Engineer",
  "createdAt": "2023-11-15T10:30:00",
  "updatedAt": "2023-11-15T10:30:00"
}
```

### 2. Get Employee by ID
- **GET** `/api/v1/employees/{id}`
- **Response**: `200 OK`
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Engineering",
  "position": "Software Engineer",
  "createdAt": "2023-11-15T10:30:00",
  "updatedAt": "2023-11-15T10:30:00"
}
```

### 3. Get All Employees
- **GET** `/api/v1/employees`
- **Response**: `200 OK`
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "department": "Engineering",
    "position": "Software Engineer",
    "createdAt": "2023-11-15T10:30:00",
    "updatedAt": "2023-11-15T10:30:00"
  }
]
```

### 4. Update Employee
- **PUT** `/api/v1/employees/{id}`
- **Request Body**:
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "department": "Marketing",
  "position": "Marketing Manager"
}
```
- **Response**: `200 OK`

### 5. Delete Employee
- **DELETE** `/api/v1/employees/{id}`
- **Response**: `204 No Content`

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "firstName": "First name is required",
    "email": "Email should be valid"
  },
  "timestamp": "2023-11-15T10:30:00"
}
```

### Employee Not Found (404 Not Found)
```json
{
  "status": 404,
  "message": "Employee not found with ID: 123",
  "timestamp": "2023-11-15T10:30:00"
}
```

### Employee Already Exists (409 Conflict)
```json
{
  "status": 409,
  "message": "Employee with email john.doe@example.com already exists",
  "timestamp": "2023-11-15T10:30:00"
}
```

## Testing

Run all tests:
```bash
./gradlew test
```

Run specific test class:
```bash
./gradlew test --tests EmployeeServiceTest
```

## Sample Data

The application comes with sample employee data that is automatically loaded on startup. You can see 5 sample employees when you start the application.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/employee/
│   │       ├── EmployeeManagementApplication.java
│   │       ├── config/
│   │       │   └── SecurityConfig.java
│   │       ├── controller/
│   │       │   └── EmployeeController.java
│   │       ├── dto/
│   │       │   ├── CreateEmployeeRequest.java
│   │       │   ├── UpdateEmployeeRequest.java
│   │       │   └── EmployeeResponse.java
│   │       ├── exception/
│   │       │   ├── EmployeeNotFoundException.java
│   │       │   ├── EmployeeAlreadyExistsException.java
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── model/
│   │       │   └── Employee.java
│   │       ├── repository/
│   │       │   └── EmployeeRepository.java
│   │       └── service/
│   │           └── EmployeeService.java
│   └── resources/
│       ├── application.yml
│       └── data.sql
└── test/
    ├── java/
    │   └── com/example/employee/
    │       ├── controller/
    │       │   └── EmployeeControllerIntegrationTest.java
    │       └── service/
    │           └── EmployeeServiceTest.java
    └── resources/
        └── application-test.yml
```

## Architecture and Design Patterns

This application follows Spring Boot best practices:

- **Layered Architecture**: Clear separation between Controller, Service, and Repository layers
- **Dependency Injection**: Constructor-based injection for better testability
- **DTO Pattern**: Using DTOs to separate API contracts from internal data models
- **Exception Handling**: Global exception handler for consistent error responses
- **Validation**: Bean validation using JSR 380 annotations
- **Testing**: Comprehensive unit and integration tests

## Future Enhancements

- Add pagination and sorting for employee listing
- Implement employee search and filtering
- Add authentication and authorization
- Use PostgreSQL or MySQL for production
- Add API documentation with Swagger/OpenAPI
- Implement audit logging
- Add caching layer
- Docker containerization
This repo is created to leverage GitHub copilot features.
