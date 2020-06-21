package com.sasfmlzr.apivk.functions.bot

import com.database.DatabaseEntity
import com.sasfmlzr.apivk.`object`.StatisticsVariable.countSendMessageUser
import com.sasfmlzr.apivk.`object`.StatisticsVariable.timeProgramStart
import com.sasfmlzr.apivk.`object`.StatisticsVariable.timeZaprosFinishSumm
import com.sasfmlzr.apivk.actions.Messages
import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.ConversationWithMessage
import java.sql.SQLException
import kotlin.math.roundToLong

class Commands(client: BotApiClient) : Messages(client) {

    //-----------------команды бота-----------------------------------------------//
    @Throws(SQLException::class, ClientException::class, ApiException::class)
    fun commandsBot(
            textMessageString: String,
            messages: String,
            actor: UserActor,
            messagesList: List<ConversationWithMessage>,
            bot: BotApiClient
    ): String? {
        var message = messages

        if (textMessageString == "стих") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                    DatabaseEntity.database.stihMessagesData[bot.other().randomId(DatabaseEntity.database.stihMessagesData.size)].response
        }
        if (textMessageString == "афоризм") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                    DatabaseEntity.database.aforismMessagesData[bot.other().randomId(DatabaseEntity.database.aforismMessagesData.size)].response
        }
        if (textMessageString == "анекдот") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                    DatabaseEntity.database.anekdotMessagesData[bot.other().randomId(DatabaseEntity.database.anekdotMessagesData.size)].response
        }
        if (textMessageString == "статус") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                    DatabaseEntity.database.statusMessagesData[bot.other().randomId(DatabaseEntity.database.statusMessagesData.size)].response
        }
        if (textMessageString == "справку") {
            print("Пришло сообщение = $textMessageString\n")

            message = "статус\n" +
                    "анекдот\n" +
                    "афоризм\n" +
                    "стих\n"
        }
        if (textMessageString == "админ справка") {
            print("Пришло сообщение = $textMessageString\n")

            message = "средний пинг \n" +
                    "время работы \n" +
                    "приостановка бота \n"
        }


        if (textMessageString.contains("добавь")) {

            print("Пришло сообщение = $textMessageString\n")

            message = DatabaseEntity.database.databaseRequest
                    .addToDB(textMessageString, actor.id!!)
        }

        if (textMessageString.contains("мем")) {

            print("Пришло сообщение = $textMessageString\n")

            bot.messages().vksendImageMessages(actor, messagesList)
            client.stateBot.reduction = true
        }
        if (textMessageString == "приостановка бота") {
            print("Пришло сообщение = $textMessageString\n")
            message = "Ок, бот приостановлен на минуту "
            client.stateBot.priostanovka = true
        }
        if (textMessageString == "пинг") {
            print("Пришло сообщение = $textMessageString\n")
            message =
                    "Среднее время запроса до вк равно " + (100 * timeZaprosFinishSumm / countSendMessageUser).toFloat().roundToLong() / 100 + "мс\n"
        }
        if (textMessageString == "время работы") {
            print("Пришло сообщение = $textMessageString\n")
            val timeProgramFinish = System.currentTimeMillis()
            val timeProgramItog = timeProgramFinish - timeProgramStart
            message = "Время работы равно " + (timeProgramItog / 1000).toFloat().roundToLong() + "c"
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
            message = "Чат-бот включен"
            client.stateBot.botStopped = false
        }

        if (message != messages) {
            client.stateBot.findMessage = true
            print("сработали админ команды \n")
        }
        return message
    }
}
