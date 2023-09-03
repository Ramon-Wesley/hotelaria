package com.ramon_silva.projeto_hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ProjetoHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoHotelApplication.class, args);
	}

}
