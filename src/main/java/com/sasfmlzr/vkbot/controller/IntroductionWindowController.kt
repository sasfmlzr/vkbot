package com.sasfmlzr.vkbot.controller

import com.api.client.Client
import com.api.client.OnConnectedListener
import com.api.util.FileSystem
import com.sasfmlzr.vkbot.VkBot
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.stage.Stage
import java.io.IOException
import java.net.URL
import java.util.*

class IntroductionWindowController : Application(), Initializable {

    private val loginWindowPresenter = LoginWindowPresenter()
    @FXML
    private lateinit var root: AnchorPane
    @FXML
    private lateinit var grid: GridPane
    @FXML
    internal lateinit var statusText: Label
    @FXML
    internal lateinit var loginLabel: Label
    @FXML
    internal lateinit var passwordLabel: Label
    @FXML
    internal lateinit var login: TextField
    @FXML
    internal lateinit var password: PasswordField
    @FXML
    internal lateinit var btnNext: Button
    @FXML
    private lateinit var captchaImage: ImageView
    @FXML
    private lateinit var captchaKey: TextField
    @FXML
    private lateinit var loadingImage: ImageView
    @FXML
    private lateinit var statusPane: Pane

    private val onConnectedListener = object : OnConnectedListener {
        override fun onSuccess() {
            state = State.SUCCESS

            hideCaptchaAnimation()
            hideWarningAnimation()

            loadingImage.isVisible = false


            Platform.runLater { taskStage!!.show() }
        }

        override fun onInvalidData() {
            statusText.text = "Неправильный логин или пароль. Попробуйте еще раз."
            password.clear()
            captchaKey.clear()
            captchaImage.image = null

            loadingImage.isVisible = false

            hideCaptchaAnimation()
            showWarningAnimation()

            state = State.INVALID_DATA
        }

        override fun onCaptchaNeeded(captchaURL: String) {
            showCaptchaAnimation()
            hideWarningAnimation()

            loadingImage.isVisible = false

            captchaKey.clear()
            showCaptchaImage(captchaURL)
            state = State.NEED_CAPTCHA
        }
    }

    private var state = State.NONE
    private var taskStage: Stage? = null
    private var clientRunnable: Runnable? = null
    private var clientThread: Thread? = null
    private var client: Client? = null


    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val bundle = VkBot.loadLocale(Locale.getDefault(), IntroductionWindowController.resourcePath)
        val root = FXMLLoader.load<AnchorPane>(javaClass.getResource(IntroductionWindowController.fxmlPath), bundle)
        val scene = Scene(root)
        scene.root = root
        scene.stylesheets.add(javaClass.getResource("application.css").toExternalForm())

        //   primaryStage = new Stage();
        primaryStage.scene = scene
        primaryStage.minWidth = 600.0
        primaryStage.minHeight = 400.0
        primaryStage.title = "VKBot"
        primaryStage.show()

