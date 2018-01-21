package com.apiVKmanual;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;

public abstract class AbstractBot {
    int userID;
    String botName;
    VkApiClient vk;

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
