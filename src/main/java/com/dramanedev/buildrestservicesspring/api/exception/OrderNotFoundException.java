package com.dramanedev.buildrestservicesspring.api.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) { super("Could not find employee with id : " + orderId); }
}
