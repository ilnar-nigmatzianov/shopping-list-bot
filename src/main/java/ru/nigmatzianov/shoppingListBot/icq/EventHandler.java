package ru.nigmatzianov.shoppingListBot.icq;

import ru.mail.im.botapi.fetcher.event.Event;

public interface EventHandler {
    void handle(Event<?> event);
}
