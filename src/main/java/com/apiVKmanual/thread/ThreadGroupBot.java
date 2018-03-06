package com.apiVKmanual.thread;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Dialog;

import java.util.ArrayList;
import java.util.List;

import static com.apiVKmanual.object.StatisticsVariable.*;
import static com.fomenko.vkbot.StaticModel.*;

@SuppressWarnings("Duplicates")
public class ThreadGroupBot implements Runnable{

    private boolean stoped = false;
    private GroupActor actor;
    public ThreadGroupBot(GroupActor actor) {
        this.actor=actor;


    }

    @Override
    public void run() {
        while (!stoped) {
            boolean exception = false;
            try {
                sendMessageUser(actor);
            } catch (ClientException | InterruptedException | ApiException e) {
                exception = true;
                e.printStackTrace();
                System.out.println("Исключение в потоке бота");
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
        System.out.println("Побочный поток завершён");
    }
    private void stopped() {
        stoped = true;
    }

    //-----------------отправка сообщения, если есть непрочитанные-----------------//
    public void sendMessageUser(GroupActor actor) throws ClientException, ApiException, InterruptedException {

        while (pushPowerBot) {
            findMessage = false;        // совпадение с сообщением не найдено
            countSendMessageUser = countSendMessageUser + 1;
            long timeStartFunction = System.currentTimeMillis();


            botWork = true;           // если метод запущен, то бот включен
            priostanovka = false;   // для приостановки бота
            String message;         // сообщение бота


            message = groupBot.botApiClient().database.getBotRandomData().
                    get(groupBot.botApiClient().other().randomId(groupBot.botApiClient().database.
                            getBotRandomData().size())).response;          //сообщение берется из рандомной базы коляна
                        /*
           if (textMessageString.equals("")){
               System.out.print("textMessageString = " +textMessageString+ "\n");
               message = "Я Бот Колян, я выпил Блэйзера и не могу отвечать на пустые сообщения(в которых только вложения)";
           }*/
            List<Dialog> messagesList = createListMessageVK();                   // делается запрос непрочитанных сообщений





            if (!botStopped) {




                if (messagesList.size() != 0) {                                             // если нет непрочитанных, сообщений, то не выполнять
                    String textMessageString =  messagesList.get(0).getMessage().getBody().toLowerCase();       // прием сообщения в переменную              TEST


                   reduction = false;

                    if (!reduction) {
                        message = messageFromBigDataBase(textMessageString, message);
                        groupBot.botApiClient().messages().vkSendMessage(actor, message, messagesList);       // отправка сообщения и пометка его прочитанным
                    } else {
                        Thread.sleep(500);
                                    System.out.print("Нет новых сообщений" + "\n");
                    }
                    }

                delayThread(messagesList);          // поток засыпает

            }else{
                Thread.sleep(2000);
            }
  }
        groupBot.botApiClient().database.CloseDB();         //закрытие бд
        botWork = false;
    }

    //-----------------отправка и отслеживание запроса в вк на непрочитанные сообщения-------------------------------//
    private List<Dialog> createListMessageVK() throws ClientException, ApiException {
        long timezaprosstart =       System.currentTimeMillis();         // начало запроса непрочитанного запроса
        List<Dialog> messages = groupBot.getVk().messages().getDialogs(groupBot.getActor())                 // Листы сообщений
                .unread(true)
                .execute().getItems();
        long timezaprosfinish = System.currentTimeMillis();
        timeZaprosFinishItogo = timezaprosfinish-timezaprosstart ;      // время, затраченное на операцию
        return messages;
    }
    //-----------------поиск сообщения в основной БД----------------------------------------------//
    private  String messageFromDataBase(String textMessageString, String message) {           //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        String messages=message;
        List<String> listMessages = new ArrayList<>();
        if (!findMessage){      // если совпадение с сообщением не найдено, то
            long timeStartBD =       System.currentTimeMillis();
            // путешествие по списку объектов из БД
            for (int countDB = 0; countDB <= groupBot.botApiClient().database.getBotData().size() - 1; countDB = countDB + 1) {
                if ( groupBot.botApiClient().database.getBotData().get(countDB).request.toLowerCase().equals(textMessageString.toLowerCase()))  {  // сравниваем нижний регистр
                    listMessages.add(groupBot.botApiClient().database.getBotData().get(countDB).response);
                    findMessage=true;                                                           // совпадение с сообщением найдено
                }
            }
            if (findMessage)    // если совпадение с сообщением найдено, то
                messages = listMessages.get(groupBot.botApiClient().other().randomId(listMessages.size()));    // выбираем рандомно из найденного сообщение
            long timeFinishBD =      System.currentTimeMillis();
            timeConsumedMillisBD = timeFinishBD - timeStartBD;
            countUsedBD=countUsedBD+1;                                              // количество использований бд коляна увеличилось на 1
            //        System.out.print("время операции по бд коляна= "+ timeConsumedMillisBD + "\n");
        }
        return messages;
    }

    //-----------------задержка потока-----------------------------------------------//         //test
    private static void delayThread(List messagesList) throws InterruptedException {
        if (!testSpeed){
            if (messagesList.size() != 0) {
                countSleep = 0;
                timeDelayThread = 300;
                Thread.sleep(timeDelayThread);
            }
            if (countSleep <= 5) {
                countSleep = countSleep + 1;
                timeDelayThread = 700 + countSleep * 100;
                Thread.sleep(timeDelayThread);
            } else {
                countSleep = countSleep + 1;
                timeDelayThread = 1500 + countSleep * 100;
                Thread.sleep(timeDelayThread);
            }
            if (countSleep >= 30) {
                countSleep = 6;
            }
        }
        else {
            timeDelayThread = 1500;
            Thread.sleep(timeDelayThread);
        }
    }








    //-----------------поиск сообщения в большой БД-----------------------------------------------//
    private String messageFromBigDataBase(String textMessageString, String message) {       //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        String messages=message;
        List<String> listMessages = new ArrayList<>();
        if (!findMessage){      // если совпадение с сообщением не найдено, то

            for (int countDB = 0; countDB <= userBot.botApiClient().database.getBigMessagesData().size() - 1; countDB = countDB + 1) {
                if ( userBot.botApiClient().database.getBigMessagesData().get(countDB).request.toLowerCase().equals(textMessageString))  {  // сравниваем нижний регистр
                    // сравниваем сообщение и значение в БД
                    listMessages.add(userBot.botApiClient().database.getBigMessagesData().get(countDB).response);
                    findMessage=true;                                                   // совпадение с сообщением найдено
                    // messages = listMessages.get(randomIdBot(listMessages.size()));    // выбираем рандомно из найденного сообщение
                }
            }
            if (findMessage)                                                            // если совпадение с сообщением найдено, то
            {
                messages = listMessages.get(userBot.botApiClient().other().randomId(listMessages.size()));          // выбираем рандомно из найденного сообщение
            }
    }
        return messages;
    }









}