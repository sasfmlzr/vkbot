package com.sasfmlzr.apivk.actions

import com.sasfmlzr.apivk.client.AbstractAction
import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.ConversationWithMessage

open class Messages(client: BotApiClient) : AbstractAction(client) {

    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessage(actor: UserActor, message: String, messagesList: List<ConversationWithMessage>) {
        com.sasfmlzr.apivk.functions.message.Messages(client.vk, actor)
                .vkSendMessage(actor, message, messagesList, super.client.vk)
    }

    @Throws(ClientException::class, ApiException::class)
    fun vksendImageMessages(actor: UserActor, messagesList: List<ConversationWithMessage>) {
        com.sasfmlzr.apivk.functions.message.Messages(client.vk, actor)
                .vksendImageMessages(actor, messagesList, super.client.vk)
    }

    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessageUser(actor: UserActor, message: String, userID: Int) {
        com.sasfmlzr.apivk.functions.message.Messages(client.vk, actor)
                .vkSendMessageUser(actor, message, userID, super.client.vk)
    }

    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessage(actor: GroupActor, message: String, messagesList: List<ConversationWithMessage>) {
        com.sasfmlzr.apivk.functions.message.Messages(client.vk, actor)
                .vkSendMessage(actor, message, messagesList, super.client.vk)
    }

    @Throws(ClientException::class, ApiException::class)
    fun vksendImageMessages(actor: GroupActor, messagesList: List<ConversationWithMessage>) {
        com.sasfmlzr.apivk.functions.message.Messages(client.vk, actor)
                .vksendImageMessages(actor, messagesList, super.client.vk)
    }

    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessageUser(actor: GroupActor, message: String, userID: Int) {
        com.sasfmlzr.apivk.functions.message.Messages(client.vk, actor)
                .vkSendMessageUser(actor, message, userID, super.client.vk)
    }

    fun commands(): Commands {
        return Commands(client)
    }
}
