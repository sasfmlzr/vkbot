package com.fomenko.vkbot.controller;

import com.fomenko.vkbot.VkBot;
import com.fomenko.vkbot.controller.menuprogram.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindowController extends AnchorPane implements Initializable {
    @FXML private AnchorPane root;
    @FXML private ResourceBundle resources;
    @FXML private RadioMenuItem menuLangEn;
    @FXML private RadioMenuItem menuLangRu;
    @FXML private TabPane tabPane;

    private final static String resourcePath = "com.fomenko.vkbot.resourcebundle.mainwindow.messages";
    private final static String menuProgramPath = "/com/fomenko/vkbot/views/menuProgram/";
    private final String fxmlPath="/com/fomenko/vkbot/views/MainWindow.fxml";

    public MainWindowController() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setRoot(this);
        loader.setController(this);
        resources = VkBot.loadLocale(new Locale("ru", "RU"), resourcePath);
        loader.setResources(resources);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        menuLangEn.setOnAction(new LangChangeHandler());
        menuLangRu.setOnAction(new LangChangeHandler());

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        addMainTab();
        try {
            addBotTab("Boo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addMainTab()
    {
        try
        {
            AnchorPane pane = new AnchorPane();
            pane.getChildren().add(FXMLLoader.load(this.getClass().getResource(MainTabController.fxmlPath), resources));

            tabPane.getTabs().get(0).setContent(pane);
            tabPane.getTabs().get(0).setOnClosed(new TabCloseHandler());
            tabPane.getTabs().get(0).setStyle("-fx-font-size: 14;");

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private class TabCloseHandler implements EventHandler<Event>
    {
        public void handle(Event arg0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("YOU, SCUM");
            alert.setContentText("YOU CLOSED THE TAB: " + ((Tab)arg0.getSource()).getText()+ ", DIDN'T YOU?");

            alert.showAndWait();
        }
    }





    private void addBotTab(String name) throws IOException {

        AnchorPane pane = new AnchorPane();

        pane.getChildren().add(new BotTabController());
        //pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("BotTab.fxml"), resources));

        Tab tab = new Tab();
        tab.setContent(pane);
        tab.setText(name);
        tab.setClosable(true);
        tab.setOnClosed(new TabCloseHandler());
        tab.setStyle("-fx-font-size: 14;");

        tabPane.getTabs().add(tab);
    }



    private class LangChangeHandler implements EventHandler<ActionEvent>
    {
        public void handle(ActionEvent evt)
        {
            if (evt.getSource().equals(menuLangEn))
            {
                resources = VkBot.loadLocale(new Locale("en", "US"), resourcePath);
            }
            else if (evt.getSource().equals(menuLangRu))
            {
                resources = VkBot.loadLocale(new Locale("ru", "RU"), resourcePath);
            }
            try
            {
                reload();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void reload() throws IOException
    {
        Scene scene = root.getScene();
        scene.setRoot(FXMLLoader.load(getClass().getResource(fxmlPath), resources));
    }









    @FXML private void onMenuLogOpen() throws IOException
    {

        ResourceBundle bundle = VkBot.loadLocale (Locale.getDefault(), LogWindowController.resourcePath);


        FXMLLoader loader = new FXMLLoader(getClass().getResource(menuProgramPath + LogWindowController.fxmlPath), bundle);
        AnchorPane root = loader.load();

        Stage logStage = new Stage();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        root.requestFocus();

        logStage.setScene(scene);
        LogWindowController ctrl = loader.getController();
        ctrl.initWindow();
        logStage.setResizable(false);

        logStage.setTitle(bundle.getString("LogWindow.title.text"));
        logStage.show();
    }

    @FXML private void onMenuAboutOpen() throws IOException
    {

        ResourceBundle bundle = VkBot.loadLocale (Locale.getDefault(), AboutProgramWindowController.resourcePath);

        //FXMLLoader loader = new FXMLLoader(getClass().getResource(AboutProgramWindowController.fxmlPath), bundle);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(menuProgramPath +AboutProgramWindowController.fxmlPath), bundle);
        AnchorPane root = loader.load();

        Stage logStage = new Stage();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        root.requestFocus();

        logStage.setScene(scene);
        AboutProgramWindowController ctrl = loader.getController();
        ctrl.initWindow();
        logStage.setResizable(false);

        logStage.setTitle(bundle.getString("AboutProgramWindow.title.text"));
        logStage.show();
    }

    @FXML private void onMenuPropertiesOpen() throws IOException
    {

        ResourceBundle bundle = VkBot.loadLocale (Locale.getDefault(), PropertiesProgramWindowController.resourcePath);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(menuProgramPath +PropertiesProgramWindowController.fxmlPath), bundle);
        AnchorPane root = loader.load();

        Stage logStage = new Stage();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        root.requestFocus();

        logStage.setScene(scene);
        PropertiesProgramWindowController ctrl = loader.getController();
        ctrl.initWindow();
        logStage.setResizable(false);

        logStage.setTitle(bundle.getString("PropertiesProgramWindow.title.text"));
        logStage.show();
    }

    @FXML private void onMenuStatisticsBot() throws IOException
    {

        ResourceBundle bundle = VkBot.loadLocale (Locale.getDefault(), StatisticsWindowController.resourcePath);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(menuProgramPath +StatisticsWindowController.fxmlPath), bundle);
        AnchorPane root = loader.load();

        Stage logStage = new Stage();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        root.requestFocus();

        logStage.setScene(scene);
        StatisticsWindowController ctrl = loader.getController();
        ctrl.initWindow();
        logStage.setResizable(false);

        logStage.setTitle(bundle.getString("StatisticsWindow.title.text"));
        logStage.show();
    }

    @FXML private void onMenuDatabaseBot() throws IOException, SQLException, ClassNotFoundException {

        ResourceBundle bundle = VkBot.loadLocale (Locale.getDefault(), DataBaseWindowController.resourcePath);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(menuProgramPath +DataBaseWindowController.fxmlPath), bundle);
        AnchorPane root = loader.load();

        Stage logStage = new Stage();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        root.requestFocus();

        logStage.setScene(scene);
        DataBaseWindowController ctrl = loader.getController();
        ctrl.initWindow();
        logStage.setResizable(false);

        logStage.setTitle(bundle.getString("DataBaseWindow.title.text"));
        logStage.show();
    }



    @FXML private void onMenuFileAdd() throws IOException
    {
        ResourceBundle bundle = VkBot.loadLocale (Locale.getDefault(), LoginWindowController.resourcePath);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(LoginWindowController.fxmlPath), bundle);
        AnchorPane root = loader.load();

        Stage loginStage = new Stage();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        root.requestFocus();

        loginStage.setScene(scene);
        LoginWindowController ctrl = loader.getController();
        ctrl.initWindow();
        loginStage.setResizable(false);

        loginStage.setTitle(bundle.getString("LoginWindow.title.text"));
        loginStage.show();
    }

    @FXML private void onMenuFileClose()
    {
        Platform.exit();
        System.exit(0);
    }



}


