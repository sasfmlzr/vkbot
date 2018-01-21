package com.fomenko.vkbot.controller;

import com.api.client.Client;
import com.apiVKmanual.UserBot;
import com.apiVKmanual.client.BotApiClient;
import com.apiVKmanual.object.UserRights;
import com.apiVKmanual.thread.ThreadBot;
import com.fomenko.vkbot.controller.menuprogram.DataBaseWindowController;
import com.fomenko.vkbot.controller.menuprogram.PropertiesProgramWindowController;
import com.fomenko.vkbot.controller.menuprogram.StatisticsWindowController;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.account.UserSettings;
import com.vk.api.sdk.objects.messages.Dialog;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.api.client.Client.idBot;
import static com.apiVKmanual.functions.bot.Commands.adminCommandsBot;
import static com.apiVKmanual.functions.bot.Commands.commandsBot;
import static com.apiVKmanual.functions.botdatabase.DatabaseRequest.*;
import static com.apiVKmanual.object.StatisticsVariable.*;
import static com.fomenko.vkbot.controller.BotCardController.pushPowerBot;
import static com.fomenko.vkbot.controller.menuprogram.DataBaseWindowController.*;
import static com.vk.api.sdk.queries.users.UserField.PHOTO_200;


public class BotTabController extends AnchorPane implements Initializable {


    //final static String fxmlPath = "/com/fomenko/vkbot/views/BotTab.fxml";

    @FXML    private FlowPane botCardPane;
    @FXML    private ListView vkdialog;
    @FXML    private TextField textMessage;
    @FXML    private TextField users_id;
    @FXML    private TextArea textMessages;
    @FXML    private TextArea textLog;
    @FXML    private Button botLog;
    @FXML    private static ImageView imageTest;


    private static String[] lfName = new String[30];                            // массив строк из листа - имя и фамилия
    private static int[] userIDmassive = new int[30];                           // массив userID
    private static TransportClient transportClient = HttpTransportClient.getInstance();
    private static VkApiClient vk = new VkApiClient(transportClient);
    private static BotApiClient bot = new BotApiClient(vk);
    public  static UserActor actor = new UserActor(Integer.parseInt(PropertiesProgramWindowController.userId1), PropertiesProgramWindowController.token1);
    private static int countSleep = 0;                                          // период засыпания побочного потока
    private static UserBot userBot;

    public static boolean databaseLoaded=false;
    private static boolean testSpeed=true;          // ТЕСТОВАЯ ПРОВЕРКА НА КОЛИЧЕСТВЕННОСТЬ СТАТИСТИКИ И ПОЖИРАНИЕ ПАМЯТИ
    public static ResultSet resSettingBigBD;
    public static boolean botWork;                          // бот работает?
    public static boolean priostanovka;            // команда приостановки бота
    public static boolean botStopped=false;            // команда длительной остановки бота
    public static boolean findMessage;


    private static List<BotCardController> childList;


    BotTabController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fomenko/vkbot/views/BotTab.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(BotTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }





