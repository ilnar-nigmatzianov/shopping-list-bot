package ru.nigmatzianov.shoppingListBot.icq.dialog.state;

import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.nigmatzianov.shoppingListBot.domain.User;

import java.util.ArrayList;
import java.util.List;

public class Create implements StateInterface {
    private final User user;

    public Create(User user) {
        this.user = user;
    }

    @Override
    public void action(BotApiClientController controller) {
        // Create a keyboard
        ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        ArrayList<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.callbackButton(
                "Из сообщения выше",
                "based-on-previous-message"
        ));
        buttons.add(InlineKeyboardButton.callbackButton(
                "Новый пустой",
                "new"
        ));
        keyboard.add(buttons);

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
