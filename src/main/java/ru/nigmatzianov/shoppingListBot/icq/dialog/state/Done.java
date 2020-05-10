package ru.nigmatzianov.shoppingListBot.icq.dialog.state;

import org.springframework.beans.factory.annotation.Autowired;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.repo.ShoppingListItemRepository;

import java.util.ArrayList;
import java.util.List;

public class Done implements StateInterface {
    private final User user;

    public Done(User user) {
        this.user = user;
    }

    @Override
    public void action(BotApiClientController controller) {
        ShoppingList shoppingList = user.getActiveShoppingList();
        shoppingList.ready();


        String link = "Упраляйте списком по ссылке https://a8f0b07b.ngrok.io/" + shoppingList.getId();
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
