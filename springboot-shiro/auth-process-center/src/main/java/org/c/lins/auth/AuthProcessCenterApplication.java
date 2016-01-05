package org.c.lins.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"org.c.lins.auth"})
public class AuthProcessCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthProcessCenterApplication.class, args);
	}
}
