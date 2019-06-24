package com.sasfmlzr.vkbot.controller

import com.api.util.sig4j.signal.Signal0
import com.api.util.sig4j.signal.Signal1
import com.api.util.sig4j.signal.Signal2
import com.sasfmlzr.vkbot.VkBot
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import javafx.util.Duration
import java.io.IOException
import java.util.*

class LoginWindowPresenter {

    val sendCaptcha = Signal1<String>()
    val sendData = Signal2<String, String>()
    val sendPhoneConfirmed = Signal0()

    fun showOrHide(grid: GridPane, show: Boolean, gridIndex: Int, maxHeight: Int, vararg nodes: Node) {
        val start: KeyFrame
        val end: KeyFrame

        val expanded = nodes[0].visibleProperty().get()

        if (expanded && !show) {
            start = KeyFrame(Duration.ZERO, KeyValue(grid.rowConstraints[gridIndex].prefHeightProperty(), maxHeight))
            end = KeyFrame(Duration.millis(200.0), KeyValue(grid.rowConstraints[gridIndex].prefHeightProperty(), 0))

            for (n in nodes) {
                n.isVisible = false
                n.isManaged = false
            }
        } else if (!expanded && show) {
            start = KeyFrame(Duration.ZERO, KeyValue(grid.rowConstraints[gridIndex].prefHeightProperty(), 0))
            end = KeyFrame(Duration.millis(200.0), KeyValue(grid.rowConstraints[gridIndex].prefHeightProperty(), maxHeight))
        } else {
            start = KeyFrame(Duration.ZERO, KeyValue(grid.rowConstraints[gridIndex].prefHeightProperty(), 0))
            end = KeyFrame(Duration.ZERO, KeyValue(grid.rowConstraints[gridIndex].prefHeightProperty(), maxHeight))
        }

        val timeline = Timeline(start, end)

        timeline.onFinished = EventHandler<ActionEvent> {
            if (!expanded && show)
                for (n in nodes) {
                    n.isVisible = true
                    n.isManaged = true
                }
        }

        timeline.play()
    }


    // --------Показать форму после диалога-------- //
    fun showBrowserDialog(windowRoot: Window, URL: String, onReceiveBrowserResult: OnReceiveBrowserResult) {
        val bundle = VkBot.loadLocale(Locale.getDefault(), BrowserDialogWindowController.resourcePath)
        val loader = FXMLLoader(javaClass.getResource(BrowserDialogWindowController.fxmlPath), bundle)
        val pane: AnchorPane

        pane = loader.load()

        val browserStage = Stage()

        val scene = Scene(pane)
        scene.root = pane

        browserStage.scene = scene
        browserStage.isResizable = false

        browserStage.initModality(Modality.WINDOW_MODAL)
        browserStage.initOwner(windowRoot)

        val ctrl = loader.getController<BrowserDialogWindowController>()
        ctrl.initWindow()
        ctrl.setURL(URL)

        ctrl.sendBrowserResult.connect {
            onReceiveBrowserResult.onReceiveBrowserResult(it)
        }

        browserStage.title = bundle.getString("BrowserDialogWindow.title.text")

        Platform.runLater { browserStage.show() }
    }

    interface OnReceiveBrowserResult {
        fun onReceiveBrowserResult(isReceive: Boolean)
    }

    fun createTaskStage(): Stage {
        try {
            val bundle = VkBot.loadLocale(Locale.getDefault(), BotTaskWindowController.resourcePath)
            val loader = FXMLLoader(javaClass.getResource(BotTaskWindowController.fxmlPath), bundle)
            val pane = loader.load<AnchorPane>()

            val taskStage = Stage()

            val scene = Scene(pane)
            scene.root = pane

            taskStage.scene = scene
            taskStage.isResizable = false

            val ctrl = loader.getController<BotTaskWindowController>()
            ctrl.initWindow()

            taskStage.title = "Set api.bot task"
            return taskStage
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        throw RuntimeException("Something was wrong")
    }
}
