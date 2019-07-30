package com.newapi.apivk.architecture.db

import com.newapi.apivk.architecture.storage.DatabaseStorage
import com.sasfmlzr.apivk.`object`.BotDatabase_IdRequest
import com.sasfmlzr.apivk.`object`.BotDatabase_IdRequestResponse
import com.sasfmlzr.apivk.`object`.BotDatabase_RequestResponse
import com.sasfmlzr.apivk.`object`.UserIdRightsBD
import java.sql.*

class DatabaseConnection {

    lateinit var conn: Connection
    lateinit var statmt: Statement

    private lateinit var databaseStorage: DatabaseStorage
    lateinit var databaseRequest: DatabaseRequest

    companion object {
        @Volatile
        private var instance: DatabaseConnection? = null

        fun getInstance(): DatabaseConnection {
            return instance ?: synchronized(this) {
                instance ?: DatabaseConnection().also { instance = it }
            }
        }
    }

    fun connect() {
        conn = DriverManager.getConnection("jdbc:sqlite:Database.db")

        statmt = conn.createStatement()

        databaseStorage = DatabaseStorage.getInstance()
        databaseRequest = DatabaseRequest.getInstance(conn, statmt)
        println("JDBC connected")
        init()
    }

    fun init() {
        databaseStorage.clear()

        databaseRequest.createDB()
        databaseRequest.createSecondaryDB()


        fillData(statmt.executeQuery("SELECT * FROM BotMessages"))

        initOneDB_Id_Request("RandomMessages", databaseStorage.botRandomData)
        initOneDB_Id_Request("StihMessages", databaseStorage.stihMessagesData)
        initOneDB_Id_Request("AforismMessages", databaseStorage.aforismMessagesData)
        initOneDB_Id_Request("AnekdotMessages", databaseStorage.anekdotMessagesData)
        initOneDB_Id_Request("StatusMessages", databaseStorage.statusMessagesData)
        initOneDB_Request_Response("RandomBazaBot", databaseStorage.bigMessagesData)
        initOneDB_userRights(databaseStorage.userRightsData)
        println("БД проинициализировалась")
    }

    fun disconnect() {
        try {
            conn.close()
            statmt.close()
            println("JDBC disconnected")
        } catch (se: SQLException) {
            se.printStackTrace()
        }
    }

    private fun fillData(resSet: ResultSet) {
        while (resSet.next()) {
            val requesttextbot = resSet.getString("requesttextbot")
            val id = Integer.parseInt(resSet.getString("id"))
            val responseTextBot = resSet.getString("responsetextbot")
            databaseStorage.botData.add(BotDatabase_IdRequestResponse(id, requesttextbot, responseTextBot))
        }
    }


    // initialize table ID REQUEST
    @Throws(SQLException::class)
    private fun initOneDB_Id_Request(tableDB: String, objectData: MutableList<BotDatabase_IdRequest>) {
        val resSet: ResultSet = statmt.executeQuery("SELECT * FROM $tableDB")
        while (resSet.next()) {
            val requesttextbot = resSet.getString("request")
            val id = Integer.parseInt(resSet.getString("id"))

            objectData.add(BotDatabase_IdRequest(id, requesttextbot))
        }
        resSet.close()
    }

    // initialize table   REQUEST RESPONSE
    @Throws(SQLException::class)
    private fun initOneDB_Request_Response(tableDB: String, objectData: MutableList<BotDatabase_RequestResponse>) {
        val resSet: ResultSet = statmt.executeQuery("SELECT * FROM $tableDB")
        while (resSet.next()) {
            val requesttextbot = resSet.getString("requesttextbot")
            val responsetextbot = resSet.getString("responsetextbot")

            objectData.add(BotDatabase_RequestResponse(requesttextbot, responsetextbot))

        }
        resSet.close()
    }

    // initialize table initOneDB_userRights
    @Throws(SQLException::class)
    private fun initOneDB_userRights(objectData: MutableList<UserIdRightsBD>) {

        val resSet: ResultSet = statmt.executeQuery("SELECT * FROM UserRights")
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
}