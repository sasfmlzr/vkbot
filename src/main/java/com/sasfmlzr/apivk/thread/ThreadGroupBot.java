package com.sasfmlzr.apivk.thread;

import com.sasfmlzr.apiVK.client.BotApiClient;
import com.sasfmlzr.apiVK.object.StatisticsVariable;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Dialog;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class ThreadGroupBot extends ThreadBot implements Runnable {

    public ThreadGroupBot(BotApiClient client, GroupActor actor) {
        this.client = client;
        this.actor = actor;
    }

    private BotApiClient client;
    private boolean stoped = false;
    private GroupActor actor;

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
            if (!exception) {
                stopped();
            } else {
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

        while (client.stateBot.pushPowerBot) {
            client.stateBot.findMessage = false;        // совпадение с сообщением не найдено
            StatisticsVariable.countSendMessageUser = StatisticsVariable.countSendMessageUser + 1;
            long timeStartFunction = System.currentTimeMillis();


            client.stateBot.botWork = true;           // если метод запущен, то бот включен
            client.stateBot.priostanovka = false;   // для приостановки бота
            String message;         // сообщение бота


            message = client.database.getBotRandomData().
                    get(client.other().randomId(client.database.
                            getBotRandomData().size())).response;          //сообщение берется из рандомной базы коляна
                        /*
           if (textMessageString.equals("")){
               System.out.print("textMessageString = " +textMessageString+ "\n");
               message = "Я Бот Колян, я выпил Блэйзера и не могу отвечать на пустые сообщения(в которых только вложения)";
           }*/
            List<Dialog> messagesList = createListMessageVK();                   // делается запрос непрочитанных сообщений


            if (!client.stateBot.botStopped) {


                if (messagesList.size() != 0) {                                             // если нет непрочитанных, сообщений, то не выполнять
                    String textMessageString = messagesList.get(0).getMessage().getBody().toLowerCase();       // прием сообщения в переменную              TEST


                    client.stateBot.reduction = false;

                    if (!client.stateBot.reduction) {
                        message = messageFromBigDataBase(textMessageString, message);
                        client.messages().vkSendMessage(actor, message, messagesList);       // отправка сообщения и пометка его прочитанным
                    } else {
                        Thread.sleep(500);
                        System.out.print("Нет новых сообщений" + "\n");
                    }
                }

                delayThread(messagesList, client);          // поток засыпает

            } else {
                Thread.sleep(2000);
            }
        }
        client.database.CloseDB();         //закрытие бд
        client.stateBot.botWork = false;
    }

    //-----------------отправка и отслеживание запроса в вк на непрочитанные сообщения-------------------------------//
    private List<Dialog> createListMessageVK() throws ClientException, ApiException {
        long timezaprosstart = System.currentTimeMillis();         // начало запроса непрочитанного запроса

        //client.getVkApiClient().messages().send(actor).userId(30562433).randomId(234).message("fdffgf").execute();
        List<Dialog> messages = client.getVkApiClient().messages().getDialogs(actor)                 // Листы сообщений
                .unread(true)
                .execute().getItems();
        long timezaprosfinish = System.currentTimeMillis();
        StatisticsVariable.timeZaprosFinishItogo = timezaprosfinish - timezaprosstart;      // время, затраченное на операцию
        return messages;
    }

    //-----------------поиск сообщения в основной БД----------------------------------------------//
    private String messageFromDataBase(String textMessageString, String message) {           //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        String messages = message;
        List<String> listMessages = new ArrayList<>();
        if (!client.stateBot.findMessage) {      // если совпадение с сообщением не найдено, то
            long timeStartBD = System.currentTimeMillis();
            // путешествие по списку объектов из БД
            for (int countDB = 0; countDB <= client.database.getBotData().size() - 1; countDB = countDB + 1) {
                if (client.database.getBotData().get(countDB).request.toLowerCase().equals(textMessageString.toLowerCase())) {  // сравниваем нижний регистр
                    listMessages.add(client.database.getBotData().get(countDB).response);
                    client.stateBot.findMessage = true;                                                           // совпадение с сообщением найдено
                }
            }
            if (client.stateBot.findMessage)    // если совпадение с сообщением найдено, то
                messages = listMessages.get(client.other().randomId(listMessages.size()));    // выбираем рандомно из найденного сообщение
            long timeFinishBD = System.currentTimeMillis();
            StatisticsVariable.timeConsumedMillisBD = timeFinishBD - timeStartBD;
            StatisticsVariable.countUsedBD = StatisticsVariable.countUsedBD + 1;                                              // количество использований бд коляна увеличилось на 1
            //        System.out.print("время операции по бд коляна= "+ timeConsumedMillisBD + "\n");
        }
        return messages;
    }

    //-----------------поиск сообщения в большой БД-----------------------------------------------//
    private String messageFromBigDataBase(String textMessageString, String message) {       //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        String messages = message;
        List<String> listMessages = new ArrayList<>();
        if (!client.stateBot.findMessage) {      // если совпадение с сообщением не найдено, то

            for (int countDB = 0; countDB <= client.database.getBigMessagesData().size() - 1; countDB = countDB + 1) {
                if (client.database.getBigMessagesData().get(countDB).request.toLowerCase().equals(textMessageString)) {  // сравниваем нижний регистр
                    // сравниваем сообщение и значение в БД
                    listMessages.add(client.database.getBigMessagesData().get(countDB).response);
                    client.stateBot.findMessage = true;                                                   // совпадение с сообщением найдено
                    // messages = listMessages.get(randomIdBot(listMessages.size()));    // выбираем рандомно из найденного сообщение
                }
            }
            if (client.stateBot.findMessage)                                                            // если совпадение с сообщением найдено, то
            {
                messages = listMessages.get(client.other().randomId(listMessages.size()));          // выбираем рандомно из найденного сообщение
            }
        }
        return messages;
    }
}
