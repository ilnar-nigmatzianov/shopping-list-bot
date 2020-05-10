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
import ru.nigmatzianov.shoppingListBot.icq.dialog.DialogContext;
import ru.nigmatzianov.shoppingListBot.icq.dialog.state.Create;
import ru.nigmatzianov.shoppingListBot.icq.dialog.state.Done;
import ru.nigmatzianov.shoppingListBot.icq.dialog.state.Edit;
import ru.nigmatzianov.shoppingListBot.icq.dialog.state.StateInterface;
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
                StateInterface initialState = null;
                if (event instanceof NewMessageEvent) {
                    IcqUserDto icqUserDto = IcqUserDto.create(
                            ((NewMessageEvent) event).getFrom(),
                            ((NewMessageEvent) event).getChat()
                    );

                    User user = userService.get(icqUserDto);
                    if (null != user) {
                        // get active shopping list
                        // if there is on - state is edit
                    } else {
                        user = userService.create(icqUserDto);
                        initialState = new Create(user);
                    }
                }
                if (event instanceof CallbackQueryEvent) {
                    IcqUserDto icqUserDto = IcqUserDto.create(
                            ((CallbackQueryEvent) event).getFrom(),
                            ((CallbackQueryEvent) event).getMessageChat()
                    );
                    User user = userService.get(icqUserDto);

                    if (((CallbackQueryEvent) event).getCallbackData().equals("done")) {
                        initialState = new Done(user);
                    } else {
                        new Edit(user);                    }

//
//                    User user = userService.getOrCreate(icqUserDto);
                }

                DialogContext dialog = new DialogContext(initialState);
                dialog.action(controller);
            });
        });
    }

}