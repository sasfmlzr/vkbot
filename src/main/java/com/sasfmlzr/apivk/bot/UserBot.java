package com.sasfmlzr.apivk.bot;

import com.sasfmlzr.apiVK.bot.AbstractBot;
import com.sasfmlzr.apiVK.thread.ThreadUserBot;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class UserBot extends AbstractBot {
    private UserActor actor;
    private ThreadUserBot threadUserBot;

    public UserBot(VkApiClient vkApiClient, UserActor actor) {
        this.actor = actor;
        vk = vkApiClient;
        threadUserBot = new ThreadUserBot(botApiClient(), actor);
        try {
            UserXtrCounters botSelfInfo = vk.users().get(actor).fields(UserField.PHOTO_200).execute().get(0);
            userID = botSelfInfo.getId();
            botName = botSelfInfo.getFirstName() + " " + botSelfInfo.getLastName();
            BufferedImage daffyDuckImage = ImageIO.read(new URL(botSelfInfo.getPhoto200()));
            setBotImage(SwingFXUtils.toFXImage(daffyDuckImage, null));
        } catch (ApiException | ClientException | IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Runnable r = threadUserBot;
        new Thread(r).start();
    }

    public UserActor getActor() {
        return actor;
    }

    public void setActor(UserActor actor) {
        this.actor = actor;
    }
}
