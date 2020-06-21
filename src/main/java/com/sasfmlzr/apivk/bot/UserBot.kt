package com.sasfmlzr.apivk.bot

import com.sasfmlzr.apivk.thread.ThreadUserBot
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.objects.users.Fields
import javafx.embed.swing.SwingFXUtils
import javax.imageio.ImageIO

class UserBot(vk: VkApiClient, actor: UserActor) : AbstractBot(vk, actor) {
    private val threadUserBot: ThreadUserBot = ThreadUserBot(botApiClient(), actor)

    init {
        try {
            val botSelfInfo = vk.users().get(actor).fields(Fields.PHOTO_200).execute()[0]
            userID = botSelfInfo.id
            botName = "${botSelfInfo.firstName} ${botSelfInfo.lastName}"
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
