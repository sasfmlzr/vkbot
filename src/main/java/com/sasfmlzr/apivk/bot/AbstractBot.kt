package com.sasfmlzr.apivk.bot

import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.VkApiClient
import javafx.scene.image.Image

abstract class AbstractBot {
    var userID: Int = 0
    var botName: String? = null
    var vk: VkApiClient? = null
    var botImage: Image? = null

    fun botApiClient(): BotApiClient {
        return BotApiClient(vk!!)
    }

}
