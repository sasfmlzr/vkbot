package com.fomenko.vkbot.controller.menuprogram;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;



public class LogWindowController implements Initializable
{
	public final static String resourcePath = "com.fomenko.vkbot.resourcebundle.LogWindow.messages";
	public final static String fxmlPath = "LogWindow.fxml";
    @FXML private TextArea textLog;


	public void initialize(URL location, ResourceBundle resources)
	{
	}

	public void initWindow() throws IOException {

		textLog.setText("");
		BufferedReader bReader = new BufferedReader(new FileReader("src/resources/locale/LogWindow/Log.txt"));
		System.out.println(bReader);
		String s;
		while((s=bReader.readLine())!=null){
			textLog.appendText(s + "\n");
	//		System.out.println(s);
		}
//////////////логи берутся из файла
	}


}