    //-----------------инициалихация-----------------------------------------------//
    public void initialize(URL location, ResourceBundle resources) {
        childList = new ArrayList<>();
        childList.add(new BotCardController());
        botCardPane.getChildren().addAll( childList);
    }
    //-----------------при нажатии на диалог---------------------------------------//   // test
    public void dialog() {
        System.out.println("UserID = " + "\n");
    }
    //-----------------ручная отправка сообщения-----------------------------------//
    public void pushButton() throws ClientException, ApiException {



        if (!Objects.equals(textMessage.getText(), ""))
            bot.messages().vkSendMessageUser(actor,textMessage.getText(),Integer.parseInt(users_id.getText()));
    }
    //-----------------обновление сообщений----------------------------------------//
    public void requestMessage() throws ClientException, ApiException {


        String token = Client.token;
        UserActor actor = new UserActor(idBot, token);

        UserSettings infoAccount = vk.account().getProfileInfo(actor)
                .execute();
        String firstNameAccount = infoAccount.getFirstName();                   //имя и фамилия клинта приложения
        String lastNameNameAccount =  infoAccount.getLastName();
        int useridmessage = userIDmassive[vkdialog.getSelectionModel().getSelectedIndex()];

        textMessages.setText("");                   //имя и фамилия клинта приложения

        List messageList = vk.messages().getHistory(actor)
                .count(40)
                .userId(useridmessage)
                .execute().getItems();


        String[] textMessage = new String[messageList.size()];

        int countMessage;                                        // счетчик диалогов
        for (countMessage = 0; countMessage <= messageList.size() - 1; countMessage = countMessage + 1) {
            textMessage[countMessage] = ((Message) messageList.get(countMessage)).getBody();     // массив сообщений
            if (((Message) messageList.get(countMessage)).getUserId().equals(((Message) messageList.get(countMessage)).getFromId())) {
                textMessages.appendText(vkdialog.getSelectionModel().getSelectedItem() + ": " + textMessage[countMessage] + "\n");
            } else {
                textMessages.appendText(firstNameAccount + " " + lastNameNameAccount + ": " + textMessage[countMessage] + "\n");
            }
        }

        System.out.print("vkdialog  " + vkdialog.getSelectionModel().getSelectedIndex() + "\n");
        System.out.print("vkdindex  " + lfName[vkdialog.getSelectionModel().getSelectedIndex()] + "\n");
        System.out.print("vkdindex  " + userIDmassive[vkdialog.getSelectionModel().getSelectedIndex()] + "\n");
        //  List listItems  =  vkdialog.getItems();
        //   vkdialog.getSelectionModel().getSelectedItem();
        //  System.out.print("getLongPollHistory = ");
    }
    //-----------------обновить диалоги--------------------------------------------//
    public void refreshDialogs() throws ClientException, ApiException {
        vkdialog.getItems().clear();

        String token = Client.token;
        UserActor actor = new UserActor(idBot, token);
////////////////////////////////
                                      // создаем объект user id
        List<Dialog> dialogList = vk.messages().getDialogs(actor)            // записываем в лист результат работы запроса
                .count(30)
                .execute().getItems();
        int countDialog;                                        // счетчик диалогов

        StringBuilder summUserID = new StringBuilder();                                     // список userID через запятую
        for (countDialog = 0; countDialog <= dialogList.size() - 1; countDialog = countDialog + 1) {
                                 //получаем диалог countSendMessageUser
            int userID = dialogList.get(countDialog).getMessage().getUserId();        // получаем userID диалога
            userIDmassive[countDialog] = userID;
            summUserID.append(userIDmassive[countDialog]).append(",");
////////////////////////////////получение userID диалога
        }
////////////////////////////получение имени и фамилии
        List<UserXtrCounters> infoUser = vk.users().get(actor)                                                                //берем информацию о пользователе
                .userIds(String.valueOf(summUserID.toString()))
                .execute();
        String[] lastName = new String[30];                                                                  // массив строк из листа - фамилия
        String[] firstName = new String[30];                                                                 // массив строк из листа - имя
        for (countDialog = 0; countDialog <= infoUser.size() - 1; countDialog = countDialog + 1) {
            lastName[countDialog] = infoUser.get(countDialog).getLastName();                  // получаем фамилию
            firstName[countDialog] = infoUser.get(countDialog).getFirstName();                // получаем имя
            lfName[countDialog] = firstName[countDialog] + " " + lastName[countDialog];                      //строка имени и фамилии
            //     System.out.println("lfName[countDialog] = " + lfName[countDialog]  + "\n");
            userIDmassive[countDialog] = infoUser.get(countDialog).getId();                   // id пользователя в массиве
            vkdialog.getItems().add(lfName[countDialog]);                                                    // загрузка в лист
        }
    }
    static boolean ava=false;
    //-----------------запуск бота по кнопке включения бота------------------------//
    static void  recursion() throws SQLException, ClassNotFoundException {
        countSendMessageUser=0;
        countSendMessage = 0;
        timeZaprosFinishSumm=0;
        StatisticsWindowController.seriesZaprosVk.getData().clear();          //обнуление статистики запросов
        StatisticsWindowController.seriesItogVk.getData().clear();          //обнуление статистики запросов
        StatisticsWindowController.seriesThread.getData().clear();                        //обнуление статистики задержки потока////здесь иногда ловится исключение

        if (!databaseLoaded){
            DataBaseWindowController.connectDatabase();            //подключение бд
            DataBaseWindowController.InitDB();          //инициализация таблиц бд в объект
        }

        timeProgramStart = System.currentTimeMillis();
        pushPowerBot=true;
        userBot = new UserBot(vk,actor);
        setAvatarBot(childList,userBot);
        userBot.run();
    }



