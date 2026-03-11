package com.qualifyguru.qualify_guru_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class QualifyGuruApplication {

	public static void main(String[] args) {
		SpringApplication.run(QualifyGuruApplication.class, args);
	}
	@Bean
	public CommandLineRunner generateRealHash(PasswordEncoder passwordEncoder) {
		return args -> {
			System.out.println("==========================================================");
			System.out.println("COPIE ESTE HASH E FAÇA O UPDATE NO MYSQL PARA O TESTE:");
			System.out.println(passwordEncoder.encode("senha123"));
			System.out.println("==========================================================");
		};
	}
}
