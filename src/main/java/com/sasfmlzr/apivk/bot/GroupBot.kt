package com.sasfmlzr.apivk.bot

import com.sasfmlzr.apivk.thread.ThreadGroupBot
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.queries.users.UserField
import javafx.embed.swing.SwingFXUtils
import java.net.URL
import javax.imageio.ImageIO

class GroupBot(vkApiClient: VkApiClient, var actor: GroupActor) : AbstractBot() {
    private val threadGroupBot: ThreadGroupBot

    init {
        vk = vkApiClient
        threadGroupBot = ThreadGroupBot(botApiClient(), actor)
        try {
            val listCounters = vk!!.users().get(actor).fields(UserField.PHOTO_200).execute()
            if (!listCounters.isEmpty()) {
                val botSelfInfo = listCounters[0]
                userID = botSelfInfo.id
                botName = botSelfInfo.firstName + " " + botSelfInfo.lastName
                val daffyDuckImage = ImageIO.read(URL(botSelfInfo.photo200))
                botImage = SwingFXUtils.toFXImage(daffyDuckImage, null)
            } else {
                botName = "Бот САИТ"
                val urls = javaClass.getResource("/Yes.jpg")
                if (urls == null) {
                    println("Could not find image!")
                } else {
                    val daffyDuckImage = ImageIO.read(urls)
                    botImage = SwingFXUtils.toFXImage(daffyDuckImage, null)
                    println("find image!")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun run() {
        val r = threadGroupBot
        Thread(r).start()
    }
}
