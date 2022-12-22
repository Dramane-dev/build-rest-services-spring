package com.dramanedev.buildrestservicesspring.database;

import com.dramanedev.buildrestservicesspring.constants.Status;
import com.dramanedev.buildrestservicesspring.model.EmployeeEntity;
import com.dramanedev.buildrestservicesspring.model.OrderEntity;
import com.dramanedev.buildrestservicesspring.service.EmployeeRepository;
import com.dramanedev.buildrestservicesspring.service.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            EmployeeRepository employeeRepository,
            OrderRepository orderRepository
    ) {
        return args -> {
            employeeRepository.save(
                    new EmployeeEntity(
                            "Dramane",
                            "KAMISSOKO",
                            "Developer"
                    )
            );
            employeeRepository.save(
                    new EmployeeEntity(
                            "Dramane",
                            "KAMISSOKO",
                            "Web Developer"
                    )
            );

            orderRepository.save(
                new OrderEntity(
                        "Commande d'un sac pour ma femme d'amour",
                        Status.IN_PROGRESS
                )
            );

            orderRepository.save(
                new OrderEntity(
                        "Commande d'un cadeau pour ma maman d'amour",
                        Status.IN_PROGRESS
                )
            );

            employeeRepository
                    .findAll()
                    .forEach(employee -> log.info("Preloaded " + employee));
            orderRepository
                    .findAll()
                    .forEach(order -> log.info("Preloaded " + order));
        };
    }
}
