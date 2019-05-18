package com.sasfmlzr.vkbot.controller.menuprogram;


import com.api.client.Client;
import com.sasfmlzr.apivk.object.BotDatabase_IdRequestResponse;
import com.sasfmlzr.vkbot.StaticModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class DataBaseWindowController implements Initializable {

    @FXML
    private TableView<BotDatabase_IdRequestResponse> tableTextBot;
    @FXML
    private TableColumn<BotDatabase_IdRequestResponse, String> idColumn;
    @FXML
    private TableColumn<BotDatabase_IdRequestResponse, String> sendTextMessage;
    @FXML
    private TableColumn<BotDatabase_IdRequestResponse, String> requestTextMessage;
    @FXML
    private TextField zapros;
    @FXML
    private TextField otvet;
    @FXML
    private Button close;
    @FXML
    private Label textTable;
    public final static String resourcePath = "com.sasfmlzr.vkbot.resourcebundle.DataBaseWindow.messages";
    public final static String fxmlPath = "DataBaseWindow.fxml";


    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initWindow() throws SQLException {

        if (StaticModel.userBot.botApiClient().getStateBot().getBotWork()) {
            textTable.setText("БД:" + "\n");
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
            refreshTable();
        } else {
            textTable.setText("Сначала запусти бота" + "\n");
        }
    }


    public void start(Stage primaryStage) {

    }


    private static ResourceBundle loadLocale(Locale locale, String resourcePath) {
        Locale.setDefault(locale);
        return ResourceBundle.getBundle(resourcePath, Locale.getDefault());
    }


    // при нажатии на обновить таблицу
    public void refreshTable() throws SQLException {
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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sendTextMessage.setCellValueFactory(new PropertyValueFactory<>("response"));
        requestTextMessage.setCellValueFactory(new PropertyValueFactory<>("request"));

        tableTextBot.setItems(StaticModel.userBot.botApiClient().Companion.getDatabase().getBotData());
        StaticModel.userBot.botApiClient().Companion.getDatabase().InitDB();


    }

    /**
     * починить баг дублирования элементов
     */

    public void closeView() {

        //CloseDB();

        //primaryStage.close();
        //DataBaseWindowController.setOnCloseRequest(new EventHandler())


        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();

    }


    public void addElement() throws SQLException {
        if ((!Objects.equals(zapros.getText(), "")) && (!Objects.equals(otvet.getText(), ""))) {
            int idBot;
            if (Client.actor == null) {
                idBot = StaticModel.userBot.getActor().getId();
            } else
                idBot = Client.actor.getId();
            StaticModel.userBot.botApiClient().Companion.getDatabase().getStatmt().execute("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot', 'Login') VALUES ('" + zapros.getText() + "', '" + otvet.getText() + "',  '" + idBot + "');");
            System.out.println("Таблица заполнена");
            refreshTable();
        } else {
            System.out.println("Заполните ячейки");
        }
    }   // добавить новый элемент в таблицу






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
}
