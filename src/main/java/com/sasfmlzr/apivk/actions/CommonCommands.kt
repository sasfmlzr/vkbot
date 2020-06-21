package com.sasfmlzr.apivk.actions

import com.sasfmlzr.apivk.client.AbstractAction
import com.sasfmlzr.apivk.client.BotApiClient
import com.sasfmlzr.apivk.functions.bot.Commands
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.ConversationWithMessage
import java.sql.SQLException

class CommonCommands internal constructor(client: BotApiClient) : AbstractAction(client) {

    @Throws(SQLException::class, ApiException::class, ClientException::class)
    fun commandsBot(
            textMessageString: String,
            messages: String,
            actor: UserActor,
            messagesList: List<ConversationWithMessage>,
            bot: BotApiClient
    ): String? {
        return Commands(client)
                .commandsBot(textMessageString, messages, actor, messagesList, bot)
    }

    fun adminCommandsBot(textMessageString: String, messages: String): String {
        return Commands(client).adminCommandsBot(textMessageString, messages)
    }
}
