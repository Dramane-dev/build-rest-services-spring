package com.dramanedev.buildrestservicesspring.api.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long employeeId) {
        super("Could not find employee with id : " + employeeId);
    }
}
