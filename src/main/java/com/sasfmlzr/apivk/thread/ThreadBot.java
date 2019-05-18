package com.sasfmlzr.apivk.thread;

import com.sasfmlzr.apiVK.client.BotApiClient;
import com.sasfmlzr.apiVK.object.StatisticsVariable;

import java.util.List;

abstract class ThreadBot {
    //-----------------задержка потока-----------------------------------------------//         //test
    void delayThread(List messagesList, BotApiClient client) throws InterruptedException {
        //if (!client.stateBot.testSpeed)
            if (!client.stateBot.testSpeed)
        {
            if (messagesList.size() != 0) {
                client.stateBot.countSleep = 0;
                StatisticsVariable.timeDelayThread = 300;
                Thread.sleep(StatisticsVariable.timeDelayThread);
            }
            if (client.stateBot.countSleep <= 5) {
                client.stateBot.countSleep ++;
                StatisticsVariable.timeDelayThread = 700 + client.stateBot.countSleep * 100;
                Thread.sleep(StatisticsVariable.timeDelayThread);
            } else {
                client.stateBot.countSleep ++;
                StatisticsVariable.timeDelayThread = 1500 + client.stateBot.countSleep * 100;
                Thread.sleep(StatisticsVariable.timeDelayThread);
            }
            if (client.stateBot.countSleep >= 30) {
                client.stateBot.countSleep = 6;
            }
        }
        else {
            StatisticsVariable.timeDelayThread = 1500;
            Thread.sleep(StatisticsVariable.timeDelayThread);
        }
    }

}
