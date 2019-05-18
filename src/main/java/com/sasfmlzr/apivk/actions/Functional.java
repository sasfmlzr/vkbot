package com.sasfmlzr.apivk.actions;

import com.sasfmlzr.apiVK.client.AbstractAction;
import com.sasfmlzr.apiVK.client.BotApiClient;

public class Functional extends AbstractAction {
    Functional(BotApiClient botApiClient) {
        super(botApiClient);
    }

    public String weather(String city) {
        return new com.sasfmlzr.apiVK.functions.bot.Functional(getClient()).weather(city);
    }
}
