package ru.nigmatzianov.shoppingListBot.icq.dialog;

import org.springframework.stereotype.Component;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.nigmatzianov.shoppingListBot.domain.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChooseHowToCreateList implements State {
    private final BotApiClientController controller;
    private User user;

    public ChooseHowToCreateList( BotApiClientController botApiClientController) {
        this.controller = botApiClientController;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void react() {
        ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        ArrayList<InlineKeyboardButton> firstLine = new ArrayList<>();
        firstLine.add(InlineKeyboardButton.callbackButton(
                "Из сообщения выше",
                "based-on-previous-message"
        ));
        keyboard.add(firstLine);

        ArrayList<InlineKeyboardButton> secondLine = new ArrayList<>();
        secondLine.add(InlineKeyboardButton.callbackButton(
                "Новый пустой",
                "new"
        ));
        keyboard.add(secondLine);

        SendTextRequest request = new SendTextRequest();
        request
                .setChatId(user.getChatId())
                .setKeyboard(keyboard)
                .setText("Создать новый список");
        try {
            controller.sendTextMessage(request);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
