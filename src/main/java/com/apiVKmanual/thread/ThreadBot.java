package com.apiVKmanual.thread;

import com.api.client.Client;

import com.fomenko.vkbot.controller.BotTabController;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import java.io.IOException;
import java.sql.SQLException;



public class ThreadBot extends Thread		//(содержащее метод run())          отправление сообщения в рекурсии в отдельном потоке
{
    public static UserActor actor;
    private boolean stoped = false;

    public void run()         //Этот метод будет выполняться в побочном потоке
    {

        while (!stoped) {
            boolean exception = false;
                if (Client.actor==null){
                    actor= BotTabController.actor;
                }else
                    actor=Client.actor;
            try {
                BotTabController.sendMessageUser(actor);
            } catch (ClientException | SQLException  | IOException | InterruptedException | ApiException e) {
                exception = true;
                e.printStackTrace();
                System.out.print("Исключение в потоке бота \n");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (!exception){
                stopped();
            }else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        System.out.print("Побочный поток завершён \n");
    }
    private void stopped() {
        stoped = true;
    }
}













