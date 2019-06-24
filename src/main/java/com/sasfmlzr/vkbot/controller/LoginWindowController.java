package com.sasfmlzr.vkbot.controller;

import com.api.client.Client;
import com.api.util.FileSystem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class LoginWindowController implements Initializable {

    private LoginWindowPresenter loginWindowPresenter = new LoginWindowPresenter();

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
        taskStage = loginWindowPresenter.createTaskStage();
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

            loginWindowPresenter.getSendCaptcha().clear();
            loginWindowPresenter.getSendData().clear();
            loginWindowPresenter.getSendPhoneConfirmed().clear();

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

        loginWindowPresenter.getSendCaptcha().connect(client::receiveCaptcha);
        loginWindowPresenter.getSendData().connect(client::receiveData);
        loginWindowPresenter.getSendPhoneConfirmed().connect(client::receivePhoneConfirmed);
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
            case LOGGING_IN:
            case CONFIRM_PHONE:
            case SUCCESS: {
                break;
            }
            case NEED_CAPTCHA: {
                if (!Objects.equals(captchaKey.getText(), "")) {
                    loginWindowPresenter.getSendCaptcha().emit(captchaKey.getText());
                    loadingImage.setVisible(true);
                    state = State.LOGGING_IN;
                }
                break;
            }
            case INVALID_DATA: {
                if (!Objects.equals(loginText.getText(), "") && !Objects.equals(passText.getText(), "")) {
                    loginWindowPresenter.getSendData().emit(loginText.getText(), passText.getText());
                    loadingImage.setVisible(true);
                    hideWarningAnimation();
                }
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
        statusText.setText("Неправильный логин или пароль.");
        passText.clear();
        captchaKey.clear();
        captchaImage.setImage(null);

        loadingImage.setVisible(false);

        hideCaptchaAnimation();
        showWarningAnimation();

        state = State.INVALID_DATA;
    }

    // --------Для успешного подтверждения телефона-------- //
    private LoginWindowPresenter.OnReceiveBrowserResult onReceiveBrowserResult = isReceive -> phoneConfirmationResult = isReceive;

    private void onSuspectLogin(String URL) {
        try {
            statusText.setText(resources.getString("LoginWindow.error.confirmPhone"));
            loginWindowPresenter.showBrowserDialog(root.getScene().getWindow(), URL, onReceiveBrowserResult);

            if (phoneConfirmationResult) {
                loginWindowPresenter.getSendPhoneConfirmed().emit();
            } else {
                Platform.runLater(() -> root.getScene().getWindow().hide());
            }
        } catch (Exception ignored) {

        }
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
        loginWindowPresenter.showOrHide(grid, true, 0, 45, statusPane);
    }

    private void showCaptchaAnimation() {
        loginWindowPresenter.showOrHide(grid, true, 3, 111, captchaImage, captchaKey);
    }

    private void hideWarningAnimation() {
        loginWindowPresenter.showOrHide(grid, false, 0, 45, statusPane);
    }

    private void hideCaptchaAnimation() {
        loginWindowPresenter.showOrHide(grid, false, 3, 111, captchaImage, captchaKey);
    }
}
