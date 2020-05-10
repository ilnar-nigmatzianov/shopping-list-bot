package ru.nigmatzianov.shoppingListBot.icq.dialog.state;

import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingListItem;
import ru.nigmatzianov.shoppingListBot.domain.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Edit implements StateInterface {
    private final User user;

    public Edit(User user) {
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
                "based-on-previous-message"
        ));
        keyboard.add(buttons);


    }
}
