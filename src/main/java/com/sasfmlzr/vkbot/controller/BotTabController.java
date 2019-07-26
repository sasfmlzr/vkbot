package com.sasfmlzr.vkbot.controller;

import com.api.client.Client;
import com.newapi.apivk.architecture.db.DatabaseConnection;
import com.newapi.utils.ParseMessageToDB;
import com.sasfmlzr.vkbot.BotTabPresenter;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class BotTabController extends AnchorPane implements Initializable {


    private final static String fxmlPath = "/com/sasfmlzr/vkbot/views/BotTab.fxml";

    @FXML
    private FlowPane botCardPane;
    @FXML
    private ListView<String> vkdialog;
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
    private ImageView imageTest;

    private BotTabPresenter botTabPresenter = new BotTabPresenter();

    private static String[] lfName = new String[30];                            // массив строк из листа - имя и фамилия
    private static int[] userIDmassive = new int[30];                           // массив userID

    BotTabController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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
        botCardPane.getChildren().addAll(new BotCardController());
    }

    //-----------------при нажатии на диалог---------------------------------------//   // test
    public void dialog() {
        System.out.println("UserID = " + "\n");
    }

    //-----------------ручная отправка сообщения-----------------------------------//
    public void pushButton() throws SQLException, ClassNotFoundException {
        //     if (!Objects.equals(textMessage.getText(), ""))
        //        StaticModel.INSTANCE.getUserBot().botApiClient().messages().vkSendMessageUser(
        //                StaticModel.INSTANCE.getUserBot().getActor(), textMessage.getText(), Integer.parseInt(users_id.getText()));


        // UserActor actor = StaticModel.INSTANCE.getUserBot().getActor();

        DatabaseConnection.Companion.getInstance().connect();

        UserActor actor = new UserActor(30562433, "");

        ParseMessageToDB parseMessageToDB = new ParseMessageToDB(DatabaseConnection.Companion.getInstance().databaseRequest);

        parseMessageToDB.parseMessageToDB(actor, 30562433);
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
                .userIds(summUserID.toString())
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
    //-----------------запуск бота по кнопке включения бота------------------------//

    @FXML
    public void logFill() {
        textLog.setText(botTabPresenter.fillLog());
    }
}








