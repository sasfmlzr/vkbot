package com.sasfmlzr.apivk.functions.bot

import com.sasfmlzr.apivk.`object`.StatisticsVariable.*
import com.sasfmlzr.apivk.actions.Messages
import com.sasfmlzr.apivk.bot.DatabaseEntity
import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import java.sql.SQLException

class Commands(client: BotApiClient) : Messages(client) {

    //-----------------команды бота-----------------------------------------------//
    @Throws(SQLException::class, ClientException::class, ApiException::class)
    fun commandsBot(
        textMessageString: String,
        messages: String,
        actor: UserActor,
        messagesList: List<com.vk.api.sdk.objects.messages.Dialog>,
        bot: BotApiClient
    ): String? {
        var message = messages

        if (textMessageString == "го стих") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                DatabaseEntity.database.stihMessagesData[bot.other().randomId(DatabaseEntity.database.stihMessagesData.size)].response
        }
        if (textMessageString == "го афоризм") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                DatabaseEntity.database.aforismMessagesData[bot.other().randomId(DatabaseEntity.database.aforismMessagesData.size)].response
        }
        if (textMessageString == "го анекдот") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                DatabaseEntity.database.anekdotMessagesData[bot.other().randomId(DatabaseEntity.database.anekdotMessagesData.size)].response
        }
        if (textMessageString == "го статус") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                DatabaseEntity.database.statusMessagesData[bot.other().randomId(DatabaseEntity.database.statusMessagesData.size)].response
        }
        if (textMessageString == "го справку") {
            print("Пришло сообщение = $textMessageString\n")

            message = "го статус - модный статус про машину в вк \n" +
                    "го анекдот - анекдот про хонду \n" +
                    "го афоризм - афоризм про машину в вк \n" +
                    "го стих - стих про машину в вк \n"
        }
        if (textMessageString == "го админ справку") {
            print("Пришло сообщение = $textMessageString\n")

            message = "го средний пинг \n" +
                    "го время работы \n" +
                    "го приостановка бота \n"
        }


        if (textMessageString.contains("добавь")) {

            print("Пришло сообщение = $textMessageString\n")

            message = DatabaseEntity.database.databaseRequest(DatabaseEntity.database.statmt)
                .addToDB(textMessageString, actor.id!!)
        }

        if (textMessageString.contains("го мем")) {

            print("Пришло сообщение = $textMessageString\n")

            bot.messages().vksendImageMessages(actor, messagesList)
            client.stateBot.reduction = true
        }
        if (textMessageString == "го приостановка бота") {
            print("Пришло сообщение = $textMessageString\n")
            message = "Ок, бот приостановлен на минуту "
            client.stateBot.priostanovka = true
        }
        if (textMessageString == "го пинг") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                "Среднее время запроса до вк равно " + Math.round((100 * timeZaprosFinishSumm / countSendMessageUser).toFloat()) / 100 + "мс\n"
        }
        if (textMessageString == "го время работы") {
            print("Пришло сообщение = $textMessageString\n")
            val timeProgramFinish = System.currentTimeMillis()
            val timeProgramItog = timeProgramFinish - timeProgramStart
            message = "Время работы равно " + Math.round((timeProgramItog / 1000).toFloat()) + "c"
        }

        if (message != messages) {
            client.stateBot.findMessage = true
            print("сработали команды \n")
        }
        return message
    }

    //-----------------админские команды бота-----------------------------------------------//
    fun adminCommandsBot(textMessageString: String, messages: String): String {
        var message = messages


        if (textMessageString == "выключись") {
            print("Пришло сообщение = $textMessageString\n")
            message = "Выключаюсь("
            client.stateBot.botStopped = true
        }
        if (textMessageString == "включись") {
            print("Пришло сообщение = $textMessageString\n")
            message = "Смотри скай поехал"
            client.stateBot.botStopped = false
        }

        if (message != messages) {
            client.stateBot.findMessage = true
            print("сработали админ команды \n")
        }
        return message
    }
}
