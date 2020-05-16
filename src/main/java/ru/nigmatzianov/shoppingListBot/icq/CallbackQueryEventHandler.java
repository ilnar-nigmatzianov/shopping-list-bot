package ru.nigmatzianov.shoppingListBot.icq;

import org.springframework.stereotype.Component;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.AnswerCallbackQueryRequest;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.CallbackQueryEvent;
import ru.mail.im.botapi.fetcher.event.Event;
import ru.nigmatzianov.shoppingListBot.domain.Message;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingListItem;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.dto.IcqUserDto;
import ru.nigmatzianov.shoppingListBot.service.MessageService;
import ru.nigmatzianov.shoppingListBot.service.ShoppingListService;
import ru.nigmatzianov.shoppingListBot.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CallbackQueryEventHandler implements EventHandler {
    private final UserService userService;
    private final MessageService messageService;
    private final ShoppingListService shoppingListService;
    private final BotApiClientController controller;

    public CallbackQueryEventHandler(
            UserService userService,
            MessageService messageService,
            ShoppingListService shoppingListService,
            BotApiClientController controller
    ) {
        this.userService = userService;
        this.messageService = messageService;
        this.shoppingListService = shoppingListService;
        this.controller = controller;
    }

    @Override
    public void handle(Event<?> event) {
        if (event instanceof CallbackQueryEvent) {
            AnswerCallbackQueryRequest callbackRequest = new AnswerCallbackQueryRequest();
            callbackRequest.setQueryId(((CallbackQueryEvent) event).getQueryId());
            try {
                controller.answerCallbackQuery(callbackRequest);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            IcqUserDto icqUserDto = IcqUserDto.create(
                    ((CallbackQueryEvent) event).getFrom(),
                    ((CallbackQueryEvent) event).getMessageChat()
            );
            User user = userService.get(icqUserDto);

            String data = ((CallbackQueryEvent) event).getCallbackData();
            if (data.equals("based-on-previous-message")) {
                Message message = messageService.getLastForUser(user);

                ShoppingList shoppingList = new ShoppingList();
                shoppingList.setOwner(user);
                String[] lines = message.getText().split("\\r?\\n");

                List<ShoppingListItem> shoppingListItems = Arrays.stream(lines).map(item -> {
                    ShoppingListItem shoppingListItem = new ShoppingListItem();
                    shoppingListItem.setText(item);
                    shoppingListItem.pending();

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
            } else if (data.equals("new")) {
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
            } else if (data.equals("done")) {
                ShoppingList activeShoppingList = user.getActiveShoppingList();
                activeShoppingList.setReady();
                shoppingListService.update(activeShoppingList);

                String link = "Check your list by https://localhost:8080/" + activeShoppingList.getId();
                SendTextRequest request = new SendTextRequest();
                request
                        .setChatId(user.getChatId())
                        .setText(link);

                try {
                    controller.sendTextMessage(request);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            } else {
                System.out.println("Unknown callback");
            }
        }
    }
}
