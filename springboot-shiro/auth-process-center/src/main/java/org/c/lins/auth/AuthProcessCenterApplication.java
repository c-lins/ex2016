package org.c.lins.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages={"org.c.lins.auth"})
@ImportResource("classpath:spring-config-shiro.xml")
public class AuthProcessCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthProcessCenterApplication.class, args);
	}
}
