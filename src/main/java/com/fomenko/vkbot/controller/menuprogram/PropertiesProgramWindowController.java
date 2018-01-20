package com.fomenko.vkbot.controller.menuprogram;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;


public class PropertiesProgramWindowController implements Initializable
{
	public final static String resourcePath = "com.fomenko.vkbot.resourcebundle.PropertiesProgramWindow.messages";
	public final static String fxmlPath = "PropertiesProgramWindow.fxml";
	@FXML private TextArea textLog;

	public static String token1,token2,token3,token4;		// токены из ini для ботов
	public static String userId1,userId2,userId3,userId4;	// userid из ini для ботов
	public static String mode1,mode2;	// userid из ini для ботов
	public void initialize(URL location, ResourceBundle resources)
	{




	}

	public void initWindow() throws IOException {

		textLog.setText("");
		/*
		BufferedReader bReader = new BufferedReader(new FileReader("src/resources/locale/PropertiesProgramWindow/Properties.ini"));
		System.out.println(bReader);
		String s;
		while((s=bReader.readLine())!=null){
			textLog.appendText(s + "\n");
	//		System.out.println(s);
		}
		*/
//////////////логи берутся из файла









	}


}
