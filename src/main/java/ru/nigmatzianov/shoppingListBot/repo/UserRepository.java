package ru.nigmatzianov.shoppingListBot.repo;

import org.springframework.data.repository.CrudRepository;
import ru.nigmatzianov.shoppingListBot.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByExternalId(String externalId);
}