    private static void setAvatarBot(List<BotCardController> childList, UserBot bot)   {
        childList.forEach((BotCardController) -> {
            BotCardController.settext(bot.getBotName());
            BotCardController.setavatar(bot.getBotImage());
        });
    }


    public static boolean reduction=false;

    //-----------------отправка сообщения, если есть непрочитанные-----------------//
        public static void sendMessageUser(UserActor actor) throws ClientException, ApiException, InterruptedException, SQLException, ClassNotFoundException {

        while (pushPowerBot) {
            findMessage = false;        // совпадение с сообщением не найдено
            countSendMessageUser = countSendMessageUser + 1;
            long timeStartFunction = System.currentTimeMillis();


            botWork = true;           // если метод запущен, то бот включен
            priostanovka = false;   // для приостановки бота
            String message;         // сообщение бота
            String obrachenie = "Колян, ";                      //обращение к боту

            message = botRandomData.get(bot.other().randomId(botRandomData.size())).response;          //сообщение берется из рандомной базы коляна
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
                addInfoUser(userID, actor, vk);       // добавить инфу о пользователе, если нет  // здесь есть запрос к вк
                addInfoUserRights(userID, actor);    // добавить права пользователю, если нет
                String userRightString = findUserRights(userID, actor);  // запись права текущего пользователя в ячейку
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
                            message = adminCommandsBot(textMessageString, message);         //проверка на команды бота
                            if (!Objects.equals(message, dublicateMessages)) {
                                bot.messages().vkSendMessage(actor, message, messagesList);
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
                            message = commandsBot(textMessageString, message, actor, messagesList, bot);         //проверка на команды бота
                            textMessageString = textMessageString.replaceAll("[^ A-Za-zА-Яа-я0-9?]", "");       // замена знаков

                        }
                    }
                    if (!reduction) {
                        message = messageFromDataBase(textMessageString, message);       // бд коляна
                        message = messageFromBigDataBase(textMessageString, message);    // большая бд
                        bot.messages().vkSendMessage(actor, message, messagesList);       // отправка сообщения и пометка его прочитанным
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
            DataBaseWindowController.CloseDB();         //закрытие бд
            botWork = false;
    }

    //-----------------отправка и отслеживание запроса в вк на непрочитанные сообщения-------------------------------//
    private static List<Dialog> createListMessageVK() throws ClientException, ApiException {
        long timezaprosstart =       System.currentTimeMillis();         // начало запроса непрочитанного запроса
        List<Dialog> messages = vk.messages().getDialogs(userBot.getActor())                 // Листы сообщений
                .unread(true)
                .execute().getItems();
        long timezaprosfinish = System.currentTimeMillis();
        timeZaprosFinishItogo = timezaprosfinish-timezaprosstart ;      // время, затраченное на операцию
        return messages;
    }
    //-----------------поиск сообщения в основной БД----------------------------------------------//
    private static String messageFromDataBase(String textMessageString, String message) {           //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        String messages=message;
        List<String> listMessages = new ArrayList<>();
        if (!findMessage){      // если совпадение с сообщением не найдено, то
            long timeStartBD =       System.currentTimeMillis();
            // путешествие по списку объектов из БД
            for (int countDB = 0; countDB <= botData.size() - 1; countDB = countDB + 1) {
                if ( botData.get(countDB).request.toLowerCase().equals(textMessageString.toLowerCase()))  {  // сравниваем нижний регистр
                    listMessages.add(botData.get(countDB).response);
                    findMessage=true;                                                           // совпадение с сообщением найдено
                }
            }
            if (findMessage)    // если совпадение с сообщением найдено, то
                messages = listMessages.get(bot.other().randomId(listMessages.size()));    // выбираем рандомно из найденного сообщение
            long timeFinishBD =      System.currentTimeMillis();
            timeConsumedMillisBD = timeFinishBD - timeStartBD;
            countUsedBD=countUsedBD+1;                                              // количество использований бд коляна увеличилось на 1
    //        System.out.print("время операции по бд коляна= "+ timeConsumedMillisBD + "\n");
        }
        return messages;
    }
    //-----------------поиск сообщения в большой БД-----------------------------------------------//
    private static String messageFromBigDataBase(String textMessageString, String message) {       //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
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

            for (int countDB = 0; countDB <= BigMessagesData.size() - 1; countDB = countDB + 1) {
                if ( BigMessagesData.get(countDB).request.toLowerCase().equals(textMessageString))  {  // сравниваем нижний регистр
                    // сравниваем сообщение и значение в БД
                    listMessages.add(BigMessagesData.get(countDB).response);
                    findMessage=true;                                                   // совпадение с сообщением найдено
                   // messages = listMessages.get(randomIdBot(listMessages.size()));    // выбираем рандомно из найденного сообщение

                }
            }
            if (findMessage)                                                            // если совпадение с сообщением найдено, то
            {
                messages = listMessages.get(bot.other().randomId(listMessages.size()));          // выбираем рандомно из найденного сообщение
            }

            long timeFinishBigBD =      System.currentTimeMillis();
            timeConsumedMillisBigBD = timeFinishBigBD - timeStartBigBD;
            countUsedBigBD=countUsedBigBD+1;                                            // количество использований большой бд увеличилось на 1
            StatisticsWindowController.seriesBigBD.getData().add(new XYChart.Data(countUsedBigBD, timeConsumedMillisBigBD));                        //ведение статистики задержки потока////здесь иногда ловится исключение
        }
        return messages;
    }
    //-----------------заполнение окна логов-----------------------------------------------//
    @FXML
    public void logFill() {
        if (botWork)
        {
            String statistic="Время, затраченное на последнюю операцию запроса непрочитанных сообщений "+timeZaprosFinishItogo+"мс\n" +
                    "Время, затраченное на последние остальные операции "+timeItogoMsMinusVK+"мс\n" +
                    "Подробнее:\n";
            if (timeConsumedMillisBD!=0)
                statistic=statistic+"Время последней выборки из бд коляна составляет " + timeConsumedMillisBD+"мс\n";
            if (timeConsumedMillisBigBD!=0)
                statistic=statistic+"Время последней выборки из большой бд составляет " + timeConsumedMillisBigBD+"мс\n";
            if (timeItogoSendMessage!=0)
                statistic=statistic+"Время затраченное на последнюю отправку сообщения составляет " + timeItogoSendMessage+"мс\n" +
                        "Количество отправленных сообщений равно " + countSendMessage + "\n";
            long timeProgramFinish = System.currentTimeMillis();
            long timeProgramItog = timeProgramFinish-timeProgramStart;
            statistic = statistic+"Время работы бота равно " + Math.round(timeProgramItog/1000) + "c\n"+
                    "Среднее время запроса до вк равно " + Math.round (100*timeZaprosFinishSumm/ countSendMessageUser)/100 +"мс\n" +
                    "Количество совершенных циклов работы бота равно " + countSendMessageUser + "\n";
            if (countUsedBD!=0)
                statistic=statistic+"Количество обращений к основной таблицы БД равно " + countUsedBD+"\n";
            if (countUsedBigBD!=0)
                statistic=statistic+"Количество обращений к большой таблице БД равно " + countUsedBigBD+"\n";
            textLog.setText(statistic);
        }else
            textLog.setText("Сначала включи бота");
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




    private int o=0;
    private long[] usedBytes = new long[100];

    public void test()  {
        usedBytes[0]=0;
        o++;
        usedBytes[o]=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println(usedBytes[o]-usedBytes[o-1]);
    }
    }








