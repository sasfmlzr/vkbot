package com.sasfmlzr.vkbot.controller.menuprogram


import com.api.client.Client
import com.newapi.apivk.architecture.db.DatabaseConnection
import com.newapi.apivk.architecture.storage.DatabaseStorage
import com.sasfmlzr.apivk.`object`.BotDatabase_IdRequestResponse
import com.sasfmlzr.vkbot.StaticModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Stage
import java.net.URL
import java.sql.SQLException
import java.util.*

class DataBaseWindowController : Initializable {

    companion object {
        const val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.DataBaseWindow.messages"
        const val fxmlPath = "DataBaseWindow.fxml"

        private fun loadLocale(locale: Locale, resourcePath: String): ResourceBundle {
            Locale.setDefault(locale)
            return ResourceBundle.getBundle(resourcePath, Locale.getDefault())
        }
    }

    @FXML
    private lateinit var tableTextBot: TableView<BotDatabase_IdRequestResponse>
    @FXML
    private lateinit var idColumn: TableColumn<BotDatabase_IdRequestResponse, String>
    @FXML
    private lateinit var sendTextMessage: TableColumn<BotDatabase_IdRequestResponse, String>
    @FXML
    private lateinit var requestTextMessage: TableColumn<BotDatabase_IdRequestResponse, String>
    @FXML
    private lateinit var zapros: TextField
    @FXML
    private lateinit var otvet: TextField
    @FXML
    private lateinit var close: Button
    @FXML
    private lateinit var textTable: Label

    override fun initialize(location: URL, resources: ResourceBundle) {}

    @Throws(SQLException::class)
    fun initWindow() {

        if (StaticModel.userBot.botApiClient().stateBot.botWork) {
            textTable.text = "БД:" + "\n"

            refreshTable()
        } else {
            textTable.text = "Сначала запусти бота" + "\n"
        }
    }

    // при нажатии на обновить таблицу
    @Throws(SQLException::class)
    fun refreshTable() {
        idColumn.cellValueFactory = PropertyValueFactory("id")
        sendTextMessage.cellValueFactory = PropertyValueFactory("response")
        requestTextMessage.cellValueFactory = PropertyValueFactory("request")

        DatabaseConnection.getInstance().connect()

        //WTF
        tableTextBot.items = FXCollections.observableList(DatabaseStorage.getInstance().botData)
    }

    /**
     * починить баг дублирования элементов
     */

    fun closeView() {
        val stage = close.scene.window as Stage
        stage.close()
    }


    @Throws(SQLException::class)
    fun addElement() {
        if (zapros.text != "" && otvet.text != "") {
            val idBot: Int
            if (Client.actor == null) {
                idBot = StaticModel.userBot.actor.id!!
            } else
                idBot = Client.actor.id!!

            //WTF
            DatabaseConnection.getInstance().statmt.execute("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot', 'Login') VALUES ('" + zapros.text + "', '" + otvet.text + "',  '" + idBot + "');")
            println("Таблица заполнена")
            refreshTable()
        } else {
            println("Заполните ячейки")
        }
    }   // добавить новый элемент в таблицу
}
