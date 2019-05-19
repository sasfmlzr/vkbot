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


    private val groupActor = GroupActor(144853761, "0ed42462bb7ff49865bd92343711fbcab31b18ce3e7bfeabffed19019e05eb84aa25fa982d6412a9ab979")
    var groupBot = GroupBot(vk, groupActor)
}
