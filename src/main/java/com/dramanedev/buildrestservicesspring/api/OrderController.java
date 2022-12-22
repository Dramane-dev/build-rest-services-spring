package com.dramanedev.buildrestservicesspring.api;

import com.dramanedev.buildrestservicesspring.api.exception.OrderNotFoundException;
import com.dramanedev.buildrestservicesspring.constants.Status;
import com.dramanedev.buildrestservicesspring.model.OrderEntity;
import com.dramanedev.buildrestservicesspring.model.OrderModelAssembler;
import com.dramanedev.buildrestservicesspring.service.OrderRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderModelAssembler assembler;

    OrderController(
            OrderRepository orderRepository,
            OrderModelAssembler assembler
    ) {
        this.orderRepository = orderRepository;
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<OrderEntity>> getAll() {
        List<EntityModel<OrderEntity>> orders = orderRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return  CollectionModel.of(
                orders,
                linkTo(methodOn(OrderController.class).getAll()).withSelfRel()
        );
    }

    @GetMapping("/orders/{orderId}")
    public EntityModel<OrderEntity> getById(@PathVariable Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return assembler.toModel(orderEntity);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<OrderEntity>> addOrder(@RequestBody OrderEntity orderEntity) {
        orderEntity.setStatus(Status.IN_PROGRESS);
        OrderEntity newOrder = orderRepository.save(orderEntity);

        return ResponseEntity
                .created(
                        linkTo(methodOn(OrderController.class).getById(newOrder.getId())).toUri()
                )
                .body(assembler.toModel(newOrder));
    }

    @PutMapping("/orders/{orderId}/complete")
    public ResponseEntity<?> complete(@PathVariable Long orderId) {
        OrderEntity orderEntity = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (orderEntity.getStatus() == Status.IN_PROGRESS) {
            orderEntity.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(orderEntity)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(
                        Problem.create()
                                .withTitle("Method not allowed")
                                .withDetail("You can't cancel an order that is in the " + orderEntity.getStatus() + " status")
                );
    }

    @DeleteMapping("/orders/{orderId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long orderId) {
        OrderEntity orderEntity = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (orderEntity.getStatus() == Status.IN_PROGRESS) {
            orderEntity.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(orderEntity)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(
                        Problem.create()
                                .withTitle("Method not allowed")
                                .withDetail("You can't cancel an order that is in the " + orderEntity.getStatus() + " status")
                );
    }
}
