package com.sasfmlzr.apivk.thread

import com.newapi.apivk.architecture.db.DatabaseConnection
import com.newapi.apivk.architecture.storage.DatabaseStorage
import com.sasfmlzr.apivk.`object`.StatisticsVariable
import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.enums.MessagesFilter
import com.vk.api.sdk.objects.messages.ConversationWithMessage
import java.util.*

class ThreadGroupBot(private val client: BotApiClient, private val actor: GroupActor) : ThreadBot(), Runnable {
    private var stoped = false

    private val databaseStorage = DatabaseStorage.getInstance()

    override fun run() {
        while (!stoped) {
            var exception = false
            try {
                sendMessageUser(actor)
            } catch (e: Exception) {
                exception = true
                e.printStackTrace()
                println("Исключение в потоке бота")
            }

            if (!exception) {
                stopped()
            } else {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

        }
        println("Побочный поток завершён")
    }

    private fun stopped() {
        stoped = true
    }

    //-----------------отправка сообщения, если есть непрочитанные-----------------//
    @Throws(ClientException::class, ApiException::class, InterruptedException::class)
    fun sendMessageUser(actor: GroupActor) {

        while (client.stateBot.pushPowerBot) {
            client.stateBot.findMessage = false        // совпадение с сообщением не найдено
            StatisticsVariable.countSendMessageUser = StatisticsVariable.countSendMessageUser + 1

            client.stateBot.botWork = true           // если метод запущен, то бот включен
            client.stateBot.priostanovka = false   // для приостановки бота
            var message: String         // сообщение бота

            message =
                    databaseStorage.botRandomData[client.other().randomId(databaseStorage.botRandomData.size)]
                            .response          //сообщение берется из рандомной базы коляна

            val messagesList = createListMessageVK()                   // делается запрос непрочитанных сообщений

            if (!client.stateBot.botStopped) {

                if (messagesList.isNotEmpty()) {                                             // если нет непрочитанных, сообщений, то не выполнять
                    val textMessageString =
                            messagesList[0].lastMessage.text.toLowerCase()       // прием сообщения в переменную              TEST


                    client.stateBot.reduction = false

                    if (!client.stateBot.reduction) {
                        message = messageFromBigDataBase(textMessageString, message)
                        client.messages().vkSendMessage(
                                actor,
                                message,
                                messagesList
                        )       // отправка сообщения и пометка его прочитанным
                    } else {
                        Thread.sleep(500)
                        print("Нет новых сообщений" + "\n")
                    }
                }

                delayThread(messagesList, client)          // поток засыпает

            } else {
                Thread.sleep(2000)
            }
        }

        //WTF
        DatabaseConnection.getInstance().disconnect()        //закрытие бд
        client.stateBot.botWork = false
    }

    //-----------------отправка и отслеживание запроса в вк на непрочитанные сообщения-------------------------------//
    @Throws(ClientException::class, ApiException::class)
    private fun createListMessageVK(): List<ConversationWithMessage> {
        val timezaprosstart = System.currentTimeMillis()         // начало запроса непрочитанного запроса

        //client.getVkApiClient().messages().send(actor).userId(30562433).randomId(234).message("fdffgf").execute();
        val messages = client.vk.messages().getConversations(actor)                 // Листы сообщений
                .filter(MessagesFilter.UNANSWERED)
                .execute().items
        val timezaprosfinish = System.currentTimeMillis()
        StatisticsVariable.timeZaprosFinishItogo =
                timezaprosfinish - timezaprosstart      // время, затраченное на операцию
        return messages
    }

    //-----------------поиск сообщения в основной БД----------------------------------------------//
    private fun messageFromDataBase(
            textMessageString: String,
            message: String
    ): String {           //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        var messages = message
        val listMessages = ArrayList<String>()
        if (!client.stateBot.findMessage) {      // если совпадение с сообщением не найдено, то
            val timeStartBD = System.currentTimeMillis()
            // путешествие по списку объектов из БД
            var countDB = 0
            while (countDB <= databaseStorage.botData.size - 1) {
                if (databaseStorage.botData[countDB].request.toLowerCase() == textMessageString.toLowerCase()) {  // сравниваем нижний регистр
                    listMessages.add(databaseStorage.botData[countDB].response)
                    client.stateBot.findMessage =
                            true                                                       // совпадение с сообщением найдено
                }
                countDB += 1
            }
            if (client.stateBot.findMessage)
            // если совпадение с сообщением найдено, то
                messages =
                        listMessages[client.other().randomId(listMessages.size)]    // выбираем рандомно из найденного сообщение
            val timeFinishBD = System.currentTimeMillis()
            StatisticsVariable.timeConsumedMillisBD = timeFinishBD - timeStartBD
            StatisticsVariable.countUsedBD =
                    StatisticsVariable.countUsedBD + 1                                              // количество использований бд коляна увеличилось на 1
            //        System.out.print("время операции по бд коляна= "+ timeConsumedMillisBD + "\n");
        }
        return messages
    }

    //-----------------поиск сообщения в большой БД-----------------------------------------------//
    private fun messageFromBigDataBase(
            textMessageString: String,
            message: String
    ): String {       //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        var messages = message
        val listMessages = ArrayList<String>()
        if (!client.stateBot.findMessage) {      // если совпадение с сообщением не найдено, то

            var countDB = 0
            while (countDB <= databaseStorage.bigMessagesData.size - 1) {
                if (databaseStorage.bigMessagesData[countDB].request.toLowerCase() == textMessageString) {  // сравниваем нижний регистр
                    // сравниваем сообщение и значение в БД
                    listMessages.add(databaseStorage.bigMessagesData[countDB].response)
                    client.stateBot.findMessage =
                            true                                                   // совпадение с сообщением найдено
                    // messages = listMessages.get(randomIdBot(listMessages.size()));    // выбираем рандомно из найденного сообщение
                }
                countDB += 1
            }
            if (client.stateBot.findMessage)
            // если совпадение с сообщением найдено, то
            {
                messages =
                        listMessages[client.other().randomId(listMessages.size)]          // выбираем рандомно из найденного сообщение
            }
        }
        return messages
    }
}
