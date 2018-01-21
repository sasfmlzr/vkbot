package com.apiVKmanual.bot;

import com.apiVKmanual.client.BotApiClient;
import com.vk.api.sdk.client.VkApiClient;

public abstract class AbstractBot {
    int userID;
    String botName;
    VkApiClient vk;

    public BotApiClient botApiClient(){
        return new BotApiClient(vk);
    }
    public VkApiClient getVk() {
        return vk;
    }

    public void setVk(VkApiClient vk) {
        this.vk = vk;
    }



    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }





}
