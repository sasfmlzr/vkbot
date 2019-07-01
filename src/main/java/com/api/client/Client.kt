package com.api.client

import com.api.client.ClientUtils.getAttributeOfElement
import com.api.client.ClientUtils.hasAttributeValue
import com.vk.api.sdk.client.actors.UserActor
import org.apache.commons.io.IOUtils
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import java.io.*
import javax.swing.text.html.HTML
import javax.swing.text.html.HTMLDocument
import javax.swing.text.html.HTMLEditorKit

class Client : Serializable {
    @Transient
    private var httpClient: CloseableHttpClient? = null

    lateinit var onConnectedListener: OnConnectedListener

    private val ID = "5988198"
    private val scope = "friends,photos,audio,video,docs,status,wall,messages,offline"
    private val redirectUri = "https://oauth.vk.com/blank.html"
    private val display = "popup"
    private val responseType = "token"
    private var login = ""
    private var pass = ""
    private var ip_h = ""
    private var lg_h = ""
    private var to = ""
    private var captchaSid = ""
    private var captchaKey = ""
    private var captchaURL = ""
    @Transient
    private var response: CloseableHttpResponse? = null
    private var stringResponse: String? = null

    val onReceiveDataListener =  object: OnReceiveDataListener{
        override fun onReceiveCaptcha(captcha: String) {
            this@Client.captchaKey = captcha
        }

        override fun onReceiveData(email: String, pass: String) {
            this@Client.login = email
            this@Client.pass = pass
        }
    }

    init {
        buildClient()
    }

    private fun buildClient() {
        val globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()
        val cookieStore = BasicCookieStore()
        val context = HttpClientContext.create()
        context.cookieStore = cookieStore
        httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build()
    }

    @Throws(Exception::class)
    fun connect(email: String, pass: String) {

        this.login = email
        this.pass = pass
        authorize()
        login()

        while (!response!!.containsHeader("location") && token == "")
            handleProblem()

        if (token == "")
            getToken()
        mam = Client.token
        idBot = Integer.parseInt(this.ID)
        println("Авторизация успешна")
        actor = UserActor(idBot, mam)

        onConnectedListener.onSuccess()
    }

    @Throws(Exception::class)
    private fun authorize() {
        val post = "https://oauth.vk.com/authorize?" +
                "client_id=" + ID +
                "&redirect_uri=" + redirectUri +
                "&display=" + display +
                "&scope=" + scope +
                "&response_type=" + responseType

        val doc = stringToHtml(postQuery(post))

        ip_h = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "ip_h", HTML.Attribute.VALUE)
        lg_h = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "lg_h", HTML.Attribute.VALUE)
        to = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "to", HTML.Attribute.VALUE)
    }

    @Throws(Exception::class)
    private fun stringToHtml(string: String?): HTMLDocument {
        val kit = HTMLEditorKit()
        val doc = kit.createDefaultDocument() as HTMLDocument
        doc.putProperty("IgnoreCharsetDirective", java.lang.Boolean.TRUE)
        val HTMLReader = StringReader(string!!)
        kit.read(HTMLReader, doc, 0)
        return doc
    }

    @Throws(Exception::class)
    private fun login() {
        val post = "https://login.vk.com/?act=login&soft=1" +
                "&q=1" +
                "&ip_h=" + ip_h +
                "&lg_h=" + lg_h +
                "&_origin=https://oauth.vk.com" +
                "&to=" + to +
                "&expire=0" +
                "&email=" + login +
                "&pass=" + pass

        postQuery(post)
        postQuery(response!!.getFirstHeader("location").value)
    }

    @Throws(Exception::class)
    private fun handleProblem() {
        val doc = stringToHtml(stringResponse)

        if (hasAttributeValue(doc, "input", HTML.Attribute.NAME, "captcha_sid"))
            handleCaptcha(doc)
        else if (hasAttributeValue(doc, "div", HTML.Attribute.CLASS, "oauth_access_header"))
            handleConfirmApplicationRights(doc)
        else
            handleInvalidData(doc)
    }

    @Throws(Exception::class)
    private fun handleCaptcha(doc: HTMLDocument) {
        getCaptcha(doc)

        val file = File("file.jpg")

        captchaKey = ""
        onConnectedListener.onCaptchaNeeded(captchaURL)

        while (captchaKey == "") {
            Thread.sleep(1)
        }

        sendCaptcha()

        file.delete()
    }

    private fun getCaptcha(doc: HTMLDocument) {
        captchaURL = getAttributeOfElement(doc, "img", HTML.Attribute.CLASS, "captcha_img", HTML.Attribute.SRC)
        captchaSid = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "captcha_sid", HTML.Attribute.VALUE)
        lg_h = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "lg_h", HTML.Attribute.VALUE)
    }

    @Throws(Exception::class)
    private fun sendCaptcha() {
        val post = "https://login.vk.com/?act=login&soft=1" +
                "&q=1" +
                "&ip_h=" + ip_h +
                "&lg_h=" + lg_h +
                "&_origin=https://oauth.vk.com" +
                "&to=" + to +
                "&expire=0" +
                "&email=" + login +
                "&pass=" + pass +
                "&captcha_sid=" + captchaSid +
                "&captcha_key=" + captchaKey

        postQuery(post)
        postQuery(response!!.getFirstHeader("location").value)
    }

    @Throws(Exception::class)
    private fun handleInvalidData(doc: HTMLDocument) {
        lg_h = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "lg_h", HTML.Attribute.VALUE)

        login = ""
        pass = ""
        onConnectedListener.onInvalidData()

        while (login == "" || pass == "") {
            Thread.sleep(1)
        }

        login()
    }

    @Throws(Exception::class)
    private fun handleConfirmApplicationRights(doc: HTMLDocument) {
        val name = getAttributeOfElement(doc, "form", HTML.Attribute.ACTION)
        postQuery(name)
        getTokenFirstTime()
    }

    private fun getTokenFirstTime() {
        val headerLocation = response!!.getFirstHeader("location").value
        token = headerLocation.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    }

    @Throws(Exception::class)
    private fun getToken() {
        postQuery(response!!.getFirstHeader("location").value)

        val headerLocation = response!!.getFirstHeader("location").value
        token = headerLocation.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    }

    @Throws(Exception::class)
    private fun postQuery(query: String): String? {
        //	while (!scheduler.canPostQuery());

        val post = HttpPost(query)

        response = httpClient!!.execute(post)
        stringResponse = IOUtils.toString(response!!.entity.content, "UTF-8")
        post.reset()

        return stringResponse
    }

    @Throws(IOException::class)
    private fun writeObject(oos: ObjectOutputStream) {
        oos.defaultWriteObject()
    }

    @Throws(ClassNotFoundException::class, IOException::class)
    private fun readObject(ois: ObjectInputStream) {
        ois.defaultReadObject()

        buildClient()
    }

    companion object {
        private const val serialVersionUID = 5840493055914657790L
        var token = ""

        /**
         * TODO: здесь требуется оптимизировать код(разобраться с переменными)
         */
        private var mam: String? = null
        var idBot: Int = 0
        lateinit var actor: UserActor
    }
}