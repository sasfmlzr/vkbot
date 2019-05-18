package com.sasfmlzr.apivk.thread;

import com.sasfmlzr.apivk.client.BotApiClient;
import com.sasfmlzr.apivk.object.StatisticsVariable;

import java.util.List;

abstract class ThreadBot {
    //-----------------задержка потока-----------------------------------------------//         //test
    void delayThread(List messagesList, BotApiClient client) throws InterruptedException {
        //if (!client.stateBot.testSpeed)
        if (!client.getStateBot().getTestSpeed()) {
            if (messagesList.size() != 0) {
                client.getStateBot().setCountSleep(0);
                StatisticsVariable.timeDelayThread = 300;
                Thread.sleep(StatisticsVariable.timeDelayThread);
            }
            if (client.getStateBot().getCountSleep() <= 5) {
                client.getStateBot().setCountSleep(client.getStateBot().getCountSleep()+1);
                StatisticsVariable.timeDelayThread = 700 + client.getStateBot().getCountSleep() * 100;
                Thread.sleep(StatisticsVariable.timeDelayThread);
            } else {
                client.getStateBot().setCountSleep(client.getStateBot().getCountSleep()+1);
                StatisticsVariable.timeDelayThread = 1500 + client.getStateBot().getCountSleep() * 100;
                Thread.sleep(StatisticsVariable.timeDelayThread);
            }
            if (client.getStateBot().getCountSleep() >= 30) {
                client.getStateBot().setCountSleep(6);
            }
        } else {
            StatisticsVariable.timeDelayThread = 1500;
            Thread.sleep(StatisticsVariable.timeDelayThread);
        }
    }
}
