package com.api.client

interface OnConnectedListener {
    fun onCaptchaNeeded(captchaURL : String)
    fun onInvalidData()
    fun onSuccess()
}