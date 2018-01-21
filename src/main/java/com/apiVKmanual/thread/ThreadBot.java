package com.apiVKmanual.thread;

import com.api.client.Client;
import com.apiVKmanual.object.UserRights;
import com.fomenko.vkbot.controller.menuprogram.StatisticsWindowController;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Dialog;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.apiVKmanual.object.StatisticsVariable.*;
import static com.fomenko.vkbot.StaticModel.*;



public class ThreadBot implements Runnable 		//(содержащее метод run())          отправление сообщения в рекурсии в отдельном потоке
{

    private boolean stoped = false;

    private UserActor actor;
    public ThreadBot(UserActor actor){

        this.actor=actor;
    }

    public void run()         //Этот метод будет выполняться в побочном потоке
    {

        while (!stoped) {
            boolean exception = false;
                if (Client.actor==null){
                    //TODO: staticmodel
                   // actor= s;
                }else
                    actor=Client.actor;
            try {
                sendMessageUser(actor);
            } catch (ClientException | SQLException   | InterruptedException | ApiException e) {
                exception = true;
                e.printStackTrace();
                System.out.print("Исключение в потоке бота \n");
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


    //-----------------отправка сообщения, если есть непрочитанные-----------------//
    public void sendMessageUser(UserActor actor) throws ClientException, ApiException, InterruptedException, SQLException {

        while (pushPowerBot) {
            findMessage = false;        // совпадение с сообщением не найдено
            countSendMessageUser = countSendMessageUser + 1;
            long timeStartFunction = System.currentTimeMillis();


            botWork = true;           // если метод запущен, то бот включен
            priostanovka = false;   // для приостановки бота
            String message;         // сообщение бота
            String obrachenie = "Колян, ";                      //обращение к боту

            message = userBot.botApiClient().database.getBotRandomData().
                    get(userBot.botApiClient().other().randomId(userBot.botApiClient().database.
                            getBotRandomData().size())).response;          //сообщение берется из рандомной базы коляна
                        /*
           if (textMessageString.equals("")){
               System.out.print("textMessageString = " +textMessageString+ "\n");
               message = "Я Бот Колян, я выпил Блэйзера и не могу отвечать на пустые сообщения(в которых только вложения)";
           }*/
            List<Dialog> messagesList = createListMessageVK();                   // делается запрос непрочитанных сообщений
            obrachenie = "Колян, ";
            timeZaprosFinishSumm = timeZaprosFinishSumm + timeZaprosFinishItogo;            // для среднего пинга



            UserRights userRight= new UserRights();

            if (messagesList.size() != 0) {
                int userID =  messagesList.get(0).getMessage().getUserId();               // запись userID пользователя
                userBot.botApiClient().database.databaseRequest(userBot.botApiClient().database.getStatmt()).addInfoUser(userID, actor, userBot.getVk());       // добавить инфу о пользователе, если нет  // здесь есть запрос к вк
                userBot.botApiClient().database.databaseRequest(userBot.botApiClient().database.getStatmt()).addInfoUserRights(userID, actor);    // добавить права пользователю, если нет
                String userRightString = userBot.botApiClient().database.databaseRequest(userBot.botApiClient().database.getStatmt()).findUserRights(userID, actor);  // запись права текущего пользователя в ячейку
                userRight = new UserRights(userRightString);  // наследование прав пользователем
                //    System.out.print(userRight.getNameRight() + " может админить? -" + userRight.getAdminCommands() + "\n" +
                //           userRight.getNameRight() + " может писать боту? -" + userRight.getAllowWriteToBot() + "\n");



                String textMessageString = messagesList.get(0).getMessage().getBody().toLowerCase();       // прием сообщения в переменную
                if (userRight.getAdminCommands()) {
                    if (!findMessage) {     // если совпадение с сообщением не найдено, то
                        if (textMessageString.contains(obrachenie.toLowerCase())) {

                            textMessageString = textMessageString.replaceAll(obrachenie.toLowerCase(), "");
                            textMessageString = textMessageString.replaceAll("[^ A-Za-zА-Яа-я0-9?]", "");       // замена знаков
                            String dublicateMessages=message;
                            message = userBot.botApiClient().messages().commands().adminCommandsBot(textMessageString, message);         //проверка на команды бота
                            if (!Objects.equals(message, dublicateMessages)) {
                                userBot.botApiClient().messages().vkSendMessage(actor, message, messagesList);
                                messagesList.clear();
                            }
                        }
                    }
                }
            }

            if (!botStopped) {




                if (messagesList.size() != 0) {                                             // если нет непрочитанных, сообщений, то не выполнять
                    String textMessageString =  messagesList.get(0).getMessage().getBody().toLowerCase();       // прием сообщения в переменную              TEST


                    //    statmt.execute("SELECT 'login','userID','UserRights' FROM 'UserRights' WHERE login="+actor.getId()+" AND userID="+userID);
                    reduction = false;
                    if (!findMessage) {     // если совпадение с сообщением не найдено, то
                        if (textMessageString.contains(obrachenie.toLowerCase())) {
                            textMessageString = textMessageString.replaceAll(obrachenie.toLowerCase(), "");
                            message = userBot.botApiClient().messages().commands().commandsBot(textMessageString, message, actor, messagesList, userBot.botApiClient());         //проверка на команды бота
                            textMessageString = textMessageString.replaceAll("[^ A-Za-zА-Яа-я0-9?]", "");       // замена знаков
                        }
                    }
                    if (!reduction) {
                        message = messageFromDataBase(textMessageString, message);       // бд коляна
                        message = messageFromBigDataBase(textMessageString, message);    // большая бд
                        userBot.botApiClient().messages().vkSendMessage(actor, message, messagesList);       // отправка сообщения и пометка его прочитанным
                    } else {
                        Thread.sleep(500);
                        //            System.out.print("Нет новых сообщений" + "\n");
                    }


                    long timeFinishFunction = System.currentTimeMillis();
                    long timeConsumedMilliss = timeFinishFunction - timeStartFunction;
                    //            System.out.print("время countSendMessageUser= " + timeConsumedMilliss + "\n");
                    timeItogoMsMinusVK = timeConsumedMilliss - timeZaprosFinishItogo;
                    //            System.out.print("время прохода минус запросвк= " + timeItogoMsMinusVK + "\n");
                }
                if (priostanovka) {
                    //                System.out.print("бот приостановлен \n");
                    Thread.sleep(60000);
                }
                delayThread(messagesList);          // поток засыпает

            }else{
                Thread.sleep(2000);
            }

/////////////////////////статистика
            StatisticsWindowController.seriesZaprosVk.getData().add(new XYChart.Data(countSendMessageUser, timeZaprosFinishItogo));          //ведение статистики запросов
            StatisticsWindowController.seriesItogVk.getData().add(new XYChart.Data(countSendMessageUser, timeItogoMsMinusVK));          //ведение статистики запросов
            StatisticsWindowController.seriesThread.getData().add(new XYChart.Data(countSendMessageUser, timeDelayThread));                        //ведение статистики задержки потока////здесь иногда ловится исключение
        }
        userBot.botApiClient().database.CloseDB();         //закрытие бд
        botWork = false;
    }

    //-----------------отправка и отслеживание запроса в вк на непрочитанные сообщения-------------------------------//
    private List<Dialog> createListMessageVK() throws ClientException, ApiException {
        long timezaprosstart =       System.currentTimeMillis();         // начало запроса непрочитанного запроса
        List<Dialog> messages = userBot.getVk().messages().getDialogs(userBot.getActor())                 // Листы сообщений
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
            for (int countDB = 0; countDB <= userBot.botApiClient().database.getBotData().size() - 1; countDB = countDB + 1) {
                if ( userBot.botApiClient().database.getBotData().get(countDB).request.toLowerCase().equals(textMessageString.toLowerCase()))  {  // сравниваем нижний регистр
                    listMessages.add(userBot.botApiClient().database.getBotData().get(countDB).response);
                    findMessage=true;                                                           // совпадение с сообщением найдено
                }
            }
            if (findMessage)    // если совпадение с сообщением найдено, то
                messages = listMessages.get(userBot.botApiClient().other().randomId(listMessages.size()));    // выбираем рандомно из найденного сообщение
            long timeFinishBD =      System.currentTimeMillis();
            timeConsumedMillisBD = timeFinishBD - timeStartBD;
            countUsedBD=countUsedBD+1;                                              // количество использований бд коляна увеличилось на 1
            //        System.out.print("время операции по бд коляна= "+ timeConsumedMillisBD + "\n");
        }
        return messages;
    }
    //-----------------поиск сообщения в большой БД-----------------------------------------------//
    private String messageFromBigDataBase(String textMessageString, String message) {       //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        String messages=message;
        List<String> listMessages = new ArrayList<>();
        if (!findMessage){      // если совпадение с сообщением не найдено, то
            long timeStartBigBD =       System.currentTimeMillis();
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
            long timeFinishBigBD =      System.currentTimeMillis();
            timeConsumedMillisBigBD = timeFinishBigBD - timeStartBigBD;
            countUsedBigBD=countUsedBigBD+1;                                            // количество использований большой бд увеличилось на 1
            StatisticsWindowController.seriesBigBD.getData().add(new XYChart.Data(countUsedBigBD, timeConsumedMillisBigBD));                        //ведение статистики задержки потока////здесь иногда ловится исключение
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


}













