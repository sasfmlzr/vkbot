package com.sasfmlzr.apivk.thread

import com.sasfmlzr.apivk.client.BotApiClient
import com.sasfmlzr.apivk.`object`.StatisticsVariable

 abstract class ThreadBot {
    //-----------------задержка потока-----------------------------------------------//         //test
    @Throws(InterruptedException::class)
    fun delayThread(messagesList: List<*>, client: BotApiClient) {
        //if (!client.stateBot.testSpeed)
        if (!client.stateBot.testSpeed) {
            if (messagesList.isNotEmpty()) {
                client.stateBot.countSleep = 0
                StatisticsVariable.timeDelayThread = 300
                Thread.sleep(StatisticsVariable.timeDelayThread.toLong())
            }
            if (client.stateBot.countSleep <= 5) {
                client.stateBot.countSleep = client.stateBot.countSleep + 1
                StatisticsVariable.timeDelayThread = 700 + client.stateBot.countSleep * 100
                Thread.sleep(StatisticsVariable.timeDelayThread.toLong())
            } else {
                client.stateBot.countSleep = client.stateBot.countSleep + 1
                StatisticsVariable.timeDelayThread = 1500 + client.stateBot.countSleep * 100
                Thread.sleep(StatisticsVariable.timeDelayThread.toLong())
            }
            if (client.stateBot.countSleep >= 30) {
                client.stateBot.countSleep = 6
            }
        } else {
            StatisticsVariable.timeDelayThread = 1500
            Thread.sleep(StatisticsVariable.timeDelayThread.toLong())
        }
    }
}
