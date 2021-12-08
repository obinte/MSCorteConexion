package com.acosux.MSCorteConexion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class MSCorteConexionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MSCorteConexionApplication.class, args);
	}

}
