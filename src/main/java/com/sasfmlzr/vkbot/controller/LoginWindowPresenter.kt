package com.sasfmlzr.vkbot.controller

import com.sasfmlzr.vkbot.VkBot
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import javafx.util.Duration
import java.io.IOException
import java.util.*

class LoginWindowPresenter {

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
