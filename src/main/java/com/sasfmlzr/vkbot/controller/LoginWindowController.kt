package com.sasfmlzr.vkbot.controller

import com.api.client.Client
import com.api.client.OnConnectedListener
import com.api.util.FileSystem
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import javafx.stage.Stage
import java.io.IOException
import java.net.URL
import java.util.*

class LoginWindowController : Initializable {

    private val loginWindowPresenter = LoginWindowPresenter()

    @FXML
    private lateinit var resources: ResourceBundle
    @FXML
    private lateinit var root: AnchorPane
    @FXML
    private lateinit var grid: GridPane

    @FXML
    private lateinit var statusPane: Pane
    @FXML
    private lateinit var statusText: Text

    @FXML
    private lateinit var loginText: TextField
    @FXML
    private lateinit var passText: PasswordField

    @FXML
    private lateinit var captchaImage: ImageView
    @FXML
    private lateinit var captchaKey: TextField

    @FXML
    private lateinit var loadingImage: ImageView

    private var taskStage: Stage? = null

    private var clientRunnable: Runnable? = null
    private var clientThread: Thread? = null

    private var client: Client? = null

    private var state = State.NONE

    private val onConnectedListener = object : OnConnectedListener {
        override fun onSuccess() {
            state = State.SUCCESS

            hideCaptchaAnimation()
            hideWarningAnimation()

            loadingImage.isVisible = false


            Platform.runLater {
                taskStage!!.show()
                root.scene.window.hide()
            }
        }

        override fun onInvalidData() {
            statusText.text = "Неправильный логин или пароль."
            passText.clear()
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

    private enum class State {
        NONE,
        LOGGING_IN,
        NEED_CAPTCHA,
        INVALID_DATA,
        CONFIRM_PHONE,
        SUCCESS
    }

    override fun initialize(location: URL, resources: ResourceBundle) {
        this.resources = resources

        statusPane.isVisible = false
        statusPane.isManaged = false
        captchaKey.isVisible = false
        captchaKey.isManaged = false
        captchaImage.isVisible = false
        captchaImage.isManaged = false
        loadingImage.isVisible = false

        grid.rowConstraints[0].prefHeight = 0.0
        grid.rowConstraints[3].prefHeight = 0.0

        initClient()
        initClientThread()
        taskStage = loginWindowPresenter.createTaskStage()
    }

    internal fun initWindow() {
        val scene = root.scene
        val window = scene.window

        scene.setOnKeyPressed { event ->
            when (event.code) {
                KeyCode.ENTER -> {
                    onLogin()
                }
                else -> {
                }
            }
        }

        grid.heightProperty().addListener { _, _, newValue -> window.height = newValue.toDouble() + 35.0 }

        window.setOnCloseRequest {
            clientThread!!.interrupt()

            window.hide()

        }
    }

    private fun initClient() {
        client = Client()
        client!!.onConnectedListener = onConnectedListener
    }

    private fun initClientThread() {
        clientRunnable = Runnable {
            try {
                client!!.connect(loginText.text, passText.text)
            } catch (ioex: IOException) {
                ioex.printStackTrace()
                statusText.text = resources.getString("LoginWindow.error.connectionProblem")
                showWarningAnimation()
                loadingImage.isVisible = false

                reloadThread()

                state = State.NONE
            } catch (ignored: InterruptedException) {

            } catch (ex: Exception) {
                ex.printStackTrace()

                statusText.text = resources.getString("LoginWindow.error.unknownError")
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

    @FXML
    private fun onLogin() {
        when (state) {
            State.NONE -> {
                if (loginText.text != "" && passText.text != "") {
                    hideCaptchaAnimation()
                    hideWarningAnimation()

                    clientThread!!.start()

                    loadingImage.isVisible = true
                    state = State.LOGGING_IN
                }
            }
            State.LOGGING_IN, State.CONFIRM_PHONE, State.SUCCESS -> {
            }
            State.NEED_CAPTCHA -> {
                if (captchaKey.text != "") {
                    client!!.onReceiveDataListener.onReceiveCaptcha(captchaKey.text)
                    loadingImage.isVisible = true
                    state = State.LOGGING_IN
                }
            }
            State.INVALID_DATA -> {
                if (loginText.text != "" && passText.text != "") {
                    client!!.onReceiveDataListener.onReceiveData(loginText.text, passText.text)
                    loadingImage.isVisible = true
                    hideWarningAnimation()
                }
            }
        }
    }

    private fun showCaptchaImage(captchaURL: String) {
        try {
            val captchaImageFile = FileSystem.downloadCaptchaToFile(captchaURL)
            captchaImage.image = Image(captchaImageFile.toURI().toString(), true)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun showWarningAnimation() {
        loginWindowPresenter.showOrHide(grid, true, 0, 45, statusPane)
    }

    private fun showCaptchaAnimation() {
        loginWindowPresenter.showOrHide(grid, true, 3, 111, captchaImage, captchaKey)
    }

    private fun hideWarningAnimation() {
        loginWindowPresenter.showOrHide(grid, false, 0, 45, statusPane)
    }

    private fun hideCaptchaAnimation() {
        loginWindowPresenter.showOrHide(grid, false, 3, 111, captchaImage, captchaKey)
    }

    companion object {

        internal const val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.LoginWindow.messages"
        internal const val fxmlPath = "/com/sasfmlzr/vkbot/views/LoginWindow.fxml"
    }
}
