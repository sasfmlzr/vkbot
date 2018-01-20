package com.apiVKmanual.client;



public abstract class AbstractAction {
    private BotApiClient BotApiClient;

    protected AbstractAction(BotApiClient botApiClient) {
        this.BotApiClient = botApiClient;
    }

    protected BotApiClient getClient() {
        return BotApiClient;
    }
}
