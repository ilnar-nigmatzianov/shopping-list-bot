package ru.nigmatzianov.shoppingListBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingList;
import ru.nigmatzianov.shoppingListBot.domain.ShoppingListItem;
import ru.nigmatzianov.shoppingListBot.repo.ShoppingListItemRepository;
import ru.nigmatzianov.shoppingListBot.repo.ShoppingListRepository;

@Controller
public class ShoppingListController {
    @Autowired
    private ShoppingListRepository shoppingListRepository;
    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;
    @GetMapping("/{shoppingList}")
    public String getList(@PathVariable ShoppingList shoppingList, Model model) {
        Iterable<ShoppingListItem> items;
        model.addAttribute("shoppingList", shoppingList);

        return "list";
    }
    @PutMapping("/lists/items/{shoppingListItem}")
    public ResponseEntity<String> buyItem(@PathVariable ShoppingListItem shoppingListItem) {
        shoppingListItem.buy();
        shoppingListItemRepository.save(shoppingListItem);

        return ResponseEntity.ok().body("Success");
    }
}
