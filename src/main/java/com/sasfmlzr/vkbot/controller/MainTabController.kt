package com.sasfmlzr.vkbot.controller

import javafx.fxml.Initializable
import javafx.scene.layout.AnchorPane

import java.net.URL
import java.util.ResourceBundle

class MainTabController : AnchorPane(), Initializable {

    //@FXML private AnchorPane root;
    //@FXML private FlowPane flowPane;

    override fun initialize(location: URL, resources: ResourceBundle) {

        //	ResourceBundle bundle = VkBot.loadLocale (Locale.getDefault(), BotCardController.resourcePath);
        /*
		flowPane.prefWidthProperty().bind(root.widthProperty());
		flowPane.prefHeightProperty().bind(root.heightProperty());


		try
		{
			flowPane.getChildren().add(FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add(FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}*/
    }

    companion object {
        //final static String resourcePath = "com.sasfmlzr.vkbot.resourcebundle.maintab.messages";
        val fxmlPath = "/com/sasfmlzr/vkbot/views/MainTab.fxml"
    }
}
