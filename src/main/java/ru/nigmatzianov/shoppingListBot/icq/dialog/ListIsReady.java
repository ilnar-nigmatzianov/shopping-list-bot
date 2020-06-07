package ru.nigmatzianov.shoppingListBot.icq.dialog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.service.ShoppingListService;

@Component
public class ListIsReady implements State {
    private final ShoppingListService shoppingListService;
    private final BotApiClientController controller;
    private User user;
    @Value("${application.publicUrl}")
    private String url;

    public ListIsReady(ShoppingListService shoppingListService, BotApiClientController controller) {
        this.shoppingListService = shoppingListService;
        this.controller = controller;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void react() {
        ShoppingList activeShoppingList = user.getActiveShoppingList();
        activeShoppingList.setReady();
        shoppingListService.update(activeShoppingList);

        String link = "Check your list by " + this.url + "/" + activeShoppingList.getId();
        SendTextRequest request = new SendTextRequest();
        request
                .setChatId(user.getChatId())
                .setText(link);

        try {
            controller.sendTextMessage(request);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
