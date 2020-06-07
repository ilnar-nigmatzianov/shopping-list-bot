package ru.nigmatzianov.shoppingListBot.icq;

import org.springframework.stereotype.Service;
import ru.mail.im.botapi.fetcher.event.Event;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;
import ru.nigmatzianov.shoppingListBot.service.MessageService;

@Service
public class MessageLogger {
    private final MessageService messageService;

    public MessageLogger(MessageService messageService) {
        this.messageService = messageService;
    }

    public void log(Event<?> event) {
        if (event instanceof NewMessageEvent) {
            messageService.storeByIcqEvent((NewMessageEvent) event);
        }
    }
}
