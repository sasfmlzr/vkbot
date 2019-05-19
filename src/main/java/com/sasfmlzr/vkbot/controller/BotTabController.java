package com.sasfmlzr.vkbot.controller;

import com.api.client.Client;
import com.sasfmlzr.apivk.object.StatisticsVariable;
import com.sasfmlzr.vkbot.StaticModel;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.account.UserSettings;
import com.vk.api.sdk.objects.messages.Dialog;
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
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
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
    public void pushButton() throws ClientException, ApiException {
        if (!Objects.equals(textMessage.getText(), ""))
            StaticModel.INSTANCE.getUserBot().botApiClient().messages().vkSendMessageUser(
                    StaticModel.INSTANCE.getUserBot().getActor(), textMessage.getText(), Integer.parseInt(users_id.getText()));
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

        String token = Client.Companion.getToken();
        UserActor actor = new UserActor(Client.Companion.getIdBot(), token);
////////////////////////////////
        // создаем объект user id
        List<Dialog> dialogList = StaticModel.INSTANCE.getUserBot().getVk().messages().getDialogs(actor)            // записываем в лист результат работы запроса
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








