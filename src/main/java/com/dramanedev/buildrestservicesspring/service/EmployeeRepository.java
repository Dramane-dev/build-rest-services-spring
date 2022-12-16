package com.dramanedev.buildrestservicesspring.service;

import com.dramanedev.buildrestservicesspring.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
}
