package com.sasfmlzr.vkbot.controller;

import com.api.client.Client;
import com.api.util.FileSystem;
import com.sasfmlzr.vkbot.VkBot;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class IntroductionWindowController extends Application implements Initializable {

    private LoginWindowPresenter loginWindowPresenter = new LoginWindowPresenter();

    private final static String resourcePath = "com.sasfmlzr.vkbot.resourcebundle.IntroductionWindow.messages";
    private final static String fxmlPath = "/com/sasfmlzr/vkbot/views/IntroductionWindow.fxml";
    @FXML
    private AnchorPane root;
    @FXML
    private GridPane grid;
    @FXML
    Label statusText, loginLabel, passwordLabel;
    @FXML
    TextField login;
    @FXML
    PasswordField password;
    @FXML
    Button btnNext;
    @FXML
    private ImageView captchaImage;
    @FXML
    private TextField captchaKey;
    @FXML
    private ImageView loadingImage;
    @FXML
    private Pane statusPane;


    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle bundle = VkBot.loadLocale(Locale.getDefault(), IntroductionWindowController.resourcePath);
        AnchorPane root = FXMLLoader.load(getClass().getResource(IntroductionWindowController.fxmlPath), bundle);
        Scene scene = new Scene(root);
        scene.setRoot(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        //   primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("VKBot");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void initialize(URL location, ResourceBundle resources) {
        //    this.resources = resources;
        captchaKey.setVisible(false);
        captchaKey.setManaged(false);
        loadingImage.setVisible(false);
        password.setManaged(false);
        password.setVisible(false);
        passwordLabel.setManaged(false);
        passwordLabel.setVisible(false);
        login.setManaged(false);
        login.setVisible(false);
        loginLabel.setVisible(false);
        loginLabel.setManaged(false);
        System.out.println("ssssssssssssssssssssssssssss");

        initClient();
        initClientThread();
        taskStage = loginWindowPresenter.createTaskStage();       // вызов формы при успехе
    }

    private static int stageIntroduction = 0;

    private enum State {
        NONE,
        LOGGING_IN,
        NEED_CAPTCHA,
        INVALID_DATA,
        CONFIRM_PHONE,
        SUCCESS
    }

    private State state = State.NONE;
    private Stage taskStage;
    private Runnable clientRunnable;
    private Thread clientThread;
    private Client client;
    private Boolean phoneConfirmationResult = false;

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
                client.connect(login.getText(), password.getText());
            } catch (IOException ioe) {
                statusText.setText("Возникла ошибка сети");
                showWarningAnimation();
                loadingImage.setVisible(false);

                reloadThread();

                state = State.NONE;
            } catch (InterruptedException ignored) {

            } catch (Exception ex) {
                ex.printStackTrace();

                statusText.setText("Неизвестная ошибка");
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

    public void nextStage() {

        System.out.println(stageIntroduction);

        DropShadow shadow = new DropShadow();
        btnNext.setEffect(shadow);

        switch (stageIntroduction) {
            case 0: {
                statusText.setText("Введите логин и пароль своего аккаунта Вконтакте");
                login.setOpacity(0.6);
                password.setOpacity(0.6);
                loginLabel.setOpacity(1);
                passwordLabel.setOpacity(1);
                password.setManaged(true);
                password.setVisible(true);
                passwordLabel.setManaged(true);
                passwordLabel.setVisible(true);
                login.setManaged(true);
                login.setVisible(true);
                loginLabel.setVisible(true);
                loginLabel.setManaged(true);
                stageIntroduction++;
                break;
            }
            case 1: {
                onLogin();
                //statusText.setText("Неправильный логин или пароль. Попробуйте еще раз.");
                break;
            }
            case 2: {
                password.setManaged(false);
                password.setVisible(false);
                passwordLabel.setManaged(false);
                passwordLabel.setVisible(false);
                login.setManaged(false);
                login.setVisible(false);
                loginLabel.setVisible(false);
                loginLabel.setManaged(false);
                stageIntroduction++;
            }
            case 3: {
                statusText.setText("dd");

                stageIntroduction++;
                break;
            }
            default:
                statusText.setText("fff");
                break;
        }
    }

    // --------При нажатии кнопки далее во ввода логина и пароля-------- //
    @FXML
    private void onLogin() {
        switch (state) {
            case NONE: {
                if (!Objects.equals(login.getText(), "") && !Objects.equals(password.getText(), "")) {
                    hideCaptchaAnimation();
                    hideWarningAnimation();

                    clientThread.start();

                    loadingImage.setVisible(true);

                    state = State.LOGGING_IN;
                }
                break;
            }
            case LOGGING_IN:
            case CONFIRM_PHONE: {
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
                if (!Objects.equals(login.getText(), "") && !Objects.equals(password.getText(), "")) {
                    loginWindowPresenter.getSendData().emit(login.getText(), password.getText());
                    loadingImage.setVisible(true);
                    hideWarningAnimation();
                }
                break;
            }
            case SUCCESS: {
                stageIntroduction++;   // если успешно
                break;
            }
        }
    }

    // ---------------------Если нужна капча---------------------------- //
    private void onCaptchaNeeded(String captchaURL) {
        showCaptchaAnimation();
        hideWarningAnimation();

        loadingImage.setVisible(false);

        captchaKey.clear();
        showCaptchaImage(captchaURL);
        state = State.NEED_CAPTCHA;
    }

    // --------Показать изображение капчи-------- //
    private void showCaptchaImage(String captchaURL) {
        try {
            File captchaImageFile = FileSystem.INSTANCE.downloadCaptchaToFile(captchaURL);
            captchaImage.setImage(new Image(captchaImageFile.toURI().toString(), true));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // --------Если был введен некорректный пароль или логин-------- //
    private void onInvalidData() {
        statusText.setText("Неправильный логин или пароль. Попробуйте еще раз.");
        password.clear();
        captchaKey.clear();
        captchaImage.setImage(null);

        loadingImage.setVisible(false);

        hideCaptchaAnimation();
        showWarningAnimation();

        state = State.INVALID_DATA;
    }

    // --------Для успешного подтверждения телефона-------- //
    private LoginWindowPresenter.OnReceiveBrowserResult onReceiveBrowserResult = isReceive -> phoneConfirmationResult = isReceive;

    // --------Требуется подтвердить номер телефона-------- //
    private void onSuspectLogin(String URL) {
        try {
            statusText.setText("Требуется подтверждение номера телефона.");
            loginWindowPresenter.showBrowserDialog(root.getScene().getWindow(), URL, onReceiveBrowserResult);

            if (phoneConfirmationResult) {
                loginWindowPresenter.getSendPhoneConfirmed().emit();
            } else {
                Platform.runLater(() -> root.getScene().getWindow().hide());
            }
        } catch (Exception ignored) {

        }
    }

    // --------Если успешно подстверждено-------- //
    private void onSuccess() {
        state = State.SUCCESS;

        hideCaptchaAnimation();
        hideWarningAnimation();

        loadingImage.setVisible(false);


        Platform.runLater(() -> {
            taskStage.show();
//            root.getScene().getWindow().hide();
        });
    }

    // --------Анимация-------- //
    private void showWarningAnimation() {
        loginWindowPresenter.showOrHide(grid, true, 0, 45, statusPane);
    }

    // --------Анимация-------- //
    private void showCaptchaAnimation() {
        loginWindowPresenter.showOrHide(grid, true, 3, 111, captchaImage, captchaKey);
    }

    // --------Анимация-------- //
    private void hideWarningAnimation() {
        loginWindowPresenter.showOrHide(grid, false, 0, 45, statusPane);
    }

    // --------Анимация-------- //
    private void hideCaptchaAnimation() {
        loginWindowPresenter.showOrHide(grid, false, 3, 111, captchaImage, captchaKey);
    }
}
