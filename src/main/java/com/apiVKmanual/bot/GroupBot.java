package com.apiVKmanual.bot;

import com.apiVKmanual.thread.ThreadGroupBot;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GroupBot extends AbstractBot {
    public GroupActor getActor() {
        return actor;
    }

    public void setActor(GroupActor actor) {
        this.actor = actor;
    }

    public Image getBotImage() {
        return botImage;
    }

    public void setBotImage(Image botImage) {
        this.botImage = botImage;
    }

    private GroupActor actor;
    private Image botImage;
    private ThreadGroupBot threadGroupBot;
    public GroupBot(VkApiClient vkApiClient, GroupActor actor){
        this.actor=actor;
        vk=vkApiClient;
        threadGroupBot = new ThreadGroupBot(actor);
        try {
            List<UserXtrCounters> listCounters = vk.users().get(actor).fields(UserField.PHOTO_200).execute();
            if (!listCounters.isEmpty()) {
                UserXtrCounters botSelfInfo = listCounters.get(0);
                userID = botSelfInfo.getId();
                botName = botSelfInfo.getFirstName() + " " + botSelfInfo.getLastName();
                BufferedImage daffyDuckImage = ImageIO.read( new URL(botSelfInfo.getPhoto200()) );
                botImage = SwingFXUtils.toFXImage(daffyDuckImage,null);
            }else {
                botName = "Бот САИТ";
                URL urls = getClass().getResource("/Yes.jpg");
                if (urls == null) {
                    System.out.println("Could not find image!");
                }else {
                    BufferedImage daffyDuckImage = ImageIO.read(urls);
                    botImage = SwingFXUtils.toFXImage(daffyDuckImage,null);
                    System.out.println("find image!");
                }

            }



        } catch (ApiException | ClientException | IOException e) {
            e.printStackTrace();
        }
    }


    public void run(){
        //Create thread bot
        Runnable r = threadGroupBot;
        new Thread(r).start();
        // threadUserBot.run();
    }

}
