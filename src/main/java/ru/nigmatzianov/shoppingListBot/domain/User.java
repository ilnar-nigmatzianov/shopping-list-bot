package ru.nigmatzianov.shoppingListBot.domain;

import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Entity
@Transactional
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String externalId;
    private String name;
    private String lastName;
    private String nick;
    private String chatId;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<ShoppingList> shoppingLists = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public ShoppingList getActiveShoppingList() {
        if (this.shoppingLists.isEmpty()) {
            return null;
        }
        ShoppingList activeShoppingList = null;
        for (ShoppingList list : this.shoppingLists) {
            if (null == activeShoppingList) {
                activeShoppingList = list;
            } else {
                if (list.getId() > activeShoppingList.getId()) {
                    activeShoppingList = list;
                }
            }
        }

        if (!activeShoppingList.isReady()) {
            return activeShoppingList;
        } else {
            return null;
        }
    }
}
