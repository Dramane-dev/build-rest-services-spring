package com.dramanedev.buildrestservicesspring.database;

import com.dramanedev.buildrestservicesspring.model.EmployeeEntity;
import com.dramanedev.buildrestservicesspring.service.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
        return args -> {
            log.info(
                    "Preloading " + employeeRepository.save(
                            new EmployeeEntity(
                                    "Dramane",
                                    "KAMISSOKO",
                                    "Developer"
                            )
                    )
            );
            log.info(
                    "Preloading " + employeeRepository.save(
                            new EmployeeEntity(
                                    "Dramane",
                                    "KAMISSOKO",
                                    "Web Developer"
                            )
                    )
            );
        };
    }
}
