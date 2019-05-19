package com.sasfmlzr.apivk.`object`

import java.util.Objects

class UserRights(val nameRight: String) {

    var allowWriteToBot: Boolean? = false
        private set        // разрешение писать боту
    var adminCommands: Boolean? = false
        private set          // разрешение на админские команды


    init {
        if (nameRight == "Бог") {
            this.adminCommands = true
            this.allowWriteToBot = true
        }
        if (nameRight == "Пользователь") {
            this.adminCommands = false
            this.allowWriteToBot = true
        }
        if (nameRight == "Колян") {
            this.adminCommands = false
            this.allowWriteToBot = false
        }
    }
}