        primaryStage.setOnCloseRequest { event ->
            Platform.exit()
            System.exit(0)
        }
    }

    override fun initialize(location: URL, resources: ResourceBundle) {
        //    this.resources = resources;
        captchaKey.isVisible = false
        captchaKey.isManaged = false
        loadingImage.isVisible = false
        password.isManaged = false
        password.isVisible = false
        passwordLabel.isManaged = false
        passwordLabel.isVisible = false
        login.isManaged = false
        login.isVisible = false
        loginLabel.isVisible = false
        loginLabel.isManaged = false
        println("ssssssssssssssssssssssssssss")

        initClient()
        initClientThread()
        taskStage = loginWindowPresenter.createTaskStage()       // вызов формы при успехе
    }

    private enum class State {
        NONE,
        LOGGING_IN,
        NEED_CAPTCHA,
        INVALID_DATA,
        CONFIRM_PHONE,
        SUCCESS
    }

    private fun initClient() {
        client = Client()
        client!!.onConnectedListener = onConnectedListener
    }

    private fun initClientThread() {
        clientRunnable = Runnable {
            try {
                client!!.connect(login.text, password.text)
            } catch (ioe: IOException) {
                statusText.text = "Возникла ошибка сети"
                showWarningAnimation()
                loadingImage.isVisible = false

                reloadThread()

                state = State.NONE
            } catch (ignored: InterruptedException) {

            } catch (ex: Exception) {
                ex.printStackTrace()

                statusText.text = "Неизвестная ошибка"
                showWarningAnimation()
                loadingImage.isVisible = false
                reloadThread()
                state = State.NONE
            }
        }
        clientThread = Thread(clientRunnable)
    }

    private fun reloadThread() {
        clientThread!!.interrupt()
        clientThread = Thread(clientRunnable)
    }

    fun nextStage() {

        println(stageIntroduction)

        val shadow = DropShadow()
        btnNext.effect = shadow

        when (stageIntroduction) {
            0 -> {
                statusText.text = "Введите логин и пароль своего аккаунта Вконтакте"
                login.opacity = 0.6
                password.opacity = 0.6
                loginLabel.opacity = 1.0
                passwordLabel.opacity = 1.0
                password.isManaged = true
                password.isVisible = true
                passwordLabel.isManaged = true
                passwordLabel.isVisible = true
                login.isManaged = true
                login.isVisible = true
                loginLabel.isVisible = true
                loginLabel.isManaged = true
                stageIntroduction++
            }
            1 -> {
                onLogin()
            }//statusText.setText("Неправильный логин или пароль. Попробуйте еще раз.");
            2 -> {
                password.isManaged = false
                password.isVisible = false
                passwordLabel.isManaged = false
                passwordLabel.isVisible = false
                login.isManaged = false
                login.isVisible = false
                loginLabel.isVisible = false
                loginLabel.isManaged = false
                stageIntroduction++
            }
            3 -> {
                statusText.text = "dd"
                stageIntroduction++
            }
            else -> statusText.text = "fff"
        }
    }

    // --------Показать изображение капчи-------- //
    private fun showCaptchaImage(captchaURL: String) {
        try {
            val captchaImageFile = FileSystem.downloadCaptchaToFile(captchaURL)
            captchaImage.image = Image(captchaImageFile.toURI().toString(), true)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    // --------При нажатии кнопки далее во ввода логина и пароля-------- //
    @FXML
    private fun onLogin() {
        when (state) {
            IntroductionWindowController.State.NONE -> {
                if (login.text != "" && password.text != "") {
                    hideCaptchaAnimation()
                    hideWarningAnimation()

                    clientThread!!.start()

                    loadingImage.isVisible = true

                    state = State.LOGGING_IN
                }
            }
            IntroductionWindowController.State.LOGGING_IN, IntroductionWindowController.State.CONFIRM_PHONE -> {
            }
            IntroductionWindowController.State.NEED_CAPTCHA -> {
                if (captchaKey.text != "") {
                    client!!.onReceiveDataListener.onReceiveCaptcha(captchaKey.text)
                    loadingImage.isVisible = true
                    state = State.LOGGING_IN
                }
            }
            IntroductionWindowController.State.INVALID_DATA -> {
                if (login.text != "" && password.text != "") {
                    client!!.onReceiveDataListener.onReceiveData(login.text, password.text)
                    loadingImage.isVisible = true
                    hideWarningAnimation()
                }
            }
            IntroductionWindowController.State.SUCCESS -> {
                stageIntroduction++   // если успешно
            }
        }
    }

    // --------Анимация-------- //
    private fun showWarningAnimation() {
        loginWindowPresenter.showOrHide(grid, true, 0, 45, statusPane)
    }

    // --------Анимация-------- //
    private fun showCaptchaAnimation() {
        loginWindowPresenter.showOrHide(grid, true, 3, 111, captchaImage, captchaKey)
    }

    // --------Анимация-------- //
    private fun hideWarningAnimation() {
        loginWindowPresenter.showOrHide(grid, false, 0, 45, statusPane)
    }

    // --------Анимация-------- //
    private fun hideCaptchaAnimation() {
        loginWindowPresenter.showOrHide(grid, false, 3, 111, captchaImage, captchaKey)
    }

    companion object {

        private const val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.IntroductionWindow.messages"
        private const val fxmlPath = "/com/sasfmlzr/vkbot/views/IntroductionWindow.fxml"

        private var stageIntroduction = 0
    }
}
