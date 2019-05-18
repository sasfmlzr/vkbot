package com.sasfmlzr.apivk.functions.botdatabase;

import com.sasfmlzr.apiVK.functions.botdatabase.DatabaseRequest;
import com.sasfmlzr.apiVK.object.BotDatabase_IdRequest;
import com.sasfmlzr.apiVK.object.BotDatabase_IdRequestResponse;
import com.sasfmlzr.apiVK.object.BotDatabase_RequestResponse;
import com.sasfmlzr.apiVK.object.UserIdRightsBD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import static com.sasfmlzr.apiVK.State.databaseLoaded;
public class Database {
    private Connection conn;          //SQL connection
    private Statement statmt;

    private  ObservableList<BotDatabase_IdRequestResponse> botData = FXCollections.observableArrayList();
    private ObservableList<BotDatabase_IdRequest> botRandomData = FXCollections.observableArrayList();
    private  ObservableList<BotDatabase_IdRequest> stihMessagesData = FXCollections.observableArrayList();
    private  ObservableList<BotDatabase_IdRequest> anekdotMessagesData = FXCollections.observableArrayList();
    private  ObservableList<BotDatabase_IdRequest> aforismMessagesData = FXCollections.observableArrayList();
    private  ObservableList<BotDatabase_IdRequest> statusMessagesData = FXCollections.observableArrayList();
    private  ObservableList<BotDatabase_RequestResponse> bigMessagesData = FXCollections.observableArrayList();
    private  ObservableList<UserIdRightsBD> userRightsData = FXCollections.observableArrayList();




    public DatabaseRequest databaseRequest(Statement statmt){
        return new DatabaseRequest(this.statmt);
    }



    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Statement getStatmt() {
        return statmt;
    }

    public void setStatmt(Statement statmt) {
        this.statmt = statmt;
    }

    public Database(){

    }



    // --------CONNECTION TO DATABASE--------
    public void connectDatabase() throws ClassNotFoundException, SQLException    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        setConn(DriverManager.getConnection("jdbc:sqlite:Database.db"));

        setStatmt(getConn().createStatement());
        System.out.println("Database connection!");
    }
    // --------CLOSE DATABASE--------
    public void CloseDB() {
        try { conn.close(); } catch(SQLException se) { /*can't do anything */ }
        try { statmt.close(); } catch(SQLException se) { /*can't do anything */ }
        //  try { resSet.close(); } catch(SQLException se) { /*can't do anything */ }
        databaseLoaded=true;
        System.out.println("?????????? ???????");
    }
    // initialize table ID REQUEST
    private void InitOneDB_Id_Request(String tableDB, ObservableList<BotDatabase_IdRequest> objectData) throws SQLException{
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
    // initialize table   REQUEST RESPONSE
    private void InitOneDB_Request_Response(String tableDB, ObservableList<BotDatabase_RequestResponse> objectData) throws SQLException{
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
    // initialize table InitOneDB_userRights
    private void InitOneDB_userRights(ObservableList<UserIdRightsBD> objectData) throws SQLException{

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

    // --------initialize database and put in object--------
    public void InitDB() throws SQLException
    {
        botData.clear();
        botRandomData.clear();
        stihMessagesData.clear();
        aforismMessagesData.clear();
        anekdotMessagesData.clear();
        statusMessagesData.clear();
        bigMessagesData.clear();

        databaseRequest(statmt).CreateDB();
        databaseRequest(statmt).CreateSecondaryDB();
        databaseRequest(statmt).InsertIntoTablePrimary();
        ResultSet resSet;
        resSet = statmt.executeQuery("SELECT * FROM BotMessages");
        addData(resSet);
        resSet = statmt.executeQuery("SELECT * FROM NorkinForewer");
        addData(resSet);

        InitOneDB_Id_Request("RandomMessages", botRandomData);
        InitOneDB_Id_Request("StihMessages", stihMessagesData);
        InitOneDB_Id_Request("AforismMessages", aforismMessagesData);
        InitOneDB_Id_Request("AnekdotMessages", anekdotMessagesData);
        InitOneDB_Id_Request("StatusMessages", statusMessagesData);
        InitOneDB_Request_Response("RandomBazaBot", bigMessagesData);
        InitOneDB_userRights(userRightsData);
        System.out.println( "БД проинициализировалась"  );
    }
    private  void addData(ResultSet resSet) throws SQLException {
        while(resSet.next())
        {
            String  requesttextbot = resSet.getString("requesttextbot");
            int id =  Integer.parseInt(resSet.getString("id"));
            String  responseTextBot = resSet.getString("responsetextbot");
            botData.add(new BotDatabase_IdRequestResponse(id,requesttextbot,responseTextBot));
        }
    }


    public ObservableList<BotDatabase_IdRequestResponse> getBotData() {
        return botData;
    }

    public void setBotData(ObservableList<BotDatabase_IdRequestResponse> botData) {
        this.botData = botData;
    }

    public ObservableList<BotDatabase_IdRequest> getBotRandomData() {
        return botRandomData;
    }

    public void setBotRandomData(ObservableList<BotDatabase_IdRequest> botRandomData) {
        this.botRandomData = botRandomData;
    }

    public ObservableList<BotDatabase_IdRequest> getStihMessagesData() {
        return stihMessagesData;
    }

    public void setStihMessagesData(ObservableList<BotDatabase_IdRequest> stihMessagesData) {
        this.stihMessagesData = stihMessagesData;
    }

    public ObservableList<BotDatabase_IdRequest> getAnekdotMessagesData() {
        return anekdotMessagesData;
    }

    public void setAnekdotMessagesData(ObservableList<BotDatabase_IdRequest> anekdotMessagesData) {
        this.anekdotMessagesData = anekdotMessagesData;
    }

    public ObservableList<BotDatabase_IdRequest> getAforismMessagesData() {
        return aforismMessagesData;
    }

    public void setAforismMessagesData(ObservableList<BotDatabase_IdRequest> aforismMessagesData) {
        this.aforismMessagesData = aforismMessagesData;
    }

    public ObservableList<BotDatabase_IdRequest> getStatusMessagesData() {
        return statusMessagesData;
    }

    public void setStatusMessagesData(ObservableList<BotDatabase_IdRequest> statusMessagesData) {
        this.statusMessagesData = statusMessagesData;
    }

    public ObservableList<BotDatabase_RequestResponse> getBigMessagesData() {
        return bigMessagesData;
    }

    public void setBigMessagesData(ObservableList<BotDatabase_RequestResponse> bigMessagesData) {
        this.bigMessagesData = bigMessagesData;
    }

    public ObservableList<UserIdRightsBD> getUserRightsData() {
        return userRightsData;
    }

    public void setUserRightsData(ObservableList<UserIdRightsBD> userRightsData) {
        this.userRightsData = userRightsData;
    }
}
