package com.newapi.apivk.architecture.storage

import com.sasfmlzr.apivk.`object`.BotDatabase_IdRequest
import com.sasfmlzr.apivk.`object`.BotDatabase_IdRequestResponse
import com.sasfmlzr.apivk.`object`.BotDatabase_RequestResponse
import com.sasfmlzr.apivk.`object`.UserIdRightsBD

class DatabaseStorage private constructor() {

    val botData = ArrayList<BotDatabase_IdRequestResponse>()
    val botRandomData = ArrayList<BotDatabase_IdRequest>()
    val stihMessagesData = ArrayList<BotDatabase_IdRequest>()
    val anekdotMessagesData = ArrayList<BotDatabase_IdRequest>()
    val aforismMessagesData = ArrayList<BotDatabase_IdRequest>()
    val statusMessagesData = ArrayList<BotDatabase_IdRequest>()
    val bigMessagesData = ArrayList<BotDatabase_RequestResponse>()
    val userRightsData = ArrayList<UserIdRightsBD>()

    companion object {
        @Volatile
        private var instance: DatabaseStorage? = null

        fun getInstance(): DatabaseStorage {
            return instance ?: synchronized(this) {
                instance ?: DatabaseStorage().also { instance = it }
            }
        }
    }

    fun clear() {
        botData.clear()
        botRandomData.clear()
        stihMessagesData.clear()
        anekdotMessagesData.clear()
        aforismMessagesData.clear()
        statusMessagesData.clear()
        bigMessagesData.clear()
        userRightsData.clear()
    }
}