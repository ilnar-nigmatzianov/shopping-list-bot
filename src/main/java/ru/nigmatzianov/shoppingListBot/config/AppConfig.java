package ru.nigmatzianov.shoppingListBot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mail.im.botapi.BotApiClient;
import ru.mail.im.botapi.BotApiClientController;
import ru.nigmatzianov.shoppingListBot.icq.EventHandler;

import java.util.SortedSet;

@Configuration
public class AppConfig {

    @Bean
    public BotApiClient botApiClient() {
        return new BotApiClient("001.3477817321.0868011592:752116872");
    }

    @Bean
    public BotApiClientController botApiClientController(BotApiClient botApiClient) {
        return BotApiClientController.startBot(botApiClient);
    }
}
