package ru.nigmatzianov.shoppingListBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;
import ru.nigmatzianov.shoppingListBot.domain.Message;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.repo.MessageRepository;
import ru.nigmatzianov.shoppingListBot.repo.UserRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository repo;
    @Autowired
    private UserRepository userRepository;

    public Message storeByIcqEvent(NewMessageEvent event) {
        User user = userRepository.findByExternalId(event.getFrom().getUserId());
        Message message = new Message();
        message.setOwner(user);
        message.setExternalId(event.getMessageId());
        message.setText(event.getText());

        repo.save(message);

        return message;
    }

    public Message getByExternalId(Long externalId) {
        return repo.findByExternalId(externalId);
    }

    public Message getLastForUser(User user) {
        return repo.findTop1ByOwner(user);
    }
}
