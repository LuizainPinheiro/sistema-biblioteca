package br.com.fecaf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Bean

    CommandLineRunner initialization(){
        return args -> {
            System.out.println("Hello world!");
        };
    }
}
