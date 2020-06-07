package ru.nigmatzianov.shoppingListBot.icq.dialog;

import ru.mail.im.botapi.fetcher.event.Event;

public class Dialog {
    private final DialogStateFactory stateFactory;

    public Dialog(DialogStateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    public void handle(Event<?> event) {
        try {
            State state = stateFactory.getState(event);
            state.react();
        } catch (UnsupportedEventException exception) {
            // logging exception
        }

    }
}
