package ru.nigmatzianov.shoppingListBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import ru.nigmatzianov.shoppingListBot.icq.Client;

public class ApplicationStartupRunner implements CommandLineRunner {
    @Autowired
    private Client client;

    public void run(String... args) {
        client.run();
    }

}
