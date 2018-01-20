package com.fomenko.vkbot.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Window;
import com.api.util.sig4j.signal.Signal1;

public class BrowserDialogWindowController implements Initializable
{
	final Signal1<Boolean> sendBrowserResult = new Signal1<>();
	
	final static String resourcePath = "com.fomenko.vkbot.resourcebundle.BrowserDialogWindow.messages";
	final static String fxmlPath = "/com/fomenko/vkbot/views/BrowserDialogWindow.fxml";

	@FXML private AnchorPane root;
	@FXML private WebView webView;

	private WebEngine engine;

	public void initialize(URL location, ResourceBundle resources)
	{
		this.engine = webView.getEngine();
	}
	
	void initWindow()
	{
		Scene scene = root.getScene();
		Window window = scene.getWindow();
		
		window.setOnCloseRequest(Event::consume);
		
		this.engine.locationProperty().addListener((prop, before, after) -> {
            //true if OK
            Platform.runLater(this::close);
       });
	}

	public void setURL (String URL)
	{
		this.engine.load(URL);
	}
	
	@FXML private void onCancel()
	{
		sendBrowserResult.emit(false);
		close();
	}
	
	private void close()
	{
		root.getScene().getWindow().hide();
		sendBrowserResult.clear();
	}
}
