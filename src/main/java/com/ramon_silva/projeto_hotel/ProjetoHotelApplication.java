package com.ramon_silva.projeto_hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoHotelApplication.class, args);
	}

}
//SELECT * FROM `reservas` WHERE data_de_check_in >= 2023-08-25 and data_de_check_out <= 2023-08-25 