package com.sasfmlzr.apivk.bot;

import com.sasfmlzr.apivk.client.BotApiClient;
import com.vk.api.sdk.client.VkApiClient;
import javafx.scene.image.Image;

public abstract class AbstractBot {
    int userID;
    String botName;
    VkApiClient vk;
    Image botImage;

    public BotApiClient botApiClient() {
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

    public Image getBotImage() {
        return botImage;
    }

    public void setBotImage(Image botImage) {
        this.botImage = botImage;
    }

}
