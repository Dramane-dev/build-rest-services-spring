package com.dramanedev.buildrestservicesspring.api;

import com.dramanedev.buildrestservicesspring.api.exception.EmployeeNotFoundException;
import com.dramanedev.buildrestservicesspring.model.EmployeeEntity;
import com.dramanedev.buildrestservicesspring.service.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    List<EmployeeEntity> getAll() {
        return employeeRepository.findAll();
    }

    @PostMapping("/employees")
    EmployeeEntity addEmployee(@RequestBody EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees/{employeeId}")
    EmployeeEntity getById(@PathVariable Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @PutMapping("/employees/{employeeId}")
    EmployeeEntity update(@RequestBody EmployeeEntity employeeUpdateInfos, @PathVariable Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employee -> {
                    employee.setName(employeeUpdateInfos.getName());
                    employee.setRole(employeeUpdateInfos.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    employeeUpdateInfos.setId(employeeId);
                    return employeeRepository.save(employeeUpdateInfos);
                });
    }

    @DeleteMapping("/employees/{employeeId}")
    void delete(@PathVariable Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}