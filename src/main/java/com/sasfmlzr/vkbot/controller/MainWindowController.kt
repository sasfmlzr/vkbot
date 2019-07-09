package com.sasfmlzr.vkbot.controller

import com.sasfmlzr.vkbot.VkBot
import com.sasfmlzr.vkbot.controller.menuprogram.*
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.RadioMenuItem
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import java.io.IOException
import java.net.URL
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

class MainWindowController : AnchorPane(), Initializable {
    @FXML
    private lateinit var root: AnchorPane
    @FXML
    private lateinit var resources: ResourceBundle
    @FXML
    private lateinit var menuLangEn: RadioMenuItem
    @FXML
    private lateinit var menuLangRu: RadioMenuItem
    @FXML
    private lateinit var tabPane: TabPane
    private val fxmlPath = "/com/sasfmlzr/vkbot/views/MainWindow.fxml"

    init {
        initClass(true)
    }

    private fun initClass(isRussianLanguage: Boolean) {
        val loader = FXMLLoader(javaClass.getResource(fxmlPath))
        loader.setRoot(this)
        loader.setController(this)
        resources = if (isRussianLanguage) {
            VkBot.loadLocale(Locale("ru", "RU"), resourcePath)
        } else {
            VkBot.loadLocale(Locale("en", "US"), resourcePath)
        }
        loader.resources = resources
        try {
            loader.load<Any>()
            println()
        } catch (ex: IOException) {
            Logger.getLogger(MainWindowController::class.java.name).log(Level.SEVERE, null, ex)
        }

    }

    override fun initialize(location: URL, resources: ResourceBundle) {
        this.resources = resources

        menuLangEn.onAction = LangChangeHandler()
        menuLangRu.onAction = LangChangeHandler()

        tabPane.tabClosingPolicy = TabPane.TabClosingPolicy.SELECTED_TAB
        addMainTab()
        addBotTab("Boo")

    }

