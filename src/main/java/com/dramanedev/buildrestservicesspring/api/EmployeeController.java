package com.dramanedev.buildrestservicesspring.api;

import com.dramanedev.buildrestservicesspring.api.exception.EmployeeNotFoundException;
import com.dramanedev.buildrestservicesspring.model.EmployeeEntity;
import com.dramanedev.buildrestservicesspring.service.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<EmployeeEntity>> getAll() {
        List<EntityModel<EmployeeEntity>> employees = employeeRepository
                .findAll()
                .stream()
                .map(employee -> EntityModel.of(
                        employee,
                        linkTo(methodOn(EmployeeController.class).getById(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees")
                        )

                )
                .collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAll()).withSelfRel());
    }

    @PostMapping("/employees")
    EmployeeEntity addEmployee(@RequestBody EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees/{employeeId}")
    EntityModel<EmployeeEntity> getById(@PathVariable Long employeeId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getById(employeeId)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees")
        );
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