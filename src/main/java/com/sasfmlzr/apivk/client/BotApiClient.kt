package com.sasfmlzr.apivk.client

import com.sasfmlzr.apivk.actions.Messages
import com.sasfmlzr.apivk.actions.Other
import com.sasfmlzr.apivk.bot.AbstractBot
import com.sasfmlzr.apivk.functions.botdatabase.Database
import com.vk.api.sdk.client.VkApiClient

open class BotApiClient(val vkApiClient: VkApiClient?) : AbstractBot() {

    var stateBot = StateBot()

    fun messages(): Messages {
        return Messages(this)
    }

    fun other(): Other {
        return Other()
    }

    companion object {
        var database = Database()
    }

}
