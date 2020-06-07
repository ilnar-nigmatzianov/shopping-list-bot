package ru.nigmatzianov.shoppingListBot.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
public class ShoppingListItem {
    @Transient
    private final String BOUGHT = "bought";
    @Transient
    private final String PENDING = "pending";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ShoppingList shoppingList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void buy() {
        this.status = BOUGHT;
    }

    public void pending() {
        this.status = PENDING;
    }

    public boolean isPending() {
        return this.status.equals(PENDING);
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }
}
