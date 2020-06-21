package com.sasfmlzr.vkbot.controller.menuprogram

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextArea
import java.io.BufferedReader
import java.io.FileReader
import java.net.URL
import java.util.*

class PropertiesProgramWindowController : Initializable {
    @FXML
    private lateinit var textLog: TextArea

    override fun initialize(location: URL, resources: ResourceBundle) {}

    fun initWindow() {
        val bReader = BufferedReader(FileReader("src/resources/locale/PropertiesProgramWindow/Properties.ini"))
        val text = bReader.readText()
        textLog.text = text
    }

    companion object {
        const val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.PropertiesProgramWindow.messages"
        const val fxmlPath = "PropertiesProgramWindow.fxml"

        var token1: String? = null
        var token2: String? = null
        var token3: String? = null
        var token4: String? = null        // токены из ini для ботов
        var userId1: String? = null
        var userId2: String? = null
        var userId3: String? = null
        var userId4: String? = null    // userid из ini для ботов
        var mode1: String? = null
        var mode2: String? = null    // userid из ini для ботов
    }
}
