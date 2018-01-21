package com.apiVKmanual.actions;

import com.apiVKmanual.client.AbstractAction;
import com.apiVKmanual.client.BotApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Dialog;

import java.sql.SQLException;
import java.util.List;

public class Commands extends AbstractAction {
    Commands(com.apiVKmanual.client.BotApiClient client) {
        super(client);
    }
    public String commandsBot(String textMessageString, String messages, UserActor actor, List<Dialog> messagesList, BotApiClient bot) throws SQLException, ApiException, ClientException {
             return new com.apiVKmanual.functions.bot.Commands(getClient()).commandsBot(textMessageString, messages, actor, messagesList, bot);
    }
    public String adminCommandsBot(String textMessageString, String messages){
            return new com.apiVKmanual.functions.bot.Commands(getClient()).adminCommandsBot(textMessageString, messages);
    }
}
