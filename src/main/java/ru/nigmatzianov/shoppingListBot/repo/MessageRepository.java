package ru.nigmatzianov.shoppingListBot.repo;

import org.springframework.data.repository.CrudRepository;
import ru.nigmatzianov.shoppingListBot.domain.Message;
import ru.nigmatzianov.shoppingListBot.domain.User;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Message findByExternalId(Long externalId);

    Message findTop1ByOwner(User user);
}