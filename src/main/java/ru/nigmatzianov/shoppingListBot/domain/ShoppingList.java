package ru.nigmatzianov.shoppingListBot.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "shoppingList", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<ShoppingListItem> items = new ArrayList<>();

    private String status = "pending";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingListItem> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReady() {
        return "ready".equals(this.status);
    }

    public void setReady() {
        this.status = "ready";
    }
}
