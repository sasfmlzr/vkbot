package com.fomenko.vkbot.controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.api.client.Client;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import javafx.fxml.Initializable;

import static com.api.client.Client.idBot;


public class BotTaskWindowController implements Initializable
{
	final static String resourcePath = "com.fomenko.vkbot.resourcebundle.BotTaskWindow.messages";
	final static String fxmlPath = "/com/fomenko/vkbot/views/BotTaskWindow.fxml";

	public void initialize(URL location, ResourceBundle resources)
	{
	}
	
	void initWindow()
	{
		
	}


	public void zapros() throws Exception {

		TransportClient transportClient = HttpTransportClient.getInstance();
		VkApiClient vk = new VkApiClient(transportClient);

	//	Client client.token;

	String mam = Client.token;
	int ID =idBot;
		System.out.print("id = " + ID + "\n");
		System.out.print("token = " + mam + "\n");

		UserActor actor = new UserActor(ID, mam);


		int a = 10; // Начальное значение диапазона - "от"
		int b = 8000; // Конечное значение диапазона - "до"

	//	int random = Integer(Math.random());

		//UserActor actor = new UserActor(authResponse.getUserId(), client.token);

		vk.messages().send(actor)
				.message( "Как дела?")
				.userId(30562433)
				.randomId(a + (int) (Math.random() * b))
				.execute();







	}


}
