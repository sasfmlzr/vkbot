package com.sasfmlzr.vkbot.controller

import com.api.util.Effects
import com.database.DatabaseEntity
import com.sasfmlzr.apivk.State
import com.sasfmlzr.apivk.`object`.StatisticsVariable
import com.sasfmlzr.apivk.bot.GroupBot
import com.sasfmlzr.apivk.bot.UserBot
import com.sasfmlzr.vkbot.StaticModel
import com.sasfmlzr.vkbot.controller.menuprogram.StatisticsWindowController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import java.io.File
import java.io.IOException
import java.net.URL
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class BotCardControllerDDD internal constructor() : AnchorPane(), Initializable {

    @FXML
    private lateinit var avatar: ImageView
    @FXML
    private lateinit var root: AnchorPane
    @FXML
    private lateinit var buttonLoad: Button
    @FXML
    private lateinit var buttonPowerBot: Button
    @FXML
    private lateinit var nameBot: Label

    init {
        val loader = FXMLLoader(javaClass.getResource(fxmlPath))

        val file = File(loader.location.toURI())
        loader.setRoot(this)
        loader.setController(this)
        try {
            loader.load<Any>()
        } catch (ex: IOException) {
            Logger.getLogger(BotCardControllerDDD::class.java.name).log(Level.SEVERE, null, ex)
        }

    }

    override fun initialize(location: URL, resources: ResourceBundle) {}

    @Throws(SQLException::class, ClassNotFoundException::class)
    private fun recursion() {
        StatisticsVariable.countSendMessageUser = 0
        StatisticsVariable.countSendMessage = 0
        StatisticsVariable.timeZaprosFinishSumm = 0
        StatisticsWindowController.seriesZaprosVk.data.clear()          //обнуление статистики запросов
        StatisticsWindowController.seriesItogVk.data.clear()          //обнуление статистики запросов
        StatisticsWindowController.seriesThread.data.clear()                        //обнуление статистики задержки потока////здесь иногда ловится исключение

        if (!State.databaseLoaded) {
            DatabaseEntity.database.connectDatabase()            //подключение бд
            DatabaseEntity.database.InitDB()          //инициализация таблиц бд в объект
        }
        StatisticsVariable.timeProgramStart = System.currentTimeMillis()
        StaticModel.userBot.botApiClient().stateBot.pushPowerBot = true
        setAvatarBot(this, StaticModel.userBot)
        StaticModel.userBot.run()
    }

    @Throws(SQLException::class, ClassNotFoundException::class)
    fun recursionGroup() {

        if (!State.databaseLoaded) {
            DatabaseEntity.database.connectDatabase()            //подключение бд
            DatabaseEntity.database.InitDB()          //инициализация таблиц бд в объект
        }
        StatisticsVariable.timeProgramStart = System.currentTimeMillis()
        StaticModel.userBot.botApiClient().stateBot.pushPowerBot = true
        setAvatarBot(this, StaticModel.groupBot)
        StaticModel.groupBot.run()
    }


    private fun setAvatarBot(childList: BotCardControllerDDD, bot: UserBot) {
        childList.setText(bot.botName)
        childList.setAvatar(bot.botImage)
    }

    private fun setAvatarBot(childList: BotCardControllerDDD, bot: GroupBot) {
        childList.setText(bot.botName)
        childList.setAvatar(bot.botImage)
    }

    private fun setAvatar(image: Image?) {
        avatar.image = image
    }

    private fun setText(text: String?) {
        nameBot.text = text
    }

    @FXML
    @Throws(SQLException::class, ClassNotFoundException::class)
    fun onButtonPowerBot() {
        toggleButtonActive(buttonPowerBot)
        if (!buttonPowerBot.isFocused) {
            print("Bot working" + "\n")
            StaticModel.userBot.botApiClient().stateBot.pushPowerBot = true
            recursion()
            //recursionGroup();
        } else {
            print("Bot not working" + "\n")
            StaticModel.userBot.botApiClient().stateBot.pushPowerBot = false
        }
    }

    @FXML
    private fun onLoad() {
        toggleButtonActive(buttonLoad)
    }


    @FXML
    private fun onSettings() {
    }

    @FXML
    private fun onInfo() {
    }

    @FXML
    private fun onRemove() {
    }

    private fun toggleButtonActive(button: Button) {
        if (button.id != "button-active") {
            button.id = "button-active"
            button.effect = Effects.imageButtonActive
            root.requestFocus()
        } else {
            button.id = "button"
            button.effect = null
        }
    }

    companion object {
        const val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.BotCard.messages"
        const val fxmlPath = "/com/sasfmlzr/vkbot/views/BotCard.fxml"
    }
}
