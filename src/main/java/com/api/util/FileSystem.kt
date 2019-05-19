package com.api.util

import org.apache.commons.io.FileUtils

import java.io.File
import java.io.IOException
import java.net.URL
import java.util.UUID

object FileSystem {
    private val tempCaptchaPath = "/cache/temp_captcha/"
    @Throws(IOException::class)
    fun downloadCaptchaToFile(captchaURL: String): File {
        val captchaImageFile = File(tempCaptchaPath + "/" + UUID.nameUUIDFromBytes(captchaURL.toByteArray()).toString())
        FileUtils.copyURLToFile(URL(captchaURL), captchaImageFile)
        return captchaImageFile
    }
}
