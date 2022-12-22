package com.dramanedev.buildrestservicesspring.model;

import com.dramanedev.buildrestservicesspring.api.OrderController;
import com.dramanedev.buildrestservicesspring.constants.Status;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderEntity, EntityModel<OrderEntity>> {
    @Override
    public EntityModel<OrderEntity> toModel(OrderEntity orderEntity) {
        EntityModel<OrderEntity> orderModel = EntityModel.of(
                orderEntity,
                linkTo(methodOn(OrderController.class).getById(orderEntity.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAll()).withRel("orders")
        );

        if (orderEntity.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(
                    linkTo(methodOn(OrderController.class).cancel(orderEntity.getId())).withRel("cancel")
            );
            orderModel.add(
                    linkTo(methodOn(OrderController.class).complete(orderEntity.getId())).withRel("complete")
            );
        }

        return orderModel;
    }
}
