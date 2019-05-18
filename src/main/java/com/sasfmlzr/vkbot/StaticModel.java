package com.sasfmlzr.vkbot;

import com.sasfmlzr.apivk.bot.GroupBot;
import com.sasfmlzr.apivk.bot.UserBot;
import com.sasfmlzr.apivk.functions.botdatabase.Database;
import com.sasfmlzr.vkbot.controller.menuprogram.PropertiesProgramWindowController;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;


public final class StaticModel {
    private static TransportClient transportClient = HttpTransportClient.getInstance();
    private static VkApiClient vk = new VkApiClient(transportClient);
    // TODO: разобраться с UserActor
    private static UserActor actor = new UserActor(Integer.parseInt(PropertiesProgramWindowController.userId1), PropertiesProgramWindowController.token1);

    public static UserBot userBot = new UserBot(vk, actor);
    public static Database database = new Database();


    private static GroupActor groupActor = new GroupActor(144853761, "0ed42462bb7ff49865bd92343711fbcab31b18ce3e7bfeabffed19019e05eb84aa25fa982d6412a9ab979");
    public static GroupBot groupBot = new GroupBot(vk, groupActor);
}
