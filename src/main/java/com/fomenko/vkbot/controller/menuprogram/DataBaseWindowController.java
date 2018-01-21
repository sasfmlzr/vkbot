package com.fomenko.vkbot.controller.menuprogram;


import com.api.client.Client;
import com.apiVKmanual.object.BotDatabase_IdRequest;
import com.apiVKmanual.object.BotDatabase_IdRequestResponse;
import com.apiVKmanual.object.BotDatabase_RequestResponse;
import com.apiVKmanual.object.UserIdRightsBD;
import com.fomenko.vkbot.controller.BotTabController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.apiVKmanual.functions.botdatabase.DatabaseRequest.CreateDB;
import static com.apiVKmanual.functions.botdatabase.DatabaseRequest.CreateSecondaryDB;
import static com.apiVKmanual.functions.botdatabase.DatabaseRequest.InsertIntoTablePrimary;
import static com.fomenko.vkbot.controller.BotTabController.botWork;


public class DataBaseWindowController implements Initializable
{
    private static Connection conn;          //SQL соединение
    public static Statement statmt;



    public static ObservableList<BotDatabase_IdRequestResponse> botData = FXCollections.observableArrayList();
    public static ObservableList<BotDatabase_IdRequest> botRandomData = FXCollections.observableArrayList();
    public static ObservableList<BotDatabase_IdRequest> StihMessagesData = FXCollections.observableArrayList();
    public static ObservableList<BotDatabase_IdRequest> AnekdotMessagesData = FXCollections.observableArrayList();
    public static ObservableList<BotDatabase_IdRequest> AforismMessagesData = FXCollections.observableArrayList();
    public static ObservableList<BotDatabase_IdRequest> StatusMessagesData = FXCollections.observableArrayList();
    public static ObservableList<BotDatabase_RequestResponse> BigMessagesData = FXCollections.observableArrayList();
    private static ObservableList<UserIdRightsBD> userRightsData = FXCollections.observableArrayList();
   // static ObservableList<BotDatabase_RequestResponse> BigMessagesData = FXCollections.observableArrayList();
    @FXML
    private TableView<BotDatabase_IdRequestResponse> tableTextBot;
    @FXML
    private TableColumn<BotDatabase_IdRequestResponse, String> idColumn;
    @FXML
    private TableColumn<BotDatabase_IdRequestResponse, String>  sendTextMessage;
    @FXML
    private TableColumn<BotDatabase_IdRequestResponse, String>  requestTextMessage;
    @FXML
    private TextField zapros;
    @FXML
    private TextField otvet;
    @FXML
    private Button close;
    @FXML
    private Label textTable;
    public final static String resourcePath = "com.fomenko.vkbot.resourcebundle.DataBaseWindow.messages";
    public final static String fxmlPath = "DataBaseWindow.fxml";



    public void initialize(URL location, ResourceBundle resources)    {
    }

    public void initWindow() throws SQLException {


        if (botWork) {
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
        }
        else
        {
            textTable.setText("Сначала запусти бота" + "\n");
        }
    }


    public void start(Stage primaryStage)    {

    }


    private static ResourceBundle loadLocale(Locale locale, String resourcePath)    {
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

      //  connectDatabase();

     //   InitDB();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sendTextMessage.setCellValueFactory(new PropertyValueFactory<>("response"));
        requestTextMessage.setCellValueFactory(new PropertyValueFactory<>("request"));

        tableTextBot.setItems(botData);
        InitDB();




    }

