package com.api.client

interface OnReceiveDataListener {

    fun onReceiveCaptcha(captcha: String)
    fun onReceiveData(email: String, pass: String)
}