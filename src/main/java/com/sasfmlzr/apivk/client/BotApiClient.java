package com.sasfmlzr.apivk.client;

import com.sasfmlzr.apiVK.actions.Messages;
import com.sasfmlzr.apiVK.actions.Other;
import com.sasfmlzr.apiVK.bot.AbstractBot;
import com.sasfmlzr.apiVK.client.StateBot;
import com.sasfmlzr.apiVK.functions.botdatabase.Database;
import com.vk.api.sdk.client.VkApiClient;

public class BotApiClient extends AbstractBot {

    private VkApiClient vk;

    public BotApiClient(VkApiClient vkApi){
        vk=vkApi;
    }

    public VkApiClient getVkApiClient(){return this.vk;}

    public Messages messages() {
        return new Messages(this);
    }

    public Other other() {
        return new Other();
    }

    public static Database database=new Database();

    public com.sasfmlzr.apiVK.client.StateBot stateBot=new StateBot();

}
