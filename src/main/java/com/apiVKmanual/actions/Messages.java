package com.apiVKmanual.actions;


import com.apiVKmanual.client.AbstractAction;
import com.apiVKmanual.client.BotApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Dialog;

import java.util.List;

public class Messages extends AbstractAction {
    public Messages(BotApiClient client){
        super(client);
    }

    public void vkSendMessage(UserActor actor, String message, List<Dialog> messagesList) throws  ClientException, ApiException {
        new com.apiVKmanual.functions.message.Messages(getClient().getVkApiClient()).vkSendMessage(actor, message, messagesList, super.getClient().getVkApiClient());
    }


    public void vksendImageMessages(UserActor actor, List<Dialog> messagesList) throws ClientException, ApiException{
        new com.apiVKmanual.functions.message.Messages(getClient().getVkApiClient()).vksendImageMessages(actor,  messagesList, super.getClient().getVkApiClient());
    }

    public void vkSendMessageUser(UserActor actor, String message, int userID) throws ClientException, ApiException {
        new com.apiVKmanual.functions.message.Messages(getClient().getVkApiClient()).vkSendMessageUser(actor,message,userID, super.getClient().getVkApiClient());
    }




}
