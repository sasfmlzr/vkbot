package com.sasfmlzr.vkbot.controller;

import com.api.client.Client;
import com.database.DatabaseEntity;
import com.sasfmlzr.apivk.object.StatisticsVariable;
import com.sasfmlzr.vkbot.StaticModel;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.account.UserSettings;
import com.vk.api.sdk.objects.messages.ConversationWithMessage;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class BotTabController extends AnchorPane implements Initializable {


    //final static String fxmlPath = "/com/sasfmlzr/vkbot/views/BotTab.fxml";

    @FXML
    private FlowPane botCardPane;
    @FXML
    private ListView vkdialog;
    @FXML
    private TextField textMessage;
    @FXML
    private TextField users_id;
    @FXML
    private TextArea textMessages;
    @FXML
    private TextArea textLog;
    @FXML
    private Button botLog;
    @FXML
    private static ImageView imageTest;


    private static String[] lfName = new String[30];                            // массив строк из листа - имя и фамилия
    private static int[] userIDmassive = new int[30];                           // массив userID


    private static List<BotCardController> childList;


    BotTabController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sasfmlzr/vkbot/views/BotTab.fxml"));
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
        // childList = new ArrayList<>();
        // childList.add(new BotCardController());
        //botCardPane.getChildren().addAll( childList);
        botCardPane.getChildren().addAll(new BotCardController());

    }

    //-----------------при нажатии на диалог---------------------------------------//   // test
    public void dialog() {
        System.out.println("UserID = " + "\n");
    }

    //-----------------ручная отправка сообщения-----------------------------------//
    public void pushButton() throws ClientException, ApiException, InterruptedException, SQLException, ClassNotFoundException {
        //     if (!Objects.equals(textMessage.getText(), ""))
        //        StaticModel.INSTANCE.getUserBot().botApiClient().messages().vkSendMessageUser(
        //                StaticModel.INSTANCE.getUserBot().getActor(), textMessage.getText(), Integer.parseInt(users_id.getText()));


        // UserActor actor = StaticModel.INSTANCE.getUserBot().getActor();

        UserActor actor = new UserActor(30562433, "");

    /*    StaticModel.INSTANCE.getUserBot().getVk().messages().getHistory(actor)
                .count(40)
                .userId(useridmessage)
                .execute().getItems();*/

        List<String> listKolyanMessage = new ArrayList<String>();

////////////////////////////////////////////////////////
        System.out.println("Start parsing dialog");


        List<ConversationWithMessage> conversationWithMessages = runDialogs(0, actor);
        int sizeDialogs;
        int countDialogs = 200;
        do {
            List<ConversationWithMessage> tempDialogs = runDialogs(countDialogs, actor);
            conversationWithMessages.addAll(tempDialogs);
            sizeDialogs = tempDialogs.size();
            countDialogs = countDialogs + 200;
            System.out.println("Processing parsing dialog");
        } while (sizeDialogs != 0);
        System.out.println("Parsing dialog completed" + conversationWithMessages.size());
/////////////////////////////////////////
        System.out.println("Start parsing messages");
        for (int i = 0; i <= conversationWithMessages.size() - 1; i++) {
            ConversationWithMessage dialog = conversationWithMessages.get(i);

            int peerId = dialog.getConversation().getPeer().getId();

            Thread.sleep(500);

/////////////////////
            List<Message> messages = runMessages(peerId, 0, actor);
            int countMessages = 200;
            int sizeMessages;
            do {
                List<Message> tempMessages = runMessages(peerId, countMessages, actor);
                messages.addAll(tempMessages);
                sizeMessages = tempMessages.size();
                countMessages = countMessages + 200;
                System.out.println("Processing parsing messages" + messages.size());
                Thread.sleep(400);
            } while (sizeMessages != 0);
//////////////////////
            System.out.println("Finish parsing messages");
            //   LocalDateTime timeMessage = LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());

            Comparator<Message> comparator = new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    LocalDateTime timeo1 = LocalDateTime.ofInstant(Instant.ofEpochSecond(o1.getDate()), ZoneId.systemDefault());
                    LocalDateTime timeo2 = LocalDateTime.ofInstant(Instant.ofEpochSecond(o2.getDate()), ZoneId.systemDefault());
                    return timeo1.compareTo(timeo2);
                }
            };
            Collections.sort(messages, comparator);
            System.out.println("Sorted finished");

            for (int j = 0; j <= messages.size() - 1; j++) {
                int fromID = messages.get(j).getFromId();

                //92330508 если это колян
                //96026192 если это ростик
                if (fromID == 78521993) { // если это колян
                    String textMessage = messages.get(j).getText();
                    LocalDateTime timeMessage = LocalDateTime.ofInstant(Instant.ofEpochSecond(messages.get(j).getDate()),
                            ZoneId.systemDefault());

                    listKolyanMessage.add(textMessage);
                }
            }
            System.out.println("Fing kolyan messages finished");
            System.out.println();
            //     StaticModel.INSTANCE.getUserBot().getVk().messages().
        }

        DatabaseEntity.INSTANCE.getDatabase().connectDatabase();            //подключение бд
        DatabaseEntity.INSTANCE.getDatabase().InitDB();          //инициализация таблиц бд в объект

        for (int k = 0; k <= listKolyanMessage.size() - 1; k++) {
            if (!listKolyanMessage.get(k).equals("")) {
                DatabaseEntity.INSTANCE.getDatabase().getDatabaseRequest().addRandomMessage(listKolyanMessage.get(k));
            }
        }
    }

    public List<ConversationWithMessage> runDialogs(int offset, UserActor actor) throws ClientException, ApiException {
        return StaticModel.INSTANCE.getUserBot().getVk().messages().getConversations(actor)
                .offset(offset)// записываем в лист результат работы запроса
                .count(200)
                .execute().getItems();
    }

    public List<Message> runMessages(int peerId, int offset, UserActor actor) {
        List<Message> messages = new ArrayList<>();
        try {
            messages = StaticModel.INSTANCE.getUserBot().getVk().messages().getHistory(actor)
                    .peerId(peerId)
                    .offset(offset)
                    .count(200)
                    .execute().getItems();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return messages;

    }

    //-----------------обновление сообщений----------------------------------------//
    public void requestMessage() throws ClientException, ApiException {


        String token = Client.Companion.getToken();
        UserActor actor = new UserActor(Client.Companion.getIdBot(), token);

        UserSettings infoAccount = StaticModel.INSTANCE.getUserBot().getVk().account().getProfileInfo(actor)
                .execute();
        String firstNameAccount = infoAccount.getFirstName();                   //имя и фамилия клинта приложения
        String lastNameNameAccount = infoAccount.getLastName();
        int useridmessage = userIDmassive[vkdialog.getSelectionModel().getSelectedIndex()];

        textMessages.setText("");                   //имя и фамилия клинта приложения

        List messageList = StaticModel.INSTANCE.getUserBot().getVk().messages().getHistory(actor)
                .count(40)
                .userId(useridmessage)
                .execute().getItems();


        String[] textMessage = new String[messageList.size()];

        int countMessage;                                        // счетчик диалогов
        for (countMessage = 0; countMessage <= messageList.size() - 1; countMessage = countMessage + 1) {
            textMessage[countMessage] = ((Message) messageList.get(countMessage)).getText();     // массив сообщений
            if (((Message) messageList.get(countMessage)).getPeerId().equals(((Message) messageList.get(countMessage)).getFromId())) {
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

        String token = Client.Companion.getToken();
        UserActor actor = new UserActor(Client.Companion.getIdBot(), token);
////////////////////////////////
        // создаем объект user id
        List<ConversationWithMessage> dialogList = StaticModel.INSTANCE.getUserBot().getVk().messages().getConversations(actor)            // записываем в лист результат работы запроса
                .count(30)
                .execute().getItems();
        int countDialog;                                        // счетчик диалогов

        StringBuilder summUserID = new StringBuilder();                                     // список userID через запятую
        for (countDialog = 0; countDialog <= dialogList.size() - 1; countDialog = countDialog + 1) {
            //получаем диалог countSendMessageUser
            int userID = dialogList.get(countDialog).getLastMessage().getPeerId();        // получаем userID диалога
            userIDmassive[countDialog] = userID;
            summUserID.append(userIDmassive[countDialog]).append(",");
////////////////////////////////получение userID диалога
        }
////////////////////////////получение имени и фамилии
        List<UserXtrCounters> infoUser = StaticModel.INSTANCE.getUserBot().getVk().users().get(actor)                                                                //берем информацию о пользователе
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

    static boolean ava = false;
    //-----------------запуск бота по кнопке включения бота------------------------//


    //-----------------заполнение окна логов-----------------------------------------------//
    @FXML
    public void logFill() {
        if (StaticModel.INSTANCE.getUserBot().botApiClient().getStateBot().getBotWork()) {
            String statistic = "Время, затраченное на последнюю операцию запроса непрочитанных сообщений " + StatisticsVariable.INSTANCE.getTimeZaprosFinishItogo() + "мс\n" +
                    "Время, затраченное на последние остальные операции " + StatisticsVariable.INSTANCE.getTimeItogoMsMinusVK() + "мс\n" +
                    "Подробнее:\n";
            if (StatisticsVariable.INSTANCE.getTimeConsumedMillisBD() != 0)
                statistic = statistic + "Время последней выборки из бд коляна составляет " + StatisticsVariable.INSTANCE.getTimeConsumedMillisBD() + "мс\n";
            if (StatisticsVariable.INSTANCE.getTimeConsumedMillisBigBD() != 0)
                statistic = statistic + "Время последней выборки из большой бд составляет " + StatisticsVariable.INSTANCE.getTimeConsumedMillisBigBD() + "мс\n";
            if (StatisticsVariable.INSTANCE.getTimeItogoSendMessage() != 0)
                statistic = statistic + "Время затраченное на последнюю отправку сообщения составляет " + StatisticsVariable.INSTANCE.getTimeItogoSendMessage() + "мс\n" +
                        "Количество отправленных сообщений равно " + StatisticsVariable.INSTANCE.getCountSendMessage() + "\n";
            long timeProgramFinish = System.currentTimeMillis();
            long timeProgramItog = timeProgramFinish - StatisticsVariable.INSTANCE.getTimeProgramStart();
            statistic = statistic + "Время работы бота равно " + Math.round(timeProgramItog / 1000) + "c\n" +
                    "Среднее время запроса до вк равно " + Math.round(100 * StatisticsVariable.INSTANCE.getTimeZaprosFinishSumm() / StatisticsVariable.INSTANCE.getCountSendMessageUser()) / 100 + "мс\n" +
                    "Количество совершенных циклов работы бота равно " + StatisticsVariable.INSTANCE.getCountSendMessageUser() + "\n";
            if (StatisticsVariable.INSTANCE.getCountUsedBD() != 0)
                statistic = statistic + "Количество обращений к основной таблицы БД равно " + StatisticsVariable.INSTANCE.getCountUsedBD() + "\n";
            if (StatisticsVariable.INSTANCE.getCountUsedBigBD() != 0)
                statistic = statistic + "Количество обращений к большой таблице БД равно " + StatisticsVariable.INSTANCE.getCountUsedBigBD() + "\n";
            textLog.setText(statistic);
        } else
            textLog.setText("Сначала включи бота");
    }

    private int o = 0;
    private long[] usedBytes = new long[100];

    public void test() {
        usedBytes[0] = 0;
        o++;
        usedBytes[o] = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(usedBytes[o] - usedBytes[o - 1]);
    }
}








