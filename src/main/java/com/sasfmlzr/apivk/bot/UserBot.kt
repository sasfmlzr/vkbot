package com.sasfmlzr.apivk.bot

import com.sasfmlzr.apivk.thread.ThreadUserBot
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.objects.users.Fields
import javafx.embed.swing.SwingFXUtils
import javax.imageio.ImageIO

class UserBot(vkApiClient: VkApiClient, var actor: UserActor) : AbstractBot() {
    private val threadUserBot: ThreadUserBot

    init {
        vk = vkApiClient
        threadUserBot = ThreadUserBot(botApiClient(), actor)
        try {
            val botSelfInfo = vk!!.users().get(actor).fields(Fields.PHOTO_200).execute()[0]
            userID = botSelfInfo.id
            botName = botSelfInfo.firstName + " " + botSelfInfo.lastName
            val daffyDuckImage = ImageIO.read(botSelfInfo.photo200)
            botImage = SwingFXUtils.toFXImage(daffyDuckImage, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun run() {
        val r = threadUserBot
        Thread(r).start()
    }
}
