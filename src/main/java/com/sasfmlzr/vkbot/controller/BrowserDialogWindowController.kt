package com.sasfmlzr.vkbot.controller

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.layout.AnchorPane
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import javafx.stage.WindowEvent
import java.net.URL
import java.util.*

class BrowserDialogWindowController : Initializable {

    @FXML
    private lateinit var root: AnchorPane
    @FXML
    private lateinit var webView: WebView

    private lateinit var engine: WebEngine

    override fun initialize(location: URL, resources: ResourceBundle) {
        this.engine = webView.engine
    }

    internal fun initWindow() {
        val scene = root.scene
        val window = scene.window

        window.onCloseRequest = EventHandler<WindowEvent> { it.consume() }

        this.engine.locationProperty().addListener { prop, before, after ->
            //true if OK
            Platform.runLater { this.close() }
        }
    }

    fun setURL(URL: String) {
        this.engine.load(URL)
    }

    @FXML
    private fun onCancel() {
        close()
    }

    private fun close() {
        root.scene.window.hide()
    }

    companion object {
        val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.BrowserDialogWindow.messages"
        val fxmlPath = "/com/sasfmlzr/vkbot/views/BrowserDialogWindow.fxml"
    }
}
