package ru.nigmatzianov.shoppingListBot.dto;

import org.springframework.stereotype.Service;
import ru.mail.im.botapi.fetcher.Chat;
import ru.mail.im.botapi.fetcher.User;

@Service
public class IcqUserDto {
    private String name;
    private String lastName;
    private String nick;
    private String externalId;
    private String chatId;

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNick() {
        return nick;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getChatId() {
        return chatId;
    }

    public IcqUserDto create(User user, Chat chat) {
        IcqUserDto userDto = new IcqUserDto();

        userDto.externalId = user.getUserId();
        userDto.name = user.getFirstName();
        userDto.lastName = user.getLastName();
        userDto.nick = user.getNick();
        userDto.chatId = chat.getChatId();

        return userDto;
    }
}
