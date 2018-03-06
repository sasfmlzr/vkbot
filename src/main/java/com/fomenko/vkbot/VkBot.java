package com.fomenko.vkbot;

import com.apiVKmanual.client.ModeBot;
import com.fomenko.vkbot.controller.MainWindowController;
import com.fomenko.vkbot.controller.menuprogram.PropertiesProgramWindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class VkBot extends Application {



    @Override
    public void start(Stage primaryStage) {
       // ResourceBundle bundle = loadLocale(Locale.getDefault(), MainWindowController.resourcePath);

        ModeBot modeBot = new ModeBot();
        initializeIni();
        modeBot.setModeBot(PropertiesProgramWindowController.mode1);

        Scene scene = new Scene(new MainWindowController());

        //AnchorPane root = FXMLLoader.load(getClass().getResource(MainWindowController.fxmlPath), bundle);

        //Scene scene = new Scene(root);
        //scene.setRoot(root);

        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("Test VKBot");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }


    public static ResourceBundle loadLocale(Locale locale, String resourcePath)
    {
        Locale.setDefault(locale);
        return ResourceBundle.getBundle(resourcePath, Locale.getDefault());
    }


    public static void main(String[] args)
    {
        launch(args);
    }



    //////////Инициализация ini файла настроек на 4 бота
    public static void initializeIni() {
        Ini ini = null;
        try {
            ini = new Ini(new File("src/main/resources/com/fomenko/vkbot/resourcebundle/PropertiesProgramWindow/Properties.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }
  //      java.util.prefs.Preferences prefs = new IniPreferences(ini);
        System.out.println("Настройки успешно инициализировались");
        assert ini != null;
        PropertiesProgramWindowController.token1=ini.get("TokenBots", "Token1");
        PropertiesProgramWindowController.token2=ini.get("TokenBots", "Token2");
        PropertiesProgramWindowController.token3=ini.get("TokenBots", "Token3");
        PropertiesProgramWindowController.token4=ini.get("TokenBots", "Token4");
        PropertiesProgramWindowController.userId1=ini.get("TokenBots", "UserId1");
        PropertiesProgramWindowController.userId2=ini.get("TokenBots", "UserId2");
        PropertiesProgramWindowController.userId3=ini.get("TokenBots", "UserId3");
        PropertiesProgramWindowController.userId4=ini.get("TokenBots", "UserId4");
        PropertiesProgramWindowController.mode1=ini.get("ModeWorking", "Mode1");
        PropertiesProgramWindowController.mode2=ini.get("ModeWorking", "Mode2");


    }

}
