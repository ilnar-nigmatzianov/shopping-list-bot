package ru.nigmatzianov.shoppingListBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.repo.ShoppingListRepository;

@Service
public class ShoppingListService {
    @Autowired
    private ShoppingListRepository repository;

    public void update(ShoppingList shoppingList) {
        repository.save(shoppingList);
    }

    public void create(ShoppingList shoppingList) {
        repository.save(shoppingList);
    }


//    public List<ShoppingList> getList()
}
