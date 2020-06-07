package ru.nigmatzianov.shoppingListBot.icq.dialog;

import org.springframework.stereotype.Service;
import ru.mail.im.botapi.fetcher.event.CallbackQueryEvent;
import ru.mail.im.botapi.fetcher.event.Event;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.dto.IcqUserDto;
import ru.nigmatzianov.shoppingListBot.service.UserService;

import java.util.Optional;
import java.util.Set;

@Service
public class DialogStateFactory {

    private final UserService userService;
    private final IcqUserDto icqUserDto;
    private final Set<State> possibleStates;

    public DialogStateFactory(UserService userService, IcqUserDto icqUserDto, Set<State> possibleStates) {
        this.userService = userService;
        this.icqUserDto = icqUserDto;
        this.possibleStates = possibleStates;
    }

    public State getState(Event<?> event) throws UnsupportedEventException {

        if (event instanceof NewMessageEvent) {
            return testNewMessageStates((NewMessageEvent) event);
        }
        if (event instanceof CallbackQueryEvent) {
            return testCallbackQueryEventStates((CallbackQueryEvent) event);
        }

        throw new UnsupportedEventException();
    }

    private State testNewMessageStates(NewMessageEvent event) {
        IcqUserDto icqUserDto = this.icqUserDto.create(
                event.getFrom(),
                event.getChat()
        );

        User user = userService.get(icqUserDto);

        if (null != user) {
            ShoppingList activeShoppingList = user.getActiveShoppingList();
            if (null != activeShoppingList) {
                NewItem state = (NewItem) possibleStates
                        .stream()
                        .filter(i -> i instanceof NewItem)
                        .findAny()
                        .orElse(null);
                if (state != null) {
                    state.setEvent(event);
                    state.setShoppingList(activeShoppingList);
                }

                return state;
            } else {
                ChooseHowToCreateList state = (ChooseHowToCreateList) possibleStates
                        .stream()
                        .filter(i -> i instanceof ChooseHowToCreateList)
                        .findAny()
                        .orElse(null);

                if (state != null) {
                    state.setUser(user);
                }

                return state;
            }
        } else {
            NewUser state = (NewUser) possibleStates
                    .stream()
                    .filter(i -> i instanceof NewUser)
                    .findAny()
                    .orElse(null);

            if (state != null) {
                state.setIcqUserDto(icqUserDto);
            }

            return state;
        }
    }

    private State testCallbackQueryEventStates(CallbackQueryEvent event) {
        String data = event.getCallbackData();
        IcqUserDto icqUserDto = this.icqUserDto.create(
                event.getFrom(),
                event.getMessageChat()
        );

        User user = userService.get(icqUserDto);

        switch (data) {
            case "based-on-previous-message":
                NewListBasedOnPreviousMessage newListBasedOnPreviousMessage = (NewListBasedOnPreviousMessage) possibleStates
                        .stream()
                        .filter(i -> i instanceof NewListBasedOnPreviousMessage)
                        .findAny()
                        .orElse(null);

                if (newListBasedOnPreviousMessage != null) {
                    newListBasedOnPreviousMessage.setUser(user);
                }

                return newListBasedOnPreviousMessage;

            case "new":
                NewEmptyList newEmptyList = (NewEmptyList) possibleStates
                        .stream()
                        .filter(i -> i instanceof NewEmptyList)
                        .findAny()
                        .orElse(null);

                if (newEmptyList != null) {
                    newEmptyList.setUser(user);
                }

                return newEmptyList;

            default:
                ListIsReady listIsReady = (ListIsReady) possibleStates
                        .stream()
                        .filter(i -> i instanceof ListIsReady)
                        .findAny()
                        .orElse(null);

                if (listIsReady != null) {
                    listIsReady.setUser(user);
                }

                return listIsReady;
        }
    }


}
