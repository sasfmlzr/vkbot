package com.sasfmlzr.vkbot.controller.menuprogram

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextArea
import java.io.BufferedReader
import java.io.FileReader
import java.net.URL
import java.util.*

class AboutProgramWindowController : Initializable {
    
    companion object {
        const val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.AboutProgramWindow.messages"
        const val fxmlPath = "AboutProgramWindow.fxml"
    }

    @FXML
    private lateinit var textLog: TextArea

    override fun initialize(location: URL, resources: ResourceBundle) {}

    fun initWindow() {
        val bReader = BufferedReader(FileReader("/com/sasfmlzr/vkbot/resourcebundle/AboutProgramWindow/About.txt"))
        val text = bReader.readText()
        textLog.text = text
        println(text)
    }
}
