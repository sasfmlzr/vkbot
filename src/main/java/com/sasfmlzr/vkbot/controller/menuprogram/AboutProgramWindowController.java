package com.sasfmlzr.vkbot.controller.menuprogram;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class AboutProgramWindowController implements Initializable
{
	public final static String resourcePath = "com.sasfmlzr.vkbot.resourcebundle.AboutProgramWindow.messages";
	public final static String fxmlPath = "AboutProgramWindow.fxml";
	@FXML private TextArea textLog;


	public void initialize(URL location, ResourceBundle resources)
	{
	}

	public void initWindow() throws IOException {

		textLog.setText("");
		BufferedReader bReader = new BufferedReader(new FileReader("/com/sasfmlzr/vkbot/resourcebundle/AboutProgramWindow/About.txt"));
		System.out.println(bReader);
		String s;
		while((s=bReader.readLine())!=null){
			textLog.appendText(s + "\n");
	//		System.out.println(s);
		}
//////////////о программе берется из файла
	}


}
