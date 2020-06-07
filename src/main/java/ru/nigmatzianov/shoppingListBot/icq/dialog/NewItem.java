package ru.nigmatzianov.shoppingListBot.icq.dialog;

import org.springframework.stereotype.Component;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingListItem;
import ru.nigmatzianov.shoppingListBot.service.ShoppingListService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewItem implements State {
    private final ShoppingListService shoppingListService;
    private final BotApiClientController controller;

    private ShoppingList shoppingList;
    private NewMessageEvent event;

    public NewItem(ShoppingListService shoppingListService, BotApiClientController botApiClientController) {
        this.shoppingListService = shoppingListService;
        this.controller = botApiClientController;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public void setEvent(NewMessageEvent event) {
        this.event = event;
    }

    @Override
    public void react() {
        String incomingText = event.getText();
        String[] lines = incomingText.split("\\r?\\n");

        List<ShoppingListItem> shoppingListItems = Arrays.stream(lines).map(item -> {
            ShoppingListItem shoppingListItem = new ShoppingListItem();
            shoppingListItem.setText(item);
            shoppingListItem.pending();
            shoppingListItem.setShoppingList(shoppingList);

            return shoppingListItem;
        }).collect(Collectors.toList());

        shoppingList.getItems().addAll(shoppingListItems);

        shoppingListService.update(shoppingList);

        String text = "Добавил";

        ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        ArrayList<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.callbackButton(
                "Готово",
                "done"
        ));
        keyboard.add(buttons);

        SendTextRequest request = new SendTextRequest();
        request
                .setChatId(shoppingList.getOwner().getChatId())
                .setKeyboard(keyboard)
                .setText(text);
        try {
            controller.sendTextMessage(request);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
