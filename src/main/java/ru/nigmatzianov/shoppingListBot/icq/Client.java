package ru.nigmatzianov.shoppingListBot.icq;

import org.springframework.stereotype.Service;
import ru.mail.im.botapi.BotApiClient;
import java.util.List;

@Service
public class Client {
    private final List<EventHandler> handlers;
    private final BotApiClient botApiClient;

    public Client(List<EventHandler> handlers, BotApiClient botApiClient) {
       this.handlers = handlers;
       this.botApiClient = botApiClient;
    }

    public void run() {
        botApiClient.addOnEventFetchListener(events -> events.forEach(event -> {
            handlers.forEach(handler -> handler.handle(event));
        }));
    }

}