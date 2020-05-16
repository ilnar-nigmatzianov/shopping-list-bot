package ru.nigmatzianov.shoppingListBot.icq;

import org.junit.jupiter.api.Test;
import ru.mail.im.botapi.fetcher.event.Event;
import ru.mail.im.botapi.fetcher.event.EventVisitor;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;

public class NewMessageHandlerTest {
    @Test
    public void handle()
    {
        Event<NewMessageEvent> event = new Event<>() {
            @Override
            public <IN, OUT> OUT accept(EventVisitor<IN, OUT> visitor, IN in) {
                return null;
            }
        };
    }
}
