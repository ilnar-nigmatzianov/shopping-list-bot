package ru.nigmatzianov.shoppingListBot.icq.dialog;

import ru.mail.im.botapi.BotApiClientController;
import ru.nigmatzianov.shoppingListBot.icq.dialog.state.StateInterface;

import javax.validation.constraints.NotNull;

public class DialogContext {
    private StateInterface state;

    //@Todo: check what happens if I pass null here
    public DialogContext(@NotNull StateInterface initialState) {
        this.state = initialState;
    }

    public void setState(StateInterface state) {
        this.state = state;
    }

    public void action(BotApiClientController controller)
    {
        this.state.action(controller);
    }
}
