package ru.nigmatzianov.shoppingListBot.icq;

import org.springframework.stereotype.Component;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.AnswerCallbackQueryRequest;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.CallbackQueryEvent;
import ru.mail.im.botapi.fetcher.event.Event;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;
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
public class NewMessageEventHandler implements EventHandler {
    private final UserService userService;
    private final MessageService messageService;
    private final ShoppingListService shoppingListService;
    private final BotApiClientController controller;

    public NewMessageEventHandler(
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
        if (event instanceof NewMessageEvent) {
            IcqUserDto icqUserDto = IcqUserDto.create(
                    ((NewMessageEvent) event).getFrom(),
                    ((NewMessageEvent) event).getChat()
            );

            User user = userService.get(icqUserDto);
            if (null != user) {
                this.messageService.storeByIcqEvent(user, (NewMessageEvent) event);
                ShoppingList activeShoppingList = user.getActiveShoppingList();
                if (null != activeShoppingList) {
                    String incomingText = ((NewMessageEvent) event).getText();
                    String[] lines = incomingText.split("\\r?\\n");

                    List<ShoppingListItem> shoppingListItems = Arrays.stream(lines).map(item -> {
                        ShoppingListItem shoppingListItem = new ShoppingListItem();
                        shoppingListItem.setText(item);
                        shoppingListItem.pending();

                        return shoppingListItem;
                    }).collect(Collectors.toList());

                    activeShoppingList.getItems().addAll(shoppingListItems);

                    shoppingListService.update(activeShoppingList);

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
                            .setChatId(user.getChatId())
                            .setKeyboard(keyboard)
                            .setText(text);
                    try {
                        controller.sendTextMessage(request);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
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
            } else {
                user = userService.create(icqUserDto);

                String text = "Привет " + user.getName() + "! Отправь сообщение со списком того что надо купить. Разделяй элементы кнопкой энтер, запятой или можешь присылать по одному";
                SendTextRequest request = new SendTextRequest();
                request
                        .setChatId(user.getChatId())
                        .setText(text);

                try {
                    controller.sendTextMessage(request);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                // here will be code for /start command
            }
        }

    }
}
