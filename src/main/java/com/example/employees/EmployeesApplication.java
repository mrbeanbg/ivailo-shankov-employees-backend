package com.example.employees;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Employees API",
				contact = @Contact(
						name = "Ivailo Shankov",
						email = "ivailoss@gmail.com"
				)
		)
)

@SpringBootApplication
public class EmployeesApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmployeesApplication.class, args);
	}
}
