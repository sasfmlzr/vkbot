package com.sasfmlzr.apivk.client;


import com.sasfmlzr.apiVK.client.BotApiClient;

public abstract class AbstractAction {
    private com.sasfmlzr.apiVK.client.BotApiClient BotApiClient;

    protected AbstractAction(com.sasfmlzr.apiVK.client.BotApiClient botApiClient) {
        this.BotApiClient = botApiClient;
    }

    protected com.sasfmlzr.apiVK.client.BotApiClient getClient() {
        return BotApiClient;
    }





}