    private fun addMainTab() {
        try {
            val pane = AnchorPane()
            pane.children.add(FXMLLoader.load(this.javaClass.getResource(MainTabController.fxmlPath), resources))

            tabPane.tabs[0].content = pane
            tabPane.tabs[0].onClosed = TabCloseHandler()
            tabPane.tabs[0].style = "-fx-font-size: 14;"

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private inner class TabCloseHandler : EventHandler<Event> {
        override fun handle(arg0: Event) {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "YOU, SCUM"
            alert.contentText = "YOU CLOSED THE TAB: " + (arg0.source as Tab).text + ", DIDN'T YOU?"

            alert.showAndWait()
        }
    }

    private fun addBotTab(name: String) {

        val pane = AnchorPane()

        pane.children.add(BotTabController())
        //pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("BotTab.fxml"), resources));

        val tab = Tab()
        tab.content = pane
        tab.text = name
        tab.isClosable = true
        tab.onClosed = TabCloseHandler()
        tab.style = "-fx-font-size: 14;"

        tabPane.tabs.add(tab)
    }


    private inner class LangChangeHandler : EventHandler<ActionEvent> {
        override fun handle(evt: ActionEvent) {
            if (evt.source == menuLangEn) {
                reload(false)
            } else if (evt.source == menuLangRu) {
                reload(true)
            }
        }
    }

    private fun reload(isRussianLanguage: Boolean?) {
        initClass(isRussianLanguage!!)
    }

    @FXML
    @Throws(IOException::class)
    private fun onMenuLogOpen() {

        val bundle = VkBot.loadLocale(Locale.getDefault(), LogWindowController.resourcePath)

        val loader = FXMLLoader(javaClass.getResource(menuProgramPath + LogWindowController.fxmlPath), bundle)
        val root = loader.load<AnchorPane>()

        val logStage = Stage()
        val scene = Scene(root)
        scene.root = root
        root.requestFocus()

        logStage.scene = scene
        val ctrl = loader.getController<LogWindowController>()
        ctrl.initWindow()
        logStage.isResizable = false

        logStage.title = bundle.getString("LogWindow.title.text")
        logStage.show()
    }

    @FXML
    @Throws(IOException::class)
    private fun onMenuAboutOpen() {

        val bundle = VkBot.loadLocale(Locale.getDefault(), AboutProgramWindowController.resourcePath)

        //FXMLLoader loader = new FXMLLoader(getClass().getResource(AboutProgramWindowController.fxmlPath), bundle);
        val loader = FXMLLoader(javaClass.getResource(menuProgramPath + AboutProgramWindowController.fxmlPath), bundle)
        val root = loader.load<AnchorPane>()

        val logStage = Stage()
        val scene = Scene(root)
        scene.root = root
        root.requestFocus()

        logStage.scene = scene
        val ctrl = loader.getController<AboutProgramWindowController>()
        ctrl.initWindow()
        logStage.isResizable = false

        logStage.title = bundle.getString("AboutProgramWindow.title.text")
        logStage.show()
    }

    @FXML
    @Throws(IOException::class)
    private fun onMenuPropertiesOpen() {

        val bundle = VkBot.loadLocale(Locale.getDefault(), PropertiesProgramWindowController.resourcePath)

        val loader = FXMLLoader(javaClass.getResource(menuProgramPath + PropertiesProgramWindowController.fxmlPath), bundle)
        val root = loader.load<AnchorPane>()

        val logStage = Stage()
        val scene = Scene(root)
        scene.root = root
        root.requestFocus()

        logStage.scene = scene
        val ctrl = loader.getController<PropertiesProgramWindowController>()
        ctrl.initWindow()
        logStage.isResizable = false

        logStage.title = bundle.getString("PropertiesProgramWindow.title.text")
        logStage.show()
    }

    @FXML
    @Throws(IOException::class)
    private fun onMenuStatisticsBot() {

        val bundle = VkBot.loadLocale(Locale.getDefault(), StatisticsWindowController.resourcePath)

        val loader = FXMLLoader(javaClass.getResource(menuProgramPath + StatisticsWindowController.fxmlPath), bundle)
        val root = loader.load<AnchorPane>()

        val logStage = Stage()
        val scene = Scene(root)
        scene.root = root
        root.requestFocus()

        logStage.scene = scene
        val ctrl = loader.getController<StatisticsWindowController>()
        ctrl.initWindow()
        logStage.isResizable = false

        logStage.title = bundle.getString("StatisticsWindow.title.text")
        logStage.show()
    }

    @FXML
    @Throws(IOException::class, SQLException::class)
    private fun onMenuDatabaseBot() {

        val bundle = VkBot.loadLocale(Locale.getDefault(), DataBaseWindowController.resourcePath)

        val loader = FXMLLoader(javaClass.getResource(menuProgramPath + DataBaseWindowController.fxmlPath), bundle)
        val root = loader.load<AnchorPane>()

        val logStage = Stage()
        val scene = Scene(root)
        scene.root = root
        root.requestFocus()

        logStage.scene = scene
        val ctrl = loader.getController<DataBaseWindowController>()
        ctrl.initWindow()
        logStage.isResizable = false

        logStage.title = bundle.getString("DataBaseWindow.title.text")
        logStage.show()
    }

    @FXML
    @Throws(IOException::class)
    private fun onMenuFileAdd() {
        val bundle = VkBot.loadLocale(Locale.getDefault(), LoginWindowController.resourcePath)

        val loader = FXMLLoader(javaClass.getResource(LoginWindowController.fxmlPath), bundle)
        val root = loader.load<AnchorPane>()

        val loginStage = Stage()
        val scene = Scene(root)
        scene.root = root
        root.requestFocus()

        loginStage.scene = scene
        val ctrl = loader.getController<LoginWindowController>()
        ctrl.initWindow()
        loginStage.isResizable = false

        loginStage.title = bundle.getString("LoginWindow.title.text")
        loginStage.show()
    }

    @FXML
    private fun onMenuFileClose() {
        Platform.exit()
        exitProcess(0)
    }

    companion object {

        private const val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.mainwindow.messages"
        private const val menuProgramPath = "/com/sasfmlzr/vkbot/views/menuProgram/"
    }
}
