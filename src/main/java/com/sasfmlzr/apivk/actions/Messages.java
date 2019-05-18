package com.sasfmlzr.apivk.actions;


import com.sasfmlzr.apiVK.actions.Commands;
import com.sasfmlzr.apiVK.actions.Functional;
import com.sasfmlzr.apiVK.client.AbstractAction;
import com.sasfmlzr.apiVK.client.BotApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
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
        new com.sasfmlzr.apiVK.functions.message.Messages(getClient().getVkApiClient()).vkSendMessage(actor, message, messagesList, super.getClient().getVkApiClient());
    }

    public void vksendImageMessages(UserActor actor, List<Dialog> messagesList) throws ClientException, ApiException{
        new com.sasfmlzr.apiVK.functions.message.Messages(getClient().getVkApiClient()).vksendImageMessages(actor,  messagesList, super.getClient().getVkApiClient());
    }

    public void vkSendMessageUser(UserActor actor, String message, int userID) throws ClientException, ApiException {
        new com.sasfmlzr.apiVK.functions.message.Messages(getClient().getVkApiClient()).vkSendMessageUser(actor,message,userID, super.getClient().getVkApiClient());
    }

    public void vkSendMessage(GroupActor actor, String message, List<Dialog> messagesList) throws  ClientException, ApiException {
        new com.sasfmlzr.apiVK.functions.message.Messages(getClient().getVkApiClient()).vkSendMessage(actor, message, messagesList, super.getClient().getVkApiClient());
    }

    public void vksendImageMessages(GroupActor actor, List<Dialog> messagesList) throws ClientException, ApiException{
        new com.sasfmlzr.apiVK.functions.message.Messages(getClient().getVkApiClient()).vksendImageMessages(actor,  messagesList, super.getClient().getVkApiClient());
    }

    public void vkSendMessageUser(GroupActor actor, String message, int userID) throws ClientException, ApiException {
        new com.sasfmlzr.apiVK.functions.message.Messages(getClient().getVkApiClient()).vkSendMessageUser(actor,message,userID, super.getClient().getVkApiClient());
    }
    public com.sasfmlzr.apiVK.actions.Commands commands (){ return new Commands(getClient());}
    public Functional functional() {return new Functional(getClient());}

}
