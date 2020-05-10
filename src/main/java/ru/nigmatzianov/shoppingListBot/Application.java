package ru.nigmatzianov.shoppingListBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
	
    public static void main(String[] args) {
    	 SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationStartupRunner schedulerRunner() {
        return new ApplicationStartupRunner();
    }
}