    /**
     *
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
            if ((!Objects.equals(zapros.getText(), "")) &&   (!Objects.equals(otvet.getText(), "")))
        {
            int idBot;
            if (Client.actor==null){
                idBot= BotTabController.actor.getId();
            }else
                idBot=Client.actor.getId();

            statmt.execute("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot', 'Login') VALUES ('"+zapros.getText()+"', '"+ otvet.getText()+ "',  '"+idBot+"');");
            System.out.println("Таблица заполнена");
            refreshTable();
        }
        else  {
            System.out.println("Заполните ячейки");
        }
    }   // добавить новый элемент в таблицу


    public static void addElementinDialog(String request, String response) throws SQLException {
        System.out.println(request);
        System.out.println(response);
        int idBot;
        if (Client.actor==null){
            idBot=BotTabController.actor.getId();
        }else
            idBot=Client.actor.getId();

        statmt.execute("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot', 'Login')  VALUES  ('"+request+"', '"+response+"', "+idBot+"); ");
       // statmt.execute("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot', 'Login') VALUES ('"+request+"', '"+response+"',  '"+ids+"');");
            System.out.println("Успешно занесено в БД");
            InitDB();
            //  System.out.println("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot') VALUES ("+zapros.getText()+", "+ otvet.getText()+ ")");
    }   // добавить новый элемент в таблицу




    // --------Инициализация моей БД и заполнение object--------
    public static void InitDB() throws SQLException
    {
        botData.clear();
        botRandomData.clear();
        StihMessagesData.clear();
        AforismMessagesData.clear();
        AnekdotMessagesData.clear();
        StatusMessagesData.clear();
        BigMessagesData.clear();



        CreateDB();
        CreateSecondaryDB();
        InsertIntoTablePrimary();
        ResultSet resSet;
        resSet = statmt.executeQuery("SELECT * FROM BotMessages");
        while(resSet.next())
        {
            String  requesttextbot = resSet.getString("requesttextbot");
            int id =  Integer.parseInt(resSet.getString("id"));
            String  responseTextBot = resSet.getString("responsetextbot");
            botData.add(new BotDatabase_IdRequestResponse(id,requesttextbot,responseTextBot));

            //   idColumn.getColumns().add(id);
            //    sendTextMessage.getColumns().add(responseTextBot);
            //    requestTextMessage.getColumns().add(requestTextBot);
        }

        resSet = statmt.executeQuery("SELECT * FROM NorkinForewer");
        while(resSet.next())
        {
            String  requesttextbot = resSet.getString("requesttextbot");
            int id =  Integer.parseInt(resSet.getString("id"));
            String  responseTextBot = resSet.getString("responsetextbot");
            botData.add(new BotDatabase_IdRequestResponse(id,requesttextbot,responseTextBot));
        }

        InitOneDB_Id_Request("RandomMessages", botRandomData);

        InitOneDB_Id_Request("StihMessages", StihMessagesData);

        InitOneDB_Id_Request("AforismMessages", AforismMessagesData);

        InitOneDB_Id_Request("AnekdotMessages", AnekdotMessagesData);

        InitOneDB_Id_Request("StatusMessages", StatusMessagesData);

        InitOneDB_Request_Response("RandomBazaBot", BigMessagesData);

        InitOneDB_userRights(userRightsData);
        System.out.println( "БД проинициализировались"  );
    }



    // инициализация одной таблицы ID REQUEST
    private static void InitOneDB_Id_Request(String tableDB, ObservableList<BotDatabase_IdRequest> objectData) throws SQLException{
        ResultSet resSet;
        resSet = statmt.executeQuery("SELECT * FROM "+ tableDB);
        while(resSet.next())
        {
            String  requesttextbot = resSet.getString("request");
            int id =  Integer.parseInt(resSet.getString("id"));
            objectData.add(new BotDatabase_IdRequest(id,requesttextbot));
        }
        resSet.close();
    }


    // инициализация одной таблицы  REQUEST RESPONSE
    private static void InitOneDB_Request_Response(String tableDB, ObservableList<BotDatabase_RequestResponse> objectData) throws SQLException{
        ResultSet resSet;
        resSet = statmt.executeQuery("SELECT * FROM "+ tableDB);
        while(resSet.next())
        {
            String  requesttextbot = resSet.getString("requesttextbot");
            String  responsetextbot = resSet.getString("responsetextbot");

            /*  System.out.println( "ID = " + id );
            System.out.println( "request = " + requesttextbot );
            System.out.println();*/

            objectData.add(new BotDatabase_RequestResponse(requesttextbot, responsetextbot));

        }
        resSet.close();
    }

    // инициализация одной таблицы пользовательских прав
    private static void InitOneDB_userRights(ObservableList<UserIdRightsBD> objectData) throws SQLException{

        ResultSet resSet;
        resSet = statmt.executeQuery("SELECT * FROM UserRights");
        while(resSet.next())
        {
            String  loginBot = resSet.getString("login");
            int  userID = resSet.getInt("userID");
            String  nameRight = resSet.getString("nameRight");
            /*  System.out.println( "ID = " + id );
            System.out.println( "request = " + requesttextbot );
            System.out.println();*/
            objectData.add(new UserIdRightsBD(loginBot, userID, nameRight));
        }
        resSet.close();
    }



    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void connectDatabase() throws ClassNotFoundException, SQLException
    {
        conn = null;
         Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:Database.db");

        System.out.println("База Подключена!");
        statmt = conn.createStatement();
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
    // --------Закрытие--------
    public static void CloseDB() {

        try { conn.close(); } catch(SQLException se) { /*can't do anything */ }
        try { statmt.close(); } catch(SQLException se) { /*can't do anything */ }
      //  try { resSet.close(); } catch(SQLException se) { /*can't do anything */ }

        BotTabController.databaseLoaded=true;
        /*

        conn.close();
        statmt.close();
        resSet.close();/*/

        System.out.println("Соединения закрыты");
    }





}



