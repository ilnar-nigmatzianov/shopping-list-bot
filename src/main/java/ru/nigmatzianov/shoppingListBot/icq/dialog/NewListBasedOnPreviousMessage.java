package ru.nigmatzianov.shoppingListBot.icq.dialog;

import org.springframework.stereotype.Component;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.nigmatzianov.shoppingListBot.domain.Message;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingListItem;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.service.MessageService;
import ru.nigmatzianov.shoppingListBot.service.ShoppingListService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewListBasedOnPreviousMessage implements State {
    private final MessageService messageService;
    private final ShoppingListService shoppingListService;
    private final BotApiClientController controller;
    private User user;

    public NewListBasedOnPreviousMessage(MessageService messageService, ShoppingListService shoppingListService, BotApiClientController controller) {
        this.messageService = messageService;
        this.shoppingListService = shoppingListService;
        this.controller = controller;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void react() {
        Message message = messageService.getLastForUser(user);

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setOwner(user);
        String[] lines = message.getText().split("\\r?\\n");

        List<ShoppingListItem> shoppingListItems = Arrays.stream(lines).map(item -> {
            ShoppingListItem shoppingListItem = new ShoppingListItem();
            shoppingListItem.setText(item);
            shoppingListItem.pending();
            shoppingListItem.setShoppingList(shoppingList);

            return shoppingListItem;
        }).collect(Collectors.toList());

        shoppingList.setItems(shoppingListItems);

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
                .setText("Создал");
        try {
            controller.sendTextMessage(request);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
