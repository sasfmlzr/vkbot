package com.sasfmlzr.vkbot


import com.sasfmlzr.vkbot.controller.MainWindowController
import com.sasfmlzr.vkbot.controller.menuprogram.PropertiesProgramWindowController
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import org.ini4j.Ini

import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.ResourceBundle
import kotlin.system.exitProcess

class VkBot : Application() {

    override fun start(primaryStage: Stage) {
        var primaryStage = primaryStage
        // ResourceBundle bundle = loadLocale(Locale.getDefault(), MainWindowController.resourcePath);

        initializeIni()

        val scene = Scene(MainWindowController())

        //AnchorPane root = FXMLLoader.load(getClass().getResource(MainWindowController.fxmlPath), bundle);

        //Scene scene = new Scene(root);
        //scene.setRoot(root);

        primaryStage = Stage()
        primaryStage.scene = scene
        primaryStage.minWidth = 700.0
        primaryStage.minHeight = 600.0
        primaryStage.title = "Test VKBot"
        primaryStage.show()

        primaryStage.setOnCloseRequest {
            Platform.exit()
            exitProcess(0)
        }
    }

    companion object {

        fun loadLocale(locale: Locale, resourcePath: String): ResourceBundle {
            Locale.setDefault(locale)
            return ResourceBundle.getBundle(resourcePath, Locale.getDefault())
        }

        @JvmStatic
        fun main(args: Array<String>) {
            launch(VkBot::class.java)
        }


        //////////Инициализация ini файла настроек на 4 бота
        fun initializeIni() {
            var ini: Ini? = null
            try {
                ini = Ini(File("Properties.ini"))
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //      java.util.prefs.Preferences prefs = new IniPreferences(ini);
            println("Настройки успешно инициализировались")
            assert(ini != null)
            PropertiesProgramWindowController.token1 = ini!!.get("TokenBots", "Token1")
            PropertiesProgramWindowController.userId1 = ini.get("TokenBots", "UserId1")
            PropertiesProgramWindowController.mode1 = ini.get("ModeWorking", "Mode1")
            PropertiesProgramWindowController.mode2 = ini.get("ModeWorking", "Mode2")
        }
    }

}
