package com.newapi.utils

import com.newapi.apivk.architecture.db.DatabaseRequest
import com.sasfmlzr.vkbot.StaticModel
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.ConversationWithMessage
import com.vk.api.sdk.objects.messages.Message
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class ParseMessageToDB constructor(private val databaseRequest: DatabaseRequest) {

    fun parseMessageToDB(actor: UserActor, fromID: Int) {

        val listDialogs = parseDialog(actor)

        val listMessages = mutableListOf<Message>().also { messages ->
            listDialogs.forEach {
                println("Parsing message for ${it.conversation.peer.id}")
                messages.addAll(parseMessages(it.conversation.peer.id, actor))
                Thread.sleep(350)
            }
        }.also { sortMessagesByDate(it) }

        val textMessages = findMessagesById(listMessages, fromID)

        println("Fill database started")
        fillDatabase(textMessages)
        println("Fill database finished")
    }

    private fun parseDialog(actor: UserActor): List<ConversationWithMessage> {
        val conversationWithMessages = runDialogs(0, actor) as MutableList
        var sizeDialogs: Int
        var countDialogs = 200
        do {
            val tempDialogs = runDialogs(countDialogs, actor)
            conversationWithMessages.addAll(tempDialogs)
            sizeDialogs = tempDialogs.size
            countDialogs += 200
        } while (sizeDialogs != 0)
        return conversationWithMessages
    }

    private fun parseMessages(peerId: Int, actor: UserActor): List<Message> {
        val messages = getMessages(peerId, 0, actor) as MutableList
        var countMessages = 200
        var sizeMessages: Int
        do {
            val tempMessages = getMessages(peerId, countMessages, actor)
            messages.addAll(tempMessages)
            sizeMessages = tempMessages.size
            countMessages += 200
            println("Parsing message size $countMessages")
            Thread.sleep(400)
        } while (sizeMessages != 0)
        return messages
    }

    //92330508 если это колян
    //96026192 если это ростик
    //78521993 если это лена

    private fun sortMessagesByDate(messages: List<Message>): List<Message> {
        val comparator = Comparator<Message> { o1, o2 ->
            if (o1.date == null || o2.date == null)
                return@Comparator 0
            val timeo1 = LocalDateTime.ofInstant(Instant.ofEpochSecond(o1.date!!.toLong()), ZoneId.systemDefault())
            val timeo2 = LocalDateTime.ofInstant(Instant.ofEpochSecond(o2.date!!.toLong()), ZoneId.systemDefault())
            timeo1.compareTo(timeo2)
        }
        return messages.sortedWith(comparator)
    }

    private fun findMessagesById(messages: List<Message>, fromID: Int): List<String> {
        return messages.filter { it.fromId == fromID }.map { it.text }
    }

    private fun fillDatabase(texts: List<String>) {
        texts.forEach {
            if (it != "") {
                databaseRequest.addRandomMessage(it)
            }
        }
    }

    private fun getMessages(peerId: Int, offset: Int, actor: UserActor): List<Message> {
        var messages: List<Message> = ArrayList()
        try {
            messages = StaticModel.userBot.vk.messages().getHistory(actor)
                    .peerId(peerId)
                    .offset(offset)
                    .count(200)
                    .execute().items
        } catch (e: ApiException) {
            e.printStackTrace()
        } catch (e: ClientException) {
            e.printStackTrace()
        }
        return messages
    }

    private fun runDialogs(offset: Int, actor: UserActor): List<ConversationWithMessage> {
        try {
            return StaticModel.userBot.vk.messages().getConversations(actor)
                    .offset(offset)// записываем в лист результат работы запроса
                    .count(200)
                    .execute().items
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listOf()
    }
}