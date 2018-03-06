package com.fomenko.vkbot;

import com.apiVKmanual.bot.GroupBot;
import com.apiVKmanual.bot.UserBot;
import com.apiVKmanual.functions.botdatabase.Database;
import com.fomenko.vkbot.controller.menuprogram.PropertiesProgramWindowController;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
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



    private static GroupActor groupActor = new GroupActor(144853761,"0ed42462bb7ff49865bd92343711fbcab31b18ce3e7bfeabffed19019e05eb84aa25fa982d6412a9ab979");
    // саит
    //private static GroupActor groupActor = new GroupActor(103545242,"072970b1f3026aa56332213f5292335206000af4c2b808152138b6991ce440be875ea1b1cd8cd2c5fc53c");
    public  static GroupBot groupBot = new GroupBot(vk, groupActor);





}
