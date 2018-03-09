package com.sasfmlzr.vkbot.controller;

import com.api.util.Effects;
import com.sasfmlzr.apiVK.bot.GroupBot;
import com.sasfmlzr.apiVK.bot.UserBot;
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

import static com.sasfmlzr.apiVK.State.databaseLoaded;
import static com.sasfmlzr.apiVK.object.StatisticsVariable.*;

public class BotCardController extends  AnchorPane implements Initializable
{
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

	@FXML	private ImageView avatar;
	@FXML private AnchorPane root;
	@FXML private Button buttonLoad;
    @FXML	private Button buttonPowerBot;
	@FXML	private Label nameBot;

	public void initWindow(){	}
	public void setavatar(Image image){
		avatar.setImage(image);
	}
	public void settext(String text){
		nameBot.setText(text);
	}

	public void initialize(URL location, ResourceBundle resources)	{
	}

	public void  recursion() throws SQLException, ClassNotFoundException {
		countSendMessageUser=0;
		countSendMessage = 0;
		timeZaprosFinishSumm=0;
		StatisticsWindowController.seriesZaprosVk.getData().clear();          //обнуление статистики запросов
		StatisticsWindowController.seriesItogVk.getData().clear();          //обнуление статистики запросов
		StatisticsWindowController.seriesThread.getData().clear();                        //обнуление статистики задержки потока////здесь иногда ловится исключение

		if (!databaseLoaded){
			StaticModel.userBot.botApiClient().database.connectDatabase();            //подключение бд
			StaticModel.userBot.botApiClient().database.InitDB();          //инициализация таблиц бд в объект
		}
		timeProgramStart = System.currentTimeMillis();
		StaticModel.userBot.botApiClient().stateBot.pushPowerBot=true;
		setAvatarBot(this, StaticModel.userBot);
		StaticModel.userBot.run();
	}

	public void  recursionGroup() throws SQLException, ClassNotFoundException {

		if (!databaseLoaded){
			StaticModel.userBot.botApiClient().database.connectDatabase();            //подключение бд
			StaticModel.userBot.botApiClient().database.InitDB();          //инициализация таблиц бд в объект
		}
		timeProgramStart = System.currentTimeMillis();
		StaticModel.userBot.botApiClient().stateBot.pushPowerBot=true;
		setAvatarBot(this, StaticModel.groupBot);
		StaticModel.groupBot.run();
	}



	private void setAvatarBot(BotCardController childList, UserBot bot)   {
		childList.settext(bot.getBotName());
		childList.setavatar(bot.getBotImage());
	}

	private void setAvatarBot(BotCardController childList, GroupBot bot)   {
		childList.settext(bot.getBotName());
		childList.setavatar(bot.getBotImage());
	}
	@FXML public void onButtonPowerBot() throws SQLException, ClassNotFoundException {
		toggleButtonActive (buttonPowerBot);
		if (!buttonPowerBot.isFocused())
		{
			System.out.print("Bot working" + "\n");
			StaticModel.userBot.botApiClient().stateBot.pushPowerBot =true;
			recursion();
			//recursionGroup();
		}
		else
		{
			System.out.print("Bot not working" + "\n");
			StaticModel.userBot.botApiClient().stateBot.pushPowerBot =false;
		}
	}


	@FXML private void onLoad()
	{
		toggleButtonActive (buttonLoad);
	}


	@FXML private void onSettings()
	{
	}
	
	@FXML private void onInfo()
	{
	}
	
	@FXML private void onRemove()
	{

	}

	private void toggleButtonActive(Button button)
	{
		if (!Objects.equals(button.getId(), "button-active"))
		{
			button.setId("button-active");
			button.setEffect(Effects.imageButtonActive);
			root.requestFocus();
		}
		else
		{
			button.setId("button");
			button.setEffect(null);
		}
	}


}