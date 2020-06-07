package ru.nigmatzianov.shoppingListBot.icq;

import org.springframework.stereotype.Service;
import ru.mail.im.botapi.BotApiClient;
import ru.nigmatzianov.shoppingListBot.icq.dialog.Dialog;
import ru.nigmatzianov.shoppingListBot.icq.dialog.DialogStateFactory;

@Service
public class Client {
    private final DialogStateFactory dialogStateFactory;
    private final BotApiClient botApiClient;
    private final MessageLogger messageLogger;

    public Client(DialogStateFactory dialogStateFactory, BotApiClient botApiClient, MessageLogger messageLogger) {
       this.dialogStateFactory = dialogStateFactory;
       this.botApiClient = botApiClient;
       this.messageLogger = messageLogger;
    }

    public void run() {
        botApiClient.addOnEventFetchListener(events -> events.forEach(event -> {
            messageLogger.log(event);
            //Maybe this should be in separate thread?
            Dialog dialog = new Dialog(dialogStateFactory);
            dialog.handle(event);
        }));
    }

}