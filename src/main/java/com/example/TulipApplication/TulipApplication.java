package com.example.TulipApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.TulipApplication.entities.Product;
import com.example.TulipApplication.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaAuditing
public class TulipApplication {

	public static void main(String[] args) {
		SpringApplication.run(TulipApplication.class, args);
	}



}
