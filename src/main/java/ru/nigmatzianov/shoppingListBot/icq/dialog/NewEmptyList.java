package ru.nigmatzianov.shoppingListBot.icq.dialog;

import org.springframework.stereotype.Component;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.service.ShoppingListService;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewEmptyList implements State {
    private final ShoppingListService shoppingListService;
    private final BotApiClientController controller;
    private User user;

    public NewEmptyList(ShoppingListService shoppingListService, BotApiClientController controller) {
        this.shoppingListService = shoppingListService;
        this.controller = controller;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void react() {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setOwner(user);
        shoppingListService.create(shoppingList);

        ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        ArrayList<InlineKeyboardButton> firstLine = new ArrayList<>();
        firstLine.add(InlineKeyboardButton.callbackButton(
                "Готово",
                "done"
        ));
        keyboard.add(firstLine);

        SendTextRequest request = new SendTextRequest();
        request
                .setChatId(user.getChatId())
                .setKeyboard(keyboard)
                .setText("Нажмите готово если это все");
        try {
            controller.sendTextMessage(request);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
