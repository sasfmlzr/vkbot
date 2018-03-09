package com.apiVKmanual.client;

import com.apiVKmanual.actions.Messages;
import com.apiVKmanual.actions.Other;
import com.apiVKmanual.bot.AbstractBot;
import com.apiVKmanual.functions.botdatabase.Database;
import com.vk.api.sdk.client.VkApiClient;

public class BotApiClient extends AbstractBot{

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

    public StateBot stateBot=new StateBot();

}
