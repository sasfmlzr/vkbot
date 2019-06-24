package com.sasfmlzr.vkbot.controller.menuprogram

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextArea
import java.io.BufferedReader
import java.io.FileReader
import java.net.URL
import java.util.*

class LogWindowController : Initializable {
    @FXML
    private lateinit var textLog: TextArea

    override fun initialize(location: URL, resources: ResourceBundle) {}

    fun initWindow() {


        val bReader = BufferedReader(FileReader("src/resources/locale/LogWindow/Log.txt"))
        val text = bReader.readText()
        textLog.text = text
        //////////////логи берутся из файла
    }

    companion object {
        val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.LogWindow.messages"
        val fxmlPath = "LogWindow.fxml"
    }
}
