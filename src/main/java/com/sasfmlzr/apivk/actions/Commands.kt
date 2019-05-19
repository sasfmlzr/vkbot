package com.sasfmlzr.apivk.actions

import com.sasfmlzr.apivk.client.AbstractAction
import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.Dialog

import java.sql.SQLException

class Commands internal constructor(client: com.sasfmlzr.apivk.client.BotApiClient) : AbstractAction(client) {

    @Throws(SQLException::class, ApiException::class, ClientException::class)
    fun commandsBot(
        textMessageString: String,
        messages: String,
        actor: UserActor,
        messagesList: List<Dialog>,
        bot: BotApiClient
    ): String? {
        return com.sasfmlzr.apivk.functions.bot.Commands(client)
            .commandsBot(textMessageString, messages, actor, messagesList, bot)
    }

    fun adminCommandsBot(textMessageString: String, messages: String): String {
        return com.sasfmlzr.apivk.functions.bot.Commands(client).adminCommandsBot(textMessageString, messages)
    }
}