package com.dramanedev.buildrestservicesspring.model;

import com.dramanedev.buildrestservicesspring.api.EmployeeController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<EmployeeEntity, EntityModel<EmployeeEntity>> {
    @Override
    public EntityModel<EmployeeEntity> toModel(EmployeeEntity employeeEntity) {
        return EntityModel.of(
                    employeeEntity,
                    linkTo(methodOn(EmployeeController.class).getById(employeeEntity.getId())).withSelfRel(),
                    linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees")
                );
    }
}
