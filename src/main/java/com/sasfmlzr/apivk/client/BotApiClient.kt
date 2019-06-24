package com.sasfmlzr.apivk.client

import com.sasfmlzr.apivk.actions.Messages
import com.sasfmlzr.apivk.actions.Other
import com.sasfmlzr.apivk.bot.AbstractBot
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.Actor

open class BotApiClient(vk: VkApiClient, actor: Actor) : AbstractBot(vk, actor) {

    var stateBot = StateBot()

    fun messages(): Messages {
        return Messages(this)
    }

    fun other(): Other {
        return Other()
    }
}
