package com.sasfmlzr.apivk.thread;

import com.sasfmlzr.apivk.client.BotApiClient;
import com.sasfmlzr.apivk.object.StatisticsVariable;
import com.sasfmlzr.apivk.object.UserRights;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Dialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThreadUserBot extends ThreadBot implements Runnable        //(содержащее метод run())          отправление сообщения в рекурсии в отдельном потоке
{
    private BotApiClient client;

    public ThreadUserBot(BotApiClient client, UserActor actor) {
        this.client = client;
        this.actor = actor;
    }

    private boolean stoped = false;
    private UserActor actor;

    public void run()         //Этот метод будет выполняться в побочном потоке
    {
        while (!stoped) {
            boolean exception = false;
            try {
                sendMessageUser(actor);
            } catch (ClientException | SQLException | InterruptedException | ApiException e) {
                exception = true;
                e.printStackTrace();
                System.out.print("Исключение в потоке бота \n");
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
        System.out.print("Побочный поток завершён \n");
    }

    private void stopped() {
        stoped = true;
    }

    //-----------------отправка сообщения, если есть непрочитанные-----------------//
    private void sendMessageUser(UserActor actor) throws ClientException, ApiException, InterruptedException, SQLException {

        while (client.stateBot.pushPowerBot) {
            client.stateBot.findMessage = false;        // совпадение с сообщением не найдено
            StatisticsVariable.countSendMessageUser = StatisticsVariable.countSendMessageUser + 1;
            long timeStartFunction = System.currentTimeMillis();


            client.stateBot.botWork = true;           // если метод запущен, то бот включен
            client.stateBot.priostanovka = false;   // для приостановки бота
            String message;         // сообщение бота
            String obrachenie = "Колян, ";                      //обращение к боту
            message = client.database.getBotRandomData().
                    get(client.other().randomId(client.database.
                            getBotRandomData().size())).response;          //сообщение берется из рандомной базы коляна
                        /*
           if (textMessageString.equals("")){
               System.out.print("textMessageString = " +textMessageString+ "\n");
               message = "Я Бот Колян, я выпил Блэйзера и не могу отвечать на пустые сообщения(в которых только вложения)";
           }*/
            List<Dialog> messagesList = createListMessageVK();                   // делается запрос непрочитанных сообщений
            obrachenie = "Колян, ";
            StatisticsVariable.timeZaprosFinishSumm = StatisticsVariable.timeZaprosFinishSumm + StatisticsVariable.timeZaprosFinishItogo;            // для среднего пинга


            UserRights userRight;

            if (messagesList.size() != 0) {
                int userID = messagesList.get(0).getMessage().getUserId();               // запись userID пользователя
                client.database.databaseRequest(client.database.getStatmt()).addInfoUser(userID, actor, client.getVkApiClient());       // добавить инфу о пользователе, если нет  // здесь есть запрос к вк
                client.database.databaseRequest(client.database.getStatmt()).addInfoUserRights(userID, actor);    // добавить права пользователю, если нет
                String userRightString = client.database.databaseRequest(client.database.getStatmt()).findUserRights(userID, actor);  // запись права текущего пользователя в ячейку
                userRight = new UserRights(userRightString);  // наследование прав пользователем
                //    System.out.print(userRight.getNameRight() + " может админить? -" + userRight.getAdminCommands() + "\n" +
                //           userRight.getNameRight() + " может писать боту? -" + userRight.getAllowWriteToBot() + "\n");


                String textMessageString = messagesList.get(0).getMessage().getBody().toLowerCase();       // прием сообщения в переменную
                if (userRight.getAdminCommands()) {
                    if (!client.stateBot.findMessage) {     // если совпадение с сообщением не найдено, то
                        if (textMessageString.contains(obrachenie.toLowerCase())) {

                            textMessageString = textMessageString.replaceAll(obrachenie.toLowerCase(), "");
                            textMessageString = textMessageString.replaceAll("[^ A-Za-zА-Яа-я0-9?]", "");       // замена знаков
                            String dublicateMessages = message;
                            message = client.messages().commands().adminCommandsBot(textMessageString, message);         //проверка на команды бота
                            if (!Objects.equals(message, dublicateMessages)) {
                                client.messages().vkSendMessage(actor, message, messagesList);
                                messagesList.clear();
                            }
                        }
                    }
                }
            }

            if (!client.stateBot.botStopped) {

                if (messagesList.size() != 0) {                                             // если нет непрочитанных, сообщений, то не выполнять
                    String textMessageString = messagesList.get(0).getMessage().getBody().toLowerCase();       // прием сообщения в переменную              TEST


                    //    statmt.execute("SELECT 'login','userID','UserRights' FROM 'UserRights' WHERE login="+actor.getId()+" AND userID="+userID);
                    client.stateBot.reduction = false;
                    if (!client.stateBot.findMessage) {     // если совпадение с сообщением не найдено, то
                        if (textMessageString.contains(obrachenie.toLowerCase())) {
                            textMessageString = textMessageString.replaceAll(obrachenie.toLowerCase(), "");
                            message = client.messages().commands().commandsBot(textMessageString, message, actor, messagesList, client);         //проверка на команды бота
                            textMessageString = textMessageString.replaceAll("[^ A-Za-zА-Яа-я0-9?]", "");       // замена знаков
                        }
                    }
                    if (!client.stateBot.reduction) {
                        message = messageFromDataBase(textMessageString, message);       // бд коляна
                        message = messageFromBigDataBase(textMessageString, message);    // большая бд
                        client.messages().vkSendMessage(actor, message, messagesList);       // отправка сообщения и пометка его прочитанным
                    } else {
                        Thread.sleep(500);
                        //            System.out.print("Нет новых сообщений" + "\n");
                    }


                    long timeFinishFunction = System.currentTimeMillis();
                    long timeConsumedMilliss = timeFinishFunction - timeStartFunction;
                    //            System.out.print("время countSendMessageUser= " + timeConsumedMilliss + "\n");
                    StatisticsVariable.timeItogoMsMinusVK = timeConsumedMilliss - StatisticsVariable.timeZaprosFinishItogo;
                    //            System.out.print("время прохода минус запросвк= " + timeItogoMsMinusVK + "\n");
                }
                if (client.stateBot.priostanovka) {
                    //                System.out.print("бот приостановлен \n");
                    Thread.sleep(60000);
                }
                delayThread(messagesList, client);          // поток засыпает

            } else {
                Thread.sleep(2000);
            }

/////////////////////////статистика
            //StatisticsWindowController.seriesZaprosVk.getData().add(new XYChart.Data(countSendMessageUser, timeZaprosFinishItogo));          //ведение статистики запросов
            //StatisticsWindowController.seriesItogVk.getData().add(new XYChart.Data(countSendMessageUser, timeItogoMsMinusVK));          //ведение статистики запросов
            //StatisticsWindowController.seriesThread.getData().add(new XYChart.Data(countSendMessageUser, timeDelayThread));                        //ведение статистики задержки потока////здесь иногда ловится исключение
        }
        client.database.CloseDB();         //закрытие бд
        client.stateBot.botWork = false;
    }

    //-----------------отправка и отслеживание запроса в вк на непрочитанные сообщения-------------------------------//
    private List<Dialog> createListMessageVK() throws ClientException, ApiException {
        long timezaprosstart = System.currentTimeMillis();         // начало запроса непрочитанного запроса
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
            for (int countDB = 0; countDB <= client.botApiClient().database.getBotData().size() - 1; countDB = countDB + 1) {
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
            long timeStartBigBD = System.currentTimeMillis();
            // путешествие по списку объектов из большой БД

            /*
            resSettingBigBD = statmt.executeQuery("SELECT requesttextbot, responsetextbot FROM RandomBazaBot");
                        System.out.print("BigMessagesData= "+ BigMessagesData.size()+ "\n");
            while(resSettingBigBD.next())
            {
                if ( resSettingBigBD.getString("requesttextbot").equals(textMessageString))  {  // сравниваем нижний регистр
                    // сравниваем сообщение и значение в БД
                    listMessages.add(resSettingBigBD.getString("responsetextbot"));
                    findMessage=true;
                }
            }
            resSettingBigBD.close();
            if (findMessage)
                messages = listMessages.get(randomIdBot(listMessages.size()));    // выбираем рандомно из найденного сообщение
*/

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
            long timeFinishBigBD = System.currentTimeMillis();
            StatisticsVariable.timeConsumedMillisBigBD = timeFinishBigBD - timeStartBigBD;
            StatisticsVariable.countUsedBigBD = StatisticsVariable.countUsedBigBD + 1;                                            // количество использований большой бд увеличилось на 1
            //StatisticsWindowController.seriesBigBD.getData().add(new XYChart.Data(countUsedBigBD, timeConsumedMillisBigBD));                        //ведение статистики задержки потока////здесь иногда ловится исключение
        }
        return messages;
    }
}
