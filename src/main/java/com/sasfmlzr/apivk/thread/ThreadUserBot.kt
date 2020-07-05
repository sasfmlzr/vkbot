package com.sasfmlzr.apivk.thread

import com.database.DatabaseEntity
import com.sasfmlzr.apivk.`object`.StatisticsVariable
import com.sasfmlzr.apivk.`object`.StatisticsVariable.countSendMessageUser
import com.sasfmlzr.apivk.`object`.StatisticsVariable.countUsedBigBD
import com.sasfmlzr.apivk.`object`.StatisticsVariable.timeConsumedMillisBigBD
import com.sasfmlzr.apivk.`object`.StatisticsVariable.timeDelayThread
import com.sasfmlzr.apivk.`object`.StatisticsVariable.timeItogoMsMinusVK
import com.sasfmlzr.apivk.`object`.StatisticsVariable.timeZaprosFinishItogo
import com.sasfmlzr.apivk.`object`.UserRights
import com.sasfmlzr.apivk.client.BotApiClient
import com.sasfmlzr.vkbot.controller.menuprogram.StatisticsWindowController
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.enums.MessagesFilter
import com.vk.api.sdk.objects.messages.ConversationWithMessage
import javafx.scene.chart.XYChart
import java.sql.SQLException
import java.util.*

