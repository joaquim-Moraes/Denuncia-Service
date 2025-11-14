package com.example.Denuncia_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DenunciaServiceApplication {

	public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        // Puxando a chave secret
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        // PUxando api e url do IPQS do env
        System.setProperty("IPQS_KEY", dotenv.get("IPQS_KEY"));
        System.setProperty("IPQS_URL", dotenv.get("IPQS_URL"));

        SpringApplication.run(DenunciaServiceApplication.class, args);
}}
