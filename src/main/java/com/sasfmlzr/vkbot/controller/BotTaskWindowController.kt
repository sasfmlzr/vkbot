package com.sasfmlzr.vkbot.controller

import com.api.client.Client
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import javafx.fxml.Initializable
import java.net.URL
import java.util.*

class BotTaskWindowController : Initializable {

    override fun initialize(location: URL, resources: ResourceBundle) {
        println("BotTaskWindowController opened")
    }

    internal fun initWindow() {}

    @Throws(Exception::class)
    fun zapros() {
        val transportClient = HttpTransportClient.getInstance()
        val vk = VkApiClient(transportClient)
        val mam = Client.token
        val ID = Client.idBot
        print("id = $ID\n")
        print("token = $mam\n")
        val actor = UserActor(ID, mam)

        val a = 10 // Начальное значение диапазона - "от"
        val b = 8000 // Конечное значение диапазона - "до"

        //	int random = Integer(Math.random());
        //UserActor actor = new UserActor(authResponse.getUserId(), client.token);
        vk.messages().send(actor)
                .message("Как дела?")
                .userId(30562433)
                .randomId(a + (Math.random() * b).toInt())
                .execute()
    }

    companion object {
        internal val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.BotTaskWindow.messages"
        internal val fxmlPath = "/com/sasfmlzr/vkbot/views/BotTaskWindow.fxml"
    }
}
