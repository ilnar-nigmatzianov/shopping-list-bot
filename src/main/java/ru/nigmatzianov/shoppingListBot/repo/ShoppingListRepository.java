package ru.nigmatzianov.shoppingListBot.repo;

import org.springframework.data.repository.CrudRepository;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;

public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {
}
