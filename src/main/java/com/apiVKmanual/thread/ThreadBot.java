package com.apiVKmanual.thread;

import com.apiVKmanual.client.BotApiClient;

import java.util.List;

import static com.apiVKmanual.object.StatisticsVariable.timeDelayThread;

abstract class ThreadBot {
    //-----------------задержка потока-----------------------------------------------//         //test
    void delayThread(List messagesList, BotApiClient client) throws InterruptedException {
        //if (!client.stateBot.testSpeed)
            if (!client.stateBot.testSpeed)
        {
            if (messagesList.size() != 0) {
                client.stateBot.countSleep = 0;
                timeDelayThread = 300;
                Thread.sleep(timeDelayThread);
            }
            if (client.stateBot.countSleep <= 5) {
                client.stateBot.countSleep ++;
                timeDelayThread = 700 + client.stateBot.countSleep * 100;
                Thread.sleep(timeDelayThread);
            } else {
                client.stateBot.countSleep ++;
                timeDelayThread = 1500 + client.stateBot.countSleep * 100;
                Thread.sleep(timeDelayThread);
            }
            if (client.stateBot.countSleep >= 30) {
                client.stateBot.countSleep = 6;
            }
        }
        else {
            timeDelayThread = 1500;
            Thread.sleep(timeDelayThread);
        }
    }

}
