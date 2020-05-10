package ru.nigmatzianov.shoppingListBot.repo;

import org.springframework.data.repository.CrudRepository;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingListItem;

public interface ShoppingListItemRepository extends CrudRepository<ShoppingListItem, Long> {
}
