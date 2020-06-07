package ru.nigmatzianov.shoppingListBot.icq.dialog;

import org.springframework.stereotype.Component;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.dto.IcqUserDto;
import ru.nigmatzianov.shoppingListBot.service.UserService;

@Component
public class NewUser implements State {
    private final UserService userService;
    private final BotApiClientController controller;
    private IcqUserDto icqUserDto;

    public NewUser(UserService userService, BotApiClientController controller) {
        this.userService = userService;
        this.controller = controller;
    }

    public void setIcqUserDto(IcqUserDto icqUserDto) {
        this.icqUserDto = icqUserDto;
    }

    @Override
    public void react() {
        User user = userService.create(icqUserDto);

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
    }
}
