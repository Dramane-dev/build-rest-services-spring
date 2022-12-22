package com.dramanedev.buildrestservicesspring.service;

import com.dramanedev.buildrestservicesspring.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
