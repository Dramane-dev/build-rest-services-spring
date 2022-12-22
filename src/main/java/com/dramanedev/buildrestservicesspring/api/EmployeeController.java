package com.dramanedev.buildrestservicesspring.api;

import com.dramanedev.buildrestservicesspring.api.exception.EmployeeNotFoundException;
import com.dramanedev.buildrestservicesspring.model.EmployeeEntity;
import com.dramanedev.buildrestservicesspring.model.EmployeeModelAssembler;
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
    private final EmployeeModelAssembler assembler;

    EmployeeController(
            EmployeeRepository employeeRepository,
            EmployeeModelAssembler assembler
    ) {
        this.employeeRepository = employeeRepository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<EmployeeEntity>> getAll() {
        List<EntityModel<EmployeeEntity>> employees = employeeRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAll()).withSelfRel());
    }

    @PostMapping("/employees")
    EmployeeEntity addEmployee(@RequestBody EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees/{employeeId}")
    public EntityModel<EmployeeEntity> getById(@PathVariable Long employeeId) {
        EmployeeEntity employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return assembler.toModel(employee);
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