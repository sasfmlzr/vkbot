package com.database

import com.newapi.interfaces.Database
import com.sasfmlzr.apivk.State.databaseLoaded
import com.sasfmlzr.apivk.`object`.BotDatabase_IdRequest
import com.sasfmlzr.apivk.`object`.BotDatabase_IdRequestResponse
import com.sasfmlzr.apivk.`object`.BotDatabase_RequestResponse
import com.sasfmlzr.apivk.`object`.UserIdRightsBD
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.sql.*

open class DatabaseImpl : Database {
    var conn: Connection? = null          //SQL connection
    var statmt: Statement? = null

    var botData = FXCollections.observableArrayList<BotDatabase_IdRequestResponse>()!!
    var botRandomData = FXCollections.observableArrayList<BotDatabase_IdRequest>()!!
    var stihMessagesData = FXCollections.observableArrayList<BotDatabase_IdRequest>()!!
    var anekdotMessagesData = FXCollections.observableArrayList<BotDatabase_IdRequest>()!!
    var aforismMessagesData = FXCollections.observableArrayList<BotDatabase_IdRequest>()!!
    var statusMessagesData = FXCollections.observableArrayList<BotDatabase_IdRequest>()!!
    var bigMessagesData = FXCollections.observableArrayList<BotDatabase_RequestResponse>()!!
    var userRightsData = FXCollections.observableArrayList<UserIdRightsBD>()!!

    val databaseRequest: DatabaseRequestImpl by lazy {
        DatabaseRequestImpl(statmt!!, conn!!)
    }

    // --------CONNECTION TO DATABASE--------
    @Throws(ClassNotFoundException::class, SQLException::class)
    fun connectDatabase() {
        conn = null
        Class.forName("org.sqlite.JDBC")
        conn = DriverManager.getConnection("jdbc:sqlite:Database.db")

        statmt = conn!!.createStatement()
        println("DatabaseImpl connection!")
    }

    // --------CLOSE DATABASE--------
    fun CloseDB() {
        try {
            conn!!.close()
        } catch (se: SQLException) { /*can't do anything */
        }

        try {
            statmt!!.close()
        } catch (se: SQLException) { /*can't do anything */
        }

        //  try { resSet.close(); } catch(SQLException se) { /*can't do anything */ }
        databaseLoaded = true
        println("?????????? ???????")
    }

    // initialize table ID REQUEST
    @Throws(SQLException::class)
    private fun InitOneDB_Id_Request(tableDB: String, objectData: ObservableList<BotDatabase_IdRequest>) {
        val resSet: ResultSet = statmt!!.executeQuery("SELECT * FROM $tableDB")
        while (resSet.next()) {
            val requesttextbot = resSet.getString("request")
            val id = Integer.parseInt(resSet.getString("id"))
            objectData.add(BotDatabase_IdRequest(id, requesttextbot))
        }
        resSet.close()
    }

    // initialize table   REQUEST RESPONSE
    @Throws(SQLException::class)
    private fun InitOneDB_Request_Response(tableDB: String, objectData: ObservableList<BotDatabase_RequestResponse>) {
        val resSet: ResultSet = statmt!!.executeQuery("SELECT * FROM $tableDB")
        while (resSet.next()) {
            val requesttextbot = resSet.getString("requesttextbot")
            val responsetextbot = resSet.getString("responsetextbot")

            /*  System.out.println( "ID = " + id );
            System.out.println( "request = " + requesttextbot );
            System.out.println();*/

            objectData.add(BotDatabase_RequestResponse(requesttextbot, responsetextbot))

        }
        resSet.close()
    }

    // initialize table InitOneDB_userRights
    @Throws(SQLException::class)
    private fun InitOneDB_userRights(objectData: ObservableList<UserIdRightsBD>) {

        val resSet: ResultSet = statmt!!.executeQuery("SELECT * FROM UserRights")
        while (resSet.next()) {
            val loginBot = resSet.getString("login")
            val userID = resSet.getInt("userID")
            val nameRight = resSet.getString("nameRight")
            /*  System.out.println( "ID = " + id );
            System.out.println( "request = " + requesttextbot );
            System.out.println();*/
            objectData.add(UserIdRightsBD(loginBot, userID, nameRight))
        }
        resSet.close()
    }

    // --------initialize database and put in object--------
    @Throws(SQLException::class)
    fun InitDB() {
        botData.clear()
        botRandomData.clear()
        stihMessagesData.clear()
        aforismMessagesData.clear()
        anekdotMessagesData.clear()
        statusMessagesData.clear()
        bigMessagesData.clear()

        databaseRequest.createDB()
        databaseRequest.createSecondaryDB()
        databaseRequest.insertIntoTablePrimary()
        databaseRequest.createRepostDB()
        var resSet: ResultSet = statmt!!.executeQuery("SELECT * FROM BotMessages")
        addData(resSet)
        resSet = statmt!!.executeQuery("SELECT * FROM NorkinForewer")
        addData(resSet)

        InitOneDB_Id_Request("RandomMessages", botRandomData)
        InitOneDB_Id_Request("StihMessages", stihMessagesData)
        InitOneDB_Id_Request("AforismMessages", aforismMessagesData)
        InitOneDB_Id_Request("AnekdotMessages", anekdotMessagesData)
        InitOneDB_Id_Request("StatusMessages", statusMessagesData)
        InitOneDB_Request_Response("RandomBazaBot", bigMessagesData)
        InitOneDB_userRights(userRightsData)
        println("БД проинициализировалась")
    }

    @Throws(SQLException::class)
    private fun addData(resSet: ResultSet) {
        while (resSet.next()) {
            val requesttextbot = resSet.getString("requesttextbot")
            val id = Integer.parseInt(resSet.getString("id"))
            val responseTextBot = resSet.getString("responsetextbot")
            botData.add(BotDatabase_IdRequestResponse(id, requesttextbot, responseTextBot))
        }
    }
}