class ThreadUserBot(private val client: BotApiClient, private val actor: UserActor) : ThreadBot(),
        Runnable        //(содержащее метод run())          отправление сообщения в рекурсии в отдельном потоке
{

    private var stoped = false

    override fun run()         //Этот метод будет выполняться в побочном потоке
    {
        while (!stoped) {
            var exception = false
            try {
                sendMessageUser(actor)
            } catch (e: ClientException) {
                exception = true
                e.printStackTrace()
                print("Исключение в потоке бота \n")
            } catch (e: SQLException) {
                exception = true
                e.printStackTrace()
                print("Исключение в потоке бота \n")
            } catch (e: InterruptedException) {
                exception = true
                e.printStackTrace()
                print("Исключение в потоке бота \n")
            } catch (e: ApiException) {
                exception = true
                e.printStackTrace()
                print("Исключение в потоке бота \n")
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
        print("Побочный поток завершён \n")
    }

    private fun stopped() {
        stoped = true
    }

    //-----------------отправка сообщения, если есть непрочитанные-----------------//
    @Throws(ClientException::class, ApiException::class, InterruptedException::class, SQLException::class)
    private fun sendMessageUser(actor: UserActor) {

        while (client.stateBot.pushPowerBot) {
            client.stateBot.findMessage = false        // совпадение с сообщением не найдено
            countSendMessageUser = countSendMessageUser + 1
            val timeStartFunction = System.currentTimeMillis()

            client.stateBot.botWork = true           // если метод запущен, то бот включен
            client.stateBot.priostanovka = false   // для приостановки бота
            var message: String?         // сообщение бота
            var obrachenie = "Колян, "                      //обращение к боту
            message = DatabaseEntity.database.botRandomData[client.other().randomId(DatabaseEntity.database.botRandomData.size)]
                    .response          //сообщение берется из рандомной базы коляна
            /*
           if (textMessageString.equals("")){
               System.out.print("textMessageString = " +textMessageString+ "\n");
               message = "Я Бот Колян, я выпил Блэйзера и не могу отвечать на пустые сообщения(в которых только вложения)";
           }*/
            val messagesList = createListMessageVK()                   // делается запрос непрочитанных сообщений
            obrachenie = "Колян, "
            StatisticsVariable.timeZaprosFinishSumm =
                    StatisticsVariable.timeZaprosFinishSumm + timeZaprosFinishItogo            // для среднего пинга

            val userRight: UserRights

            if (messagesList.size != 0) {
                val userID = messagesList[0].lastMessage.peerId!!               // запись userID пользователя
                DatabaseEntity.database.databaseRequest.addInfoUser(
                        userID,
                        actor,
                        client.vk
                )       // добавить инфу о пользователе, если нет  // здесь есть запрос к вк
                DatabaseEntity.database.databaseRequest
                        .addInfoUserRights(userID, actor)    // добавить права пользователю, если нет
                val userRightString =
                        DatabaseEntity.database.databaseRequest
                                .findUserRights(userID, actor)  // запись права текущего пользователя в ячейку
                userRight = UserRights(userRightString)  // наследование прав пользователем
                //    System.out.print(userRight.getNameRight() + " может админить? -" + userRight.getAdminCommands() + "\n" +
                //           userRight.getNameRight() + " может писать боту? -" + userRight.getAllowWriteToBot() + "\n");


                var textMessageString = messagesList[0].lastMessage.text.toLowerCase()       // прием сообщения в переменную
                if (userRight.adminCommands!!) {
                    if (!client.stateBot.botStopped) {     // если совпадение с сообщением не найдено, то
                        if (textMessageString.contains(obrachenie.toLowerCase())) {

                            textMessageString = textMessageString.replace(obrachenie.toLowerCase().toRegex(), "")
                            textMessageString =
                                    textMessageString.replace("[^ A-Za-zА-Яа-я0-9?]".toRegex(), "")       // замена знаков
                            message = client.messages().commands()
                                    .adminCommandsBot(textMessageString, message)         //проверка на команды бота
                            if (message != message) {
                                client.messages().vkSendMessage(actor, message, messagesList)
                                messagesList.clear()
                            }
                        }
                    }
                }
            }

            if (!client.stateBot.botStopped) {

                if (messagesList.size != 0) {                                             // если нет непрочитанных, сообщений, то не выполнять
                    var textMessageString =
                            messagesList[0].lastMessage.text.toLowerCase()       // прием сообщения в переменную              TEST

                    //    statmt.execute("SELECT 'login','userID','UserRights' FROM 'UserRights' WHERE login="+actor.getId()+" AND userID="+userID);
                    client.stateBot.reduction = false
                    if (!client.stateBot.findMessage) {     // если совпадение с сообщением не найдено, то
                        if (textMessageString.contains(obrachenie.toLowerCase())) {
                            textMessageString = textMessageString.replace(obrachenie.toLowerCase().toRegex(), "")
                            message = client.messages().commands().commandsBot(
                                    textMessageString,
                                    message,
                                    actor,
                                    messagesList,
                                    client
                            )         //проверка на команды бота
                            textMessageString =
                                    textMessageString.replace("[^ A-Za-zА-Яа-я0-9?]".toRegex(), "")       // замена знаков
                        }
                    }
                    if (!client.stateBot.reduction) {
                        message = messageFromDataBase(textMessageString, message)       // бд коляна
                        message = messageFromBigDataBase(textMessageString, message!!)    // большая бд

                        if(textMessageString.contains("Красная") || textMessageString.contains("Белый") ||
                                textMessageString.contains("Красный") || textMessageString.contains("Белая")||
                                textMessageString.contains("Красн") || textMessageString.contains("Бел")||
                                textMessageString.contains("красн") || textMessageString.contains("бел")) {
                            message = textMessageString.toUpperCase() + "! " + textMessageString.toUpperCase() + "! "+ textMessageString.toUpperCase() + "!"
                        }

                        client.messages().vkSendMessage(
                                actor,
                                message,
                                messagesList
                        )       // отправка сообщения и пометка его прочитанным
                    } else {
                        Thread.sleep(500)
                        //            System.out.print("Нет новых сообщений" + "\n");
                    }


                    val timeFinishFunction = System.currentTimeMillis()
                    val timeConsumedMilliss = timeFinishFunction - timeStartFunction
                    //            System.out.print("время countSendMessageUser= " + timeConsumedMilliss + "\n");
                    timeItogoMsMinusVK =
                            timeConsumedMilliss - timeZaprosFinishItogo
                    //            System.out.print("время прохода минус запросвк= " + timeItogoMsMinusVK + "\n");
                }
                if (client.stateBot.priostanovka) {
                    //                System.out.print("бот приостановлен \n");
                    Thread.sleep(60000)
                }
                delayThread(messagesList, client)          // поток засыпает

            } else {
                Thread.sleep(2000)
            }

            /////////////////////////статистика
            StatisticsWindowController.seriesZaprosVk.data.add(XYChart.Data(countSendMessageUser, timeZaprosFinishItogo))          //ведение статистики запросов
            StatisticsWindowController.seriesItogVk.data.add(XYChart.Data(countSendMessageUser, timeItogoMsMinusVK))          //ведение статистики запросов
            StatisticsWindowController.seriesThread.data.add(XYChart.Data(countSendMessageUser, timeDelayThread))                        //ведение статистики задержки потока////здесь иногда ловится исключение
        }
        DatabaseEntity.database.CloseDB()         //закрытие бд
        client.stateBot.botWork = false
    }

    //-----------------отправка и отслеживание запроса в вк на непрочитанные сообщения-------------------------------//
    @Throws(ClientException::class, ApiException::class)
    private fun createListMessageVK(): MutableList<ConversationWithMessage> {
        val timezaprosstart = System.currentTimeMillis()         // начало запроса непрочитанного запроса
        val messages = client.vk.messages().getConversations(actor)                 // Листы сообщений
                .filter(MessagesFilter.UNREAD)
                .count(30)
                .execute().items
        val timezaprosfinish = System.currentTimeMillis()
        timeZaprosFinishItogo =
                timezaprosfinish - timezaprosstart      // время, затраченное на операцию
        return messages
    }

    //-----------------поиск сообщения в основной БД----------------------------------------------//
    private fun messageFromDataBase(
            textMessageString: String,
            message: String?
    ): String? {           //РАБОТА С ОСНОВНОЙ ТАБЛИЦЕЙ КОЛЯНА
        var messages: String? = message
        val listMessages = ArrayList<String>()
        if (!client.stateBot.findMessage) {      // если совпадение с сообщением не найдено, то
            val timeStartBD = System.currentTimeMillis()
            // путешествие по списку объектов из БД
            var countDB = 0
            while (countDB <= (DatabaseEntity.database.botData.size - 1)) {
                if (DatabaseEntity.database.botData[countDB].request.toLowerCase() == textMessageString.toLowerCase()
                ) {  // сравниваем нижний регистр
                    listMessages.add(DatabaseEntity.database.botData[countDB].response)
                    client.stateBot.findMessage =
                            true                                                           // совпадение с сообщением найдено
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
            val timeStartBigBD = System.currentTimeMillis()
            // путешествие по списку объектов из большой БД

            /*
            resSettingBigBD = statmt.executeQuery("SELECT requesttextbot, responsetextbot FROM RandomBazaBot");
                        System.out.print("BigMessagesData= "+ BigMessagesData.size()+ "\n");
            while(resSettingBigBD.next())
            {
                if ( resSettingBigBD.getString("requesttextbot").equals(textMessageString))  {  // сравниваем нижний регистр
                    // сравниваем сообщение и значение в БД
                    listMessages.add(resSettingBigBD.getString("responsetextbot"));
                    findMessage=true;
                }
            }
            resSettingBigBD.close();
            if (findMessage)
                messages = listMessages.get(randomIdBot(listMessages.size()));    // выбираем рандомно из найденного сообщение
*/

            var countDB = 0
            while (countDB <= DatabaseEntity.database.bigMessagesData.size - 1) {
                if (DatabaseEntity.database.bigMessagesData[countDB].request.toLowerCase() == textMessageString
                ) {  // сравниваем нижний регистр
                    // сравниваем сообщение и значение в БД
                    listMessages.add(DatabaseEntity.database.bigMessagesData[countDB].response)
                    client.stateBot.findMessage =
                            true                                                 // совпадение с сообщением найдено
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
            val timeFinishBigBD = System.currentTimeMillis()
            timeConsumedMillisBigBD = timeFinishBigBD - timeStartBigBD
            countUsedBigBD += 1                                            // количество использований большой бд увеличилось на 1
            StatisticsWindowController.seriesBigBD.data.add(XYChart.Data(countUsedBigBD, timeConsumedMillisBigBD.toInt()))                        //ведение статистики задержки потока////здесь иногда ловится исключение
        }
        return messages
    }
}
