package com.sasfmlzr.apivk.bot

import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.Actor
import javafx.scene.image.Image

abstract class AbstractBot constructor(var vk: VkApiClient, var actor: Actor) {
    var userID: Int = 0
    var botName: String? = null
    var botImage: Image? = null

    lateinit var bot: BotApiClient

    fun botApiClient(): BotApiClient {
        if (!::bot.isInitialized) {
            bot = BotApiClient(vk, actor)
        }
        return bot
    }
}
