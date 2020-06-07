package ru.nigmatzianov.shoppingListBot.icq.dialog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.fetcher.Chat;
import ru.mail.im.botapi.fetcher.User;
import ru.mail.im.botapi.fetcher.event.Event;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;
import ru.nigmatzianov.shoppingListBot.dto.IcqUserDto;
import ru.nigmatzianov.shoppingListBot.icq.dialog.DialogStateFactory;
import ru.nigmatzianov.shoppingListBot.icq.dialog.State;
import ru.nigmatzianov.shoppingListBot.icq.dialog.UnsupportedEventException;
import ru.nigmatzianov.shoppingListBot.service.ShoppingListService;
import ru.nigmatzianov.shoppingListBot.service.UserService;

import java.util.HashSet;
import java.util.Set;

public class DialogStateFactoryTest {

    @Test
    void getState_userDoesntExist_NewUserStateReturned() throws UnsupportedEventException {
        NewMessageEvent eventMock = createNewMessageEventMock();
        IcqUserDto icqUserDtoMock = mock(IcqUserDto.class);
        when(icqUserDtoMock.create(eventMock.getFrom(), eventMock.getChat())).thenReturn(null);

        DialogStateFactory stateFactory = new DialogStateFactory(mock(UserService.class), icqUserDtoMock, getPossibleStates());
        State state = stateFactory.getState(eventMock);

        assertThat(state, instanceOf(NewUser.class));
    }

    @Test
    void getState_userDoesntExist_NewUserStateWellConfigured() throws UnsupportedEventException {
        NewMessageEvent eventMock = createNewMessageEventMock();
        IcqUserDto icqUserDtoMock = mock(IcqUserDto.class);
        when(icqUserDtoMock.create(eventMock.getFrom(), eventMock.getChat())).thenReturn(null);

        DialogStateFactory stateFactory = new DialogStateFactory(mock(UserService.class), icqUserDtoMock, getPossibleStates());
        State state = stateFactory.getState(eventMock);
        verify((NewUser)state, times(1)).setIcqUserDto(any());
    }

//    @Test
//    void getState_userExistsAndListInEditMode_NewItem() throws UnsupportedEventException {
//        NewMessageEvent eventMock = createNewMessageEventMock();
//
//        IcqUserDto icqUserDtoMock = mock(IcqUserDto.class);
//        when(icqUserDtoMock.create(eventMock.getFrom(), eventMock.getChat())).thenReturn(icqUserDtoMock);
//
//        ru.nigmatzianov.shoppingListBot.domain.User user = mock(ru.nigmatzianov.shoppingListBot.domain.User.class);
//
//        UserService userServiceMock = mock(UserService.class);
//        when(userServiceMock.get(icqUserDtoMock)).thenReturn(user);
//
//        DialogStateFactory stateFactory = new DialogStateFactory(mock(UserService.class), icqUserDtoMock, getPossibleStates());
//        State state = stateFactory.getState(eventMock);
//        assertThat(state, instanceOf(NewItem.class));
//    }

//    private Set<State> getPossibleStates() {
//        HashSet<State> possibleStates = new HashSet<>();
//
//        NewItem newItem = new NewItem(mock(ShoppingListService.class), mock(BotApiClientController.class));
//        ChooseHowToCreateList howToCreateList = new ChooseHowToCreateList(mock(BotApiClientController.class));
//        NewUser newUser = new NewUser(mock(UserService.class), mock(BotApiClientController.class));
//        possibleStates.add(newItem);
//        possibleStates.add(howToCreateList);
//        possibleStates.add(newUser);
//
//        return possibleStates;
//    }

    private Set<State> getPossibleStates() {
        HashSet<State> possibleStates = new HashSet<>();

        NewItem newItem = mock(NewItem.class);
        ChooseHowToCreateList howToCreateList = mock(ChooseHowToCreateList.class);
        NewUser newUser = mock(NewUser.class);
        possibleStates.add(newItem);
        possibleStates.add(howToCreateList);
        possibleStates.add(newUser);

        return possibleStates;
    }

    private NewMessageEvent createNewMessageEventMock()
    {
        User user = new User();
        Chat chat = new Chat();
        NewMessageEvent eventMock = mock(NewMessageEvent.class);
        when(eventMock.getFrom()).thenReturn(user);
        when(eventMock.getChat()).thenReturn(chat);

        return eventMock;
    }
}