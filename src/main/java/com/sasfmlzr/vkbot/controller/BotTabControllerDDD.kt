package com.sasfmlzr.vkbot.controller

import com.api.client.Client
import com.database.DatabaseEntity
import com.newapi.utils.ParseMessageToDB
import com.sasfmlzr.vkbot.BotTabPresenter
import com.sasfmlzr.vkbot.StaticModel
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.Message
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.FlowPane
import java.io.IOException
import java.net.URL
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class BotTabControllerDDD internal constructor() : AnchorPane(), Initializable {

    @FXML
    private lateinit var botCardPane: FlowPane
    @FXML
    private lateinit var vkdialog: ListView<Any>
    @FXML
    private lateinit var textMessage: TextField
    @FXML
    private lateinit var users_id: TextField
    @FXML
    private lateinit var textMessages: TextArea
    @FXML
    private lateinit var textLog: TextArea
    @FXML
    private lateinit var botLog: Button
    @FXML
    private lateinit var imageTest: ImageView

    private var botTabPresenter = BotTabPresenter()

    init {
        val loader = FXMLLoader(javaClass.getResource(fxmlPath))
        loader.setRoot(this)
        loader.setController(this)
        try {
            loader.load<Any>()
        } catch (ex: IOException) {
            Logger.getLogger(BotTabControllerDDD::class.java.name).log(Level.SEVERE, null, ex)
        }

    }

    //-----------------инициалихация-----------------------------------------------//
    override fun initialize(location: URL, resources: ResourceBundle) {
        // childList = new ArrayList<>();
        // childList.add(new BotCardController());
        //botCardPane.getChildren().addAll( childList);
        botCardPane.children.addAll(BotCardController())

    }

    //-----------------при нажатии на диалог---------------------------------------//   // test
    fun dialog() {
        println("UserID = " + "\n")
    }

    //-----------------ручная отправка сообщения-----------------------------------//
    @Throws(SQLException::class, ClassNotFoundException::class)
    fun pushButton() {
        //     if (!Objects.equals(textMessage.getText(), ""))
        //        StaticModel.INSTANCE.getUserBot().botApiClient().messages().vkSendMessageUser(
        //                StaticModel.INSTANCE.getUserBot().getActor(), textMessage.getText(), Integer.parseInt(users_id.getText()));


        // UserActor actor = StaticModel.INSTANCE.getUserBot().getActor();

        DatabaseEntity.database.connectDatabase()            //подключение бд
        DatabaseEntity.database.InitDB()          //инициализация таблиц бд в объект


        val actor = UserActor(30562433, "")

        val parseMessageToDB = ParseMessageToDB(DatabaseEntity.database.databaseRequest)

        parseMessageToDB.parseMessageToDB(actor, 30562433)
    }

    //-----------------обновление сообщений----------------------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun requestMessage() {


        val token = Client.token
        val actor = UserActor(Client.idBot, token)

        val infoAccount = StaticModel.userBot.vk.account().getProfileInfo(actor)
                .execute()
        val firstNameAccount = infoAccount.firstName                   //имя и фамилия клинта приложения
        val lastNameNameAccount = infoAccount.lastName
        val useridmessage = userIDmassive[vkdialog.selectionModel.selectedIndex]

        textMessages.text = ""                   //имя и фамилия клинта приложения

        val messageList = StaticModel.userBot.vk.messages().getHistory(actor)
                .count(40)
                .userId(useridmessage)
                .execute().items

        val textMessage = arrayOfNulls<String>(messageList.size)

        var countMessage: Int                                        // счетчик диалогов
        countMessage = 0
        while (countMessage <= messageList.size - 1) {
            textMessage[countMessage] = (messageList[countMessage] as Message).text     // массив сообщений
            if ((messageList[countMessage] as Message).peerId == (messageList[countMessage] as Message).fromId) {
                textMessages.appendText(vkdialog.selectionModel.selectedItem.toString() + ": " + textMessage[countMessage] + "\n")
            } else {
                textMessages.appendText(firstNameAccount + " " + lastNameNameAccount + ": " + textMessage[countMessage] + "\n")
            }
            countMessage = countMessage + 1
        }

        print("vkdialog  " + vkdialog.selectionModel.selectedIndex + "\n")
        print("vkdindex  " + lfName[vkdialog.selectionModel.selectedIndex] + "\n")
        print("vkdindex  " + userIDmassive[vkdialog.selectionModel.selectedIndex] + "\n")
        //  List listItems  =  vkdialog.getItems();
        //   vkdialog.getSelectionModel().getSelectedItem();
        //  System.out.print("getLongPollHistory = ");
    }

    //-----------------обновить диалоги--------------------------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun refreshDialogs() {
        vkdialog.items.clear()

        val token = Client.token
        val actor = UserActor(Client.idBot, token)
        ////////////////////////////////
        // создаем объект user id
        val dialogList = StaticModel.userBot.vk.messages().getConversations(actor)            // записываем в лист результат работы запроса
                .count(30)
                .execute().items
        var countDialog: Int                                        // счетчик диалогов

        val summUserID = StringBuilder()                                     // список userID через запятую
        countDialog = 0
        while (countDialog <= dialogList.size - 1) {
            //получаем диалог countSendMessageUser
            val userID = dialogList[countDialog].lastMessage.peerId!!        // получаем userID диалога
            userIDmassive[countDialog] = userID
            summUserID.append(userIDmassive[countDialog]).append(",")
            countDialog = countDialog + 1
            ////////////////////////////////получение userID диалога
        }
        ////////////////////////////получение имени и фамилии
        val infoUser = StaticModel.userBot.vk.users().get(actor)                                                                //берем информацию о пользователе
                .userIds(summUserID.toString())
                .execute()
        val lastName = arrayOfNulls<String>(30)                                                                  // массив строк из листа - фамилия
        val firstName = arrayOfNulls<String>(30)                                                                 // массив строк из листа - имя
        countDialog = 0
        while (countDialog <= infoUser.size - 1) {
            lastName[countDialog] = infoUser[countDialog].lastName                  // получаем фамилию
            firstName[countDialog] = infoUser[countDialog].firstName                // получаем имя
            lfName[countDialog] = firstName[countDialog] + " " + lastName[countDialog]                      //строка имени и фамилии
            //     System.out.println("lfName[countDialog] = " + lfName[countDialog]  + "\n");
            userIDmassive[countDialog] = infoUser[countDialog].id!!                   // id пользователя в массиве
            vkdialog.items.add(lfName[countDialog])                                                    // загрузка в лист
            countDialog = countDialog + 1
        }
    }
    //-----------------запуск бота по кнопке включения бота------------------------//

    @FXML
    fun logFill() {
        textLog.text = botTabPresenter.fillLog()
    }

    companion object {
        private val fxmlPath = "/com/sasfmlzr/vkbot/views/BotTab.fxml"


        private val lfName = arrayOfNulls<String>(30)                            // массив строк из листа - имя и фамилия
        private val userIDmassive = IntArray(30)                           // массив userID
    }
}








