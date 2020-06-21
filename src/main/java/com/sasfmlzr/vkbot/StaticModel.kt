package com.sasfmlzr.vkbot

import com.sasfmlzr.apivk.bot.GroupBot
import com.sasfmlzr.apivk.bot.UserBot
import com.sasfmlzr.vkbot.controller.menuprogram.PropertiesProgramWindowController
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient

object StaticModel {
    private val transportClient = HttpTransportClient.getInstance()
    private val vk = VkApiClient(transportClient)
    // TODO: разобраться с UserActor
    private val actor = UserActor(Integer.parseInt(PropertiesProgramWindowController.userId1), PropertiesProgramWindowController.token1)

    var userBot = UserBot(vk, actor)


    private val groupActor = GroupActor(0, "0")
    var groupBot = GroupBot(vk, groupActor)
}
