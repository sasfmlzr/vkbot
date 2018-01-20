package com.fomenko.vkbot.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.api.util.Effects;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import static com.fomenko.vkbot.controller.BotTabController.ava;
import static com.fomenko.vkbot.controller.BotTabController.recursion;


public class BotCardController extends  AnchorPane implements Initializable
{
	public final static String resourcePath = "com.fomenko.vkbot.resourcebundle.BotCard.messages";
	public final static String fxmlPath = "/com/fomenko/vkbot/views/BotCard.fxml";

	BotCardController() {


		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fomenko/vkbot/views/BotCard.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			Logger.getLogger(BotCardController.class.getName()).log(Level.SEVERE, null, ex);
		}




	//	FXMLLoader loader=new FXMLLoader();;
	//	loader.setLocation(getClass().getResource("BotCard.fxml"));
	//	loader.setController(this);
		//loader.load();


	}

	@FXML public ImageView avatar;



	//@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	
//	@FXML private Button buttonToggle;
	@FXML private Button buttonLoad;
    @FXML public Button buttonPowerBot;

	@FXML public  Label nameBot;


	static boolean pushPowerBot=true;

	public void initWindow(){
		if (ava){
			Image ss = avatar.getImage();
			avatar.setImage(ss);
			avatar.setImage(null);
		}
	}

	public Image getavatar(){
	return avatar.getImage();
	}

	public void setavatar(Image image){
		avatar.setImage(image);
	}
	public String gettext(){
		return nameBot.getText();
	}

	public void settext(String text){
		nameBot.setText(text);
	}


	public void initialize(URL location, ResourceBundle resources)
	{

		//this.resources = resources;


		//Image ss = avatar.getImage();
		//avatar.setImage(ss);
		//avatar.setImage(null);
	//	fxmlLoader.setLocation(location);
	//	fxmlLoader.setRoot(this);
	//	fxmlLoader.setController(this);
		//Parent root;


		/*
		try {
			root = Main.getfxmlLoader().load();
		} catch (IOException e) {
			e.printStackTrace();
		}*/


//		brr= Main.getfxmlLoader().getController();

	}





	@FXML public void onButtonPowerBot() throws SQLException, ClassNotFoundException, ClientException, ApiException, IOException {
		toggleButtonActive (buttonPowerBot);




		if (!buttonPowerBot.isFocused())
		{
			System.out.print("включено = " + "asdasdasdasdasdasdsda" + "\n");
			pushPowerBot =true;
            recursion();
		}


		else
		{
			System.out.print("выключено = " + "asdasdasdasdasdasdsda" + "\n");
			pushPowerBot =false;
		}

	}


	@FXML private void onLoad()
	{
		toggleButtonActive (buttonLoad);
	}

	//public static BotCardController Controller;



	//private static BotCardController controllers;
	@FXML private void onSettings()
	{
		//BotCardController Controller = BotCardController.fxmlLoader.getController();
		//StageКлассКонтроллер контроллер = StageКласс.getFxmlLoader().getController();
		//Main Controller = Main.getFxmlLoader().getController();

		//BotCardController Controller = new Main.fxmlBotCard.getController();
	//	settext("assssssss");
		//bot = new BotCardController();




	//	Image s = Controller.avatar.getImage();

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
			setButtonActive(button);
		else
			setButtonInactive(button);
	}
	
	private void setButtonActive(Button button)
	{
		button.setId("button-active");
		button.setEffect(Effects.imageButtonActive);
		root.requestFocus();
	}
	
	private void setButtonInactive(Button button)
	{
		button.setId("button");
		button.setEffect(null);
	}


}
