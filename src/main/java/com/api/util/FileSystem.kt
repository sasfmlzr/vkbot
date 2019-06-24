package com.api.util

import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.util.*

object FileSystem {
    private const val tempCaptchaPath = "/cache/temp_captcha/"

    fun downloadCaptchaToFile(captchaURL: String): File {
        val captchaImageFile = File(tempCaptchaPath + "/" + UUID.nameUUIDFromBytes(captchaURL.toByteArray()).toString())
        FileUtils.copyURLToFile(URL(captchaURL), captchaImageFile)
        return captchaImageFile
    }
}
