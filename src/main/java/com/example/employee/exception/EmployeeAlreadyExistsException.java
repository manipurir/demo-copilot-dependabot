package com.example.employee.exception;

public class EmployeeAlreadyExistsException extends RuntimeException {
    
    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}
