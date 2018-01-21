package com.apiVKmanual.actions;

import com.apiVKmanual.client.AbstractAction;

public class Functional extends AbstractAction {
    Functional(com.apiVKmanual.client.BotApiClient botApiClient) {
        super(botApiClient);
    }
    public String weather(String city){
        return new com.apiVKmanual.functions.bot.Functional(getClient()).weather(city);
    }
}
