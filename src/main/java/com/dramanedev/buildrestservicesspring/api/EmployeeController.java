package com.dramanedev.buildrestservicesspring.api;

import com.dramanedev.buildrestservicesspring.api.exception.EmployeeNotFoundException;
import com.dramanedev.buildrestservicesspring.model.EmployeeEntity;
import com.dramanedev.buildrestservicesspring.model.EmployeeModelAssembler;
import com.dramanedev.buildrestservicesspring.service.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<?> addEmployee(@RequestBody EmployeeEntity employee) {
        EntityModel<EmployeeEntity> entityModel = assembler.toModel(employeeRepository.save(employee));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{employeeId}")
    public EntityModel<EmployeeEntity> getById(@PathVariable Long employeeId) {
        EmployeeEntity employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{employeeId}")
    ResponseEntity<?> update(@RequestBody EmployeeEntity employeeUpdateInfos, @PathVariable Long employeeId) {
        EmployeeEntity updatedEmployee = employeeRepository.findById(employeeId)
                .map(employee -> {
                    employee.setName(employeeUpdateInfos.getName());
                    employee.setRole(employeeUpdateInfos.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    employeeUpdateInfos.setId(employeeId);
                    return employeeRepository.save(employeeUpdateInfos);
                });
        EntityModel<EmployeeEntity> entityModel = assembler.toModel(updatedEmployee);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/employees/{employeeId}")
    ResponseEntity<?> delete(@PathVariable Long employeeId) {

        employeeRepository.deleteById(employeeId);
        return ResponseEntity.noContent().build();
    }
}