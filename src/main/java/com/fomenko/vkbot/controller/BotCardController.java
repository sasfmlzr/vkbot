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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import static com.fomenko.vkbot.controller.BotTabController.recursion;


class BotCardController extends  AnchorPane implements Initializable
{
	public final static String resourcePath = "com.fomenko.vkbot.resourcebundle.BotCard.messages";
	public final static String fxmlPath = "/com/fomenko/vkbot/views/BotCard.fxml";

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



	//@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	

	@FXML private Button buttonLoad;
    @FXML
	private Button buttonPowerBot;

	@FXML
	private Label nameBot;


	static boolean pushPowerBot=true;

	public void initWindow(){
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

	public void initialize(URL location, ResourceBundle resources)	{
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
			setButtonActive(button);
		else
			setButtonInactive(button);
	}
	
	private void setButtonActive(Button button)
	{
		button.setId("button-active");
		//TODO: разобраться с effects.java (new innershadow)
		button.setEffect(Effects.imageButtonActive);
		root.requestFocus();
	}
	
	private void setButtonInactive(Button button)
	{
		button.setId("button");
		button.setEffect(null);
	}


}
