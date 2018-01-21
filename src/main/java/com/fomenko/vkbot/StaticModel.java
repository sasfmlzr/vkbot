package com.fomenko.vkbot;

import com.apiVKmanual.bot.UserBot;
import com.apiVKmanual.functions.botdatabase.Database;
import com.fomenko.vkbot.controller.menuprogram.PropertiesProgramWindowController;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import java.sql.ResultSet;

public final class StaticModel {
    private static TransportClient transportClient = HttpTransportClient.getInstance();
    private static VkApiClient vk = new VkApiClient(transportClient);
    // TODO: разобраться с UserActor
    private static UserActor actor = new UserActor(Integer.parseInt(PropertiesProgramWindowController.userId1), PropertiesProgramWindowController.token1);

    public  static UserBot userBot = new UserBot(vk,actor);
    public static Database database = new Database();





    public static int countSleep = 0;              // Thread sleeping
    public static boolean databaseLoaded=false;
    public static boolean testSpeed=true;          // test memory
    public static ResultSet resSettingBigBD;
    public static boolean botWork;
    public static boolean priostanovka;            // pause bot
    public static boolean botStopped=false;            // long pause bot
    public static boolean findMessage;
    public static boolean pushPowerBot=true;
    public static boolean reduction=false;
}
