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
            /*     try {
                ResourceBundle bundle = loadLocale(Locale.getDefault(), DataBaseWindowController.resourcePath);
                Effects.init();

                AnchorPane root = FXMLLoader.load(getClass().getResource(DataBaseWindowController.fxmlPath), bundle);

                Scene scene = new Scene(root);
                scene.setRoot(root);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//////////////
            //   primaryStage = new Stage();
            //primaryStage.setScene(scene);
            //primaryStage.setMinWidth(1100);
            //primaryStage.setMinHeight(600);
            //primaryStage.setTitle("Test VKBot");
            //primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });//////////
                test();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            refreshTable()
        } else {
            textTable.text = "Сначала запусти бота" + "\n"
        }
    }


    fun start(primaryStage: Stage) {

    }


    // при нажатии на обновить таблицу
    @Throws(SQLException::class)
    fun refreshTable() {
        /*
        System.out.println("UserID = " + "\n");
        for ( int i = 0; i<tableTextBot.getItems().size(); i++) { tableTextBot.getItems().clear();
        }
        Conn();
    //    CreateDB();
     //   WriteDB();
     //   ReadDB();
        InitDB();

      //  tableTextBot.();
        //idColumn.getColumns().set()

     //   tableTextBot.getItems().clear();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sendTextMessage.setCellValueFactory(new PropertyValueFactory<>("login"));
        requestTextMessage.setCellValueFactory(new PropertyValueFactory<>("request"));

        tableTextBot.setItems(botData);     // заполняем таблицу данными
        System.out.println("Таблица выведена");

        */
        /*  for ( int i = 0; i<tableTextBot.getItems().size(); i++) { tableTextBot.getItems().clear();
        }*/
        // tableTextBot.getItems().clear();

        //  connectdatabase;

        //   InitDB();
        idColumn.cellValueFactory = PropertyValueFactory("id")
        sendTextMessage.cellValueFactory = PropertyValueFactory("response")
        requestTextMessage.cellValueFactory = PropertyValueFactory("request")

        //WTF
        tableTextBot.items = FXCollections.observableList(DatabaseStorage.getInstance().botData)

        DatabaseConnection.getInstance().init()
    }

    /**
     * починить баг дублирования элементов
     */

    fun closeView() {

        //CloseDB();

        //primaryStage.close();
        //DataBaseWindowController.setOnCloseRequest(new EventHandler())


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

    companion object {
        val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.DataBaseWindow.messages"
        val fxmlPath = "DataBaseWindow.fxml"


        private fun loadLocale(locale: Locale, resourcePath: String): ResourceBundle {
            Locale.setDefault(locale)
            return ResourceBundle.getBundle(resourcePath, Locale.getDefault())
        }
    }
}

/*
    // --------Заполнение таблицы--------
    public static void WriteDB() throws SQLException {
        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Petya', 125453); ");
        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Vasya', 321789); ");
        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Masha', 456123); ");

        System.out.println("Таблица заполнена");
    }
    /*
    // -------- Вывод таблицы--------
    public static void ReadDB() throws ClassNotFoundException, SQLException {
        resSet = statmt.executeQuery("SELECT * FROM users");

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String  name = resSet.getString("name");
            String  phone = resSet.getString("phone");
        }

        System.out.println("Таблица выведена");
    }

    */