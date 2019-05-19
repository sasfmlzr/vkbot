package com.sasfmlzr.vkbot.controller;

import com.api.util.Effects;
import com.database.DatabaseEntity;
import com.sasfmlzr.apivk.State;
import com.sasfmlzr.apivk.bot.GroupBot;
import com.sasfmlzr.apivk.bot.UserBot;
import com.sasfmlzr.apivk.object.StatisticsVariable;
import com.sasfmlzr.vkbot.StaticModel;
import com.sasfmlzr.vkbot.controller.menuprogram.StatisticsWindowController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotCardController extends AnchorPane implements Initializable {
    public final static String resourcePath = "com.sasfmlzr.vkbot.resourcebundle.BotCard.messages";
    public final static String fxmlPath = "/com/sasfmlzr/vkbot/views/BotCard.fxml";

    BotCardController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(BotCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private ImageView avatar;
    @FXML
    private AnchorPane root;
    @FXML
    private Button buttonLoad;
    @FXML
    private Button buttonPowerBot;
    @FXML
    private Label nameBot;

    public void initWindow() {
    }

    public void setavatar(Image image) {
        avatar.setImage(image);
    }

    public void settext(String text) {
        nameBot.setText(text);
    }

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void recursion() throws SQLException, ClassNotFoundException {
        StatisticsVariable.INSTANCE.setCountSendMessageUser(0);
        StatisticsVariable.INSTANCE.setCountSendMessage(0);
        StatisticsVariable.INSTANCE.setTimeZaprosFinishSumm(0);
        StatisticsWindowController.seriesZaprosVk.getData().clear();          //обнуление статистики запросов
        StatisticsWindowController.seriesItogVk.getData().clear();          //обнуление статистики запросов
        StatisticsWindowController.seriesThread.getData().clear();                        //обнуление статистики задержки потока////здесь иногда ловится исключение

        if (!State.INSTANCE.getDatabaseLoaded()) {
            DatabaseEntity.INSTANCE.getDatabase().connectDatabase();            //подключение бд
            DatabaseEntity.INSTANCE.getDatabase().InitDB();          //инициализация таблиц бд в объект
        }
        StatisticsVariable.INSTANCE.setTimeProgramStart(System.currentTimeMillis());
        StaticModel.userBot.botApiClient().getStateBot().setPushPowerBot(true);
        setAvatarBot(this, StaticModel.userBot);
        StaticModel.userBot.run();
    }

    public void recursionGroup() throws SQLException, ClassNotFoundException {

        if (!State.INSTANCE.getDatabaseLoaded()) {
            DatabaseEntity.INSTANCE.getDatabase().connectDatabase();            //подключение бд
            DatabaseEntity.INSTANCE.getDatabase().InitDB();          //инициализация таблиц бд в объект
        }
        StatisticsVariable.INSTANCE.setTimeProgramStart(System.currentTimeMillis());
        StaticModel.userBot.botApiClient().getStateBot().setPushPowerBot(true);
        setAvatarBot(this, StaticModel.groupBot);
        StaticModel.groupBot.run();
    }


    private void setAvatarBot(BotCardController childList, UserBot bot) {
        childList.settext(bot.getBotName());
        childList.setavatar(bot.getBotImage());
    }

    private void setAvatarBot(BotCardController childList, GroupBot bot) {
        childList.settext(bot.getBotName());
        childList.setavatar(bot.getBotImage());
    }

    @FXML
    public void onButtonPowerBot() throws SQLException, ClassNotFoundException {
        toggleButtonActive(buttonPowerBot);
        if (!buttonPowerBot.isFocused()) {
            System.out.print("Bot working" + "\n");
            StaticModel.userBot.botApiClient().getStateBot().setPushPowerBot(true);
            recursion();
            //recursionGroup();
        } else {
            System.out.print("Bot not working" + "\n");
            StaticModel.userBot.botApiClient().getStateBot().setPushPowerBot(false);
        }
    }


    @FXML
    private void onLoad() {
        toggleButtonActive(buttonLoad);
    }


    @FXML
    private void onSettings() {
    }

    @FXML
    private void onInfo() {
    }

    @FXML
    private void onRemove() {

    }

    private void toggleButtonActive(Button button) {
        if (!Objects.equals(button.getId(), "button-active")) {
            button.setId("button-active");
            button.setEffect(Effects.imageButtonActive);
            root.requestFocus();
        } else {
            button.setId("button");
            button.setEffect(null);
        }
    }


}
