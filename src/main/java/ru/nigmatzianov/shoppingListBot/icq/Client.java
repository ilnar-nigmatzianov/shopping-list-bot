package ru.nigmatzianov.shoppingListBot.icq;

import org.springframework.stereotype.Service;
import ru.mail.im.botapi.BotApiClient;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.CallbackQueryEvent;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingListItem;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.dto.IcqUserDto;
import ru.nigmatzianov.shoppingListBot.repo.ShoppingListRepository;
import ru.nigmatzianov.shoppingListBot.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Client {
    private final UserService userService;
    private final ShoppingListRepository shoppingListRepository;

    public Client(UserService userService, ShoppingListRepository repository) {
        this.userService = userService;
        this.shoppingListRepository = repository;
    }

    public void run() {
        BotApiClient client = new BotApiClient("001.3477817321.0868011592:752116872");

        BotApiClientController controller = BotApiClientController.startBot(client);
        client.addOnEventFetchListener(events -> {
            events.forEach(event -> {
                if (event instanceof NewMessageEvent) {
                    try {
                        IcqUserDto icqUserDto = IcqUserDto.create(
                                ((NewMessageEvent) event).getFrom(),
                                ((NewMessageEvent) event).getChat()
                        );

                        User user = userService.getOrCreate(icqUserDto);

                        // Create a keyboard
                        ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();


                        ShoppingList shoppingList = new ShoppingList();
                        shoppingList.setOwner(user);

                        String text = ((NewMessageEvent) event).getText();
                        String[] lines = text.split("\\r?\\n");

                        List<ShoppingListItem> shoppingListItems = Arrays.stream(lines).map(item -> {
                            ShoppingListItem shoppingListItem = new ShoppingListItem();
                            shoppingListItem.setText(item);
                            shoppingListItem.pending();

                            return shoppingListItem;
                        }).collect(Collectors.toList());

                        shoppingList.setItems(shoppingListItems);
                        shoppingListRepository.save(shoppingList);

                        String link = "Check your list by https://a8f0b07b.ngrok.io/" + shoppingList.getId();
                        SendTextRequest request = new SendTextRequest();
                        request
                                .setChatId(user.getChatId())
                                .setText(link);

                        controller.sendTextMessage(request);


////                        ShoppingList activeShoppingList = user.getActiveShoppingList();
//
//                        ShoppingListItem item1 = new ShoppingListItem();
//                        item1.setText("Хлеб");
//                        item1.setStatus("pending");
//
//                        ShoppingListItem item2 = new ShoppingListItem();
//                        item2.setText("Батон");
//                        item2.setStatus("pending");
//
//                        ShoppingList newList = new ShoppingList();
//                        newList.setOwner(user);
//                        newList.getItems().add(item1);
//                        newList.getItems().add(item2);
//
//                        shoppingListRepository.save(newList);
//
//                            ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//
//                            for (ShoppingListItem item : newList.getItems()) {
//                                ArrayList<InlineKeyboardButton> buttons = new ArrayList<>();
//                                buttons.add(InlineKeyboardButton.urlButton(
//                                        item.getText(),
//                                        "https://e33ea63d.ngrok.io/greeting"
//                                ));
//                                keyboard.add(buttons);
//                            }
//
//                            String text = "List";
//                            SendTextRequest request = new SendTextRequest();
//                            request
//                                    .setChatId(user.getChatId())
//                                    .setKeyboard(keyboard)
//                                    .setText(text);
//
//                            controller.sendTextMessage(request);

//                            if (user.isCreatingNewList()) {
//                                /*
//                                if submit -> create a new list
//                                if add new item -> keep calm
//                                 */
//                            } else {
//                                //propose to create a new list
//                            }




//                        if (user == null) {
//
//                            SendTextRequest request = new SendTextRequest();
//                            request
//                                    .setChatId(user.getChatId())
//                                    .setText(user.getName() + " you registered");
//                            controller.sendTextMessage(request);
//                        } else {
//                            SendTextRequest request = new SendTextRequest();
//                            request
//                                    .setChatId(user.getChatId())
//                                    .setText(user.getName() + " welcome back");
//                            controller.sendTextMessage(request);
//                        }
//
////                        controller.sendActions(((NewMessageEvent) event).getChat().getChatId(), ChatAction.LOOKING);
////                        InlineKeyboardButton button = new InlineKeyboardButton("Test", "https://7077906f.ngrok.io/greeting", "123");
//                        SendTextRequest sendTextRequest = new SendTextRequest();
//                        ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//                        ArrayList<InlineKeyboardButton> buttons = new ArrayList<>();
//                        buttons.add(InlineKeyboardButton.callbackButton("Test", "123"));
//                        keyboard.add(buttons);
//                        sendTextRequest
//                                .setText("Keyboard test")
//                                .setChatId(((NewMessageEvent) event).getChat().getChatId())
//                                .setKeyboard(keyboard);
//                        controller.sendTextMessage(sendTextRequest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event instanceof CallbackQueryEvent) {
                    IcqUserDto icqUserDto = IcqUserDto.create(
                            ((CallbackQueryEvent) event).getFrom(),
                            ((CallbackQueryEvent) event).getMessageChat()
                    );

                    User user = userService.getOrCreate(icqUserDto);
                }
            });
        });
    }

}