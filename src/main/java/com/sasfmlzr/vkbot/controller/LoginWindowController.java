package com.sasfmlzr.vkbot.controller;

import com.api.client.Client;
import com.api.util.FileSystem;
import com.api.util.sig4j.signal.Signal0;
import com.api.util.sig4j.signal.Signal1;
import com.api.util.sig4j.signal.Signal2;
import com.sasfmlzr.vkbot.VkBot;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class LoginWindowController implements Initializable {
    private final Signal1<String> sendCaptcha = new Signal1<>();
    private final Signal2<String, String> sendData = new Signal2<>();
    private final Signal0 sendPhoneConfirmed = new Signal0();

    private enum State {
        NONE,
        LOGGING_IN,
        NEED_CAPTCHA,
        INVALID_DATA,
        CONFIRM_PHONE,
        SUCCESS
    }

    final static String resourcePath = "com.sasfmlzr.vkbot.resourcebundle.LoginWindow.messages";
    final static String fxmlPath = "/com/sasfmlzr/vkbot/views/LoginWindow.fxml";

    @FXML
    private ResourceBundle resources;
    @FXML
    private AnchorPane root;
    @FXML
    private GridPane grid;

    @FXML
    private Pane statusPane;
    @FXML
    private Text statusText;

    @FXML
    private TextField loginText;
    @FXML
    private PasswordField passText;

    @FXML
    private ImageView captchaImage;
    @FXML
    private TextField captchaKey;

    @FXML
    private ImageView loadingImage;

    private Stage taskStage;

    private Runnable clientRunnable;
    private Thread clientThread;

    private Client client;

    private Boolean phoneConfirmationResult = false;

    private State state = State.NONE;


    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        statusPane.setVisible(false);
        statusPane.setManaged(false);
        captchaKey.setVisible(false);
        captchaKey.setManaged(false);
        captchaImage.setVisible(false);
        captchaImage.setManaged(false);
        loadingImage.setVisible(false);

        grid.getRowConstraints().get(0).setPrefHeight(0);
        grid.getRowConstraints().get(3).setPrefHeight(0);

        initClient();
        initClientThread();
        initTaskStage();
    }

    void initWindow() {
        Scene scene = root.getScene();
        Window window = scene.getWindow();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER: {
                    onLogin();
                    break;
                }
                default:
                    break;
            }
        });

        grid.heightProperty().addListener((observable, oldValue, newValue) -> window.setHeight(newValue.doubleValue() + 35.0));

        window.setOnCloseRequest(we -> {
            client.getOnCaptchaNeeded().clear();
            client.getOnInvalidData().clear();
            client.getOnSuccess().clear();
            client.getOnSuspectLogin().clear();

            sendCaptcha.clear();
            sendData.clear();
            sendPhoneConfirmed.clear();

            clientThread.interrupt();

            window.hide();

        });
    }

    private void initClient() {
        client = new Client();
        client.getOnCaptchaNeeded().connect(this::onCaptchaNeeded);
        client.getOnInvalidData().connect(this::onInvalidData);
        client.getOnSuspectLogin().connect(this::onSuspectLogin);
        client.getOnSuccess().connect(this::onSuccess);

        sendCaptcha.connect(client::receiveCaptcha);
        sendData.connect(client::receiveData);
        sendPhoneConfirmed.connect(client::receivePhoneConfirmed);
    }

    private void initClientThread() {
        clientRunnable = () -> {
            try {
                client.connect(loginText.getText(), passText.getText());
            } catch (IOException ioex) {
                ioex.printStackTrace();
                statusText.setText(resources.getString("LoginWindow.error.connectionProblem"));
                showWarningAnimation();
                loadingImage.setVisible(false);

                reloadThread();

                state = State.NONE;
            } catch (InterruptedException ignored) {

            } catch (Exception ex) {
                ex.printStackTrace();

                statusText.setText(resources.getString("LoginWindow.error.unknownError"));
                showWarningAnimation();
                loadingImage.setVisible(false);

                reloadThread();

                state = State.NONE;
            }
        };

        clientThread = new Thread(clientRunnable);
    }

    private void initTaskStage() {
        try {
            ResourceBundle bundle = VkBot.loadLocale(Locale.getDefault(), BotTaskWindowController.resourcePath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(BotTaskWindowController.fxmlPath), bundle);
            AnchorPane pane = loader.load();

            taskStage = new Stage();

            Scene scene = new Scene(pane);
            scene.setRoot(pane);

            taskStage.setScene(scene);
            taskStage.setResizable(false);

            BotTaskWindowController ctrl = loader.getController();
            ctrl.initWindow();

            taskStage.setTitle("Set api.bot task");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void reloadThread() {
        clientThread.interrupt();
        clientThread = new Thread(clientRunnable);
    }

    @FXML
    private void onLogin() {
        switch (state) {
            case NONE: {
                if (!Objects.equals(loginText.getText(), "") && !Objects.equals(passText.getText(), "")) {
                    hideCaptchaAnimation();
                    hideWarningAnimation();

                    clientThread.start();

                    loadingImage.setVisible(true);
                    state = State.LOGGING_IN;
                }
                break;
            }
            case LOGGING_IN: {
                break;
            }
            case NEED_CAPTCHA: {
                if (!Objects.equals(captchaKey.getText(), "")) {
                    sendCaptcha.emit(captchaKey.getText());
                    loadingImage.setVisible(true);
                    state = State.LOGGING_IN;
                }
                break;
            }
            case INVALID_DATA: {
                if (!Objects.equals(loginText.getText(), "") && !Objects.equals(passText.getText(), "")) {
                    sendData.emit(loginText.getText(), passText.getText());
                    loadingImage.setVisible(true);
                    hideWarningAnimation();
                }
                break;
            }
            case CONFIRM_PHONE: {
                break;
            }
            case SUCCESS: {
                break;
            }
        }
    }


    private void onCaptchaNeeded(String captchaURL) {
        showCaptchaAnimation();
        hideWarningAnimation();

        loadingImage.setVisible(false);

        captchaKey.clear();
        showCaptchaImage(captchaURL);
        state = State.NEED_CAPTCHA;
    }

    private void showCaptchaImage(String captchaURL) {
        try {
            File captchaImageFile = FileSystem.INSTANCE.downloadCaptchaToFile(captchaURL);
            captchaImage.setImage(new Image(captchaImageFile.toURI().toString(), true));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onInvalidData() {
        statusText.setText(resources.getString("LoginWindow.error.invalidData"));
        passText.clear();
        captchaKey.clear();
        captchaImage.setImage(null);

        loadingImage.setVisible(false);

        hideCaptchaAnimation();
        showWarningAnimation();

        state = State.INVALID_DATA;
    }

    private void onSuspectLogin(String URL) {
        try {
            statusText.setText(resources.getString("LoginWindow.error.confirmPhone"));
            showBrowserDialog(URL);

            if (phoneConfirmationResult) {
                sendPhoneConfirmed.emit();
            } else {
                Platform.runLater(() -> root.getScene().getWindow().hide());
            }
        } catch (Exception ignored) {

        }
    }

    private void showBrowserDialog(String URL) throws IOException {
        ResourceBundle bundle = VkBot.loadLocale(Locale.getDefault(), BrowserDialogWindowController.resourcePath);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(BrowserDialogWindowController.fxmlPath), bundle);
        AnchorPane pane;

        pane = loader.load();

        Stage browserStage = new Stage();

        Scene scene = new Scene(pane);
        scene.setRoot(pane);

        browserStage.setScene(scene);
        browserStage.setResizable(false);

        browserStage.initModality(Modality.WINDOW_MODAL);
        browserStage.initOwner(root.getScene().getWindow());

        BrowserDialogWindowController ctrl = loader.getController();
        ctrl.initWindow();
        ctrl.setURL(URL);

        ctrl.sendBrowserResult.connect(this::receiveBrowserResult);

        browserStage.setTitle(bundle.getString("BrowserDialogWindow.title.text"));

        Platform.runLater(browserStage::show);
    }

    private void receiveBrowserResult(Boolean success) {
        phoneConfirmationResult = success;
    }

    private void onSuccess() {
        state = State.SUCCESS;

        hideCaptchaAnimation();
        hideWarningAnimation();

        loadingImage.setVisible(false);


        Platform.runLater(() -> {
            taskStage.show();
            root.getScene().getWindow().hide();
        });
    }


    private void showWarningAnimation() {
        showOrHide(true, 0, 45, statusPane);
    }

    private void showCaptchaAnimation() {
        showOrHide(true, 3, 111, captchaImage, captchaKey);
    }

    private void hideWarningAnimation() {
        showOrHide(false, 0, 45, statusPane);
    }

    private void hideCaptchaAnimation() {
        showOrHide(false, 3, 111, captchaImage, captchaKey);
    }

    private void showOrHide(boolean show, int gridIndex, int maxHeight, Node... nodes) {
        KeyFrame start;
        KeyFrame end;

        boolean expanded = nodes[0].visibleProperty().get();

        if (expanded && !show) {
            start = new KeyFrame(Duration.ZERO, new KeyValue(grid.getRowConstraints().get(gridIndex).prefHeightProperty(), maxHeight));
            end = new KeyFrame(Duration.millis(200), new KeyValue(grid.getRowConstraints().get(gridIndex).prefHeightProperty(), 0));

            for (Node n : nodes) {
                n.setVisible(false);
                n.setManaged(false);
            }
        } else if (!expanded && show) {
            start = new KeyFrame(Duration.ZERO, new KeyValue(grid.getRowConstraints().get(gridIndex).prefHeightProperty(), 0));
            end = new KeyFrame(Duration.millis(200), new KeyValue(grid.getRowConstraints().get(gridIndex).prefHeightProperty(), maxHeight));
        } else {
            start = new KeyFrame(Duration.ZERO, new KeyValue(grid.getRowConstraints().get(gridIndex).prefHeightProperty(), 0));
            end = new KeyFrame(Duration.ZERO, new KeyValue(grid.getRowConstraints().get(gridIndex).prefHeightProperty(), maxHeight));
        }

        Timeline timeline = new Timeline(start, end);

        timeline.setOnFinished(event -> {
            if (!expanded && show)
                for (Node n : nodes) {
                    n.setVisible(true);
                    n.setManaged(true);
                }
        });
        timeline.play();
    }
}
