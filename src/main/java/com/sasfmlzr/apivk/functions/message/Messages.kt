package com.sasfmlzr.apivk.functions.message

import com.sasfmlzr.apivk.`object`.StatisticsVariable.countSendMessage
import com.sasfmlzr.apivk.`object`.StatisticsVariable.timeItogoSendMessage
import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.Actor
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.ConversationWithMessage
import com.vk.api.sdk.objects.users.Fields
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class Messages(vk: VkApiClient, actor: Actor) : BotApiClient(vk, actor) {

    //-----------------отправить сообщения без разницы кому, отметка о том, что сообщение прочитано и отслеживание времени, затраченного на отправку------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessage(actor: Actor, message: String, messagesList: List<ConversationWithMessage>, vk: VkApiClient) {
        when (actor) {
            is UserActor -> {
                val timeStartSendMessage = System.currentTimeMillis()
                val peerID: Int = messagesList[0].lastMessage.peerId
                vkSendMessageUser(actor, message, peerID, vk)                                          //отправляем сообщение
                vk.messages().markAsRead(actor).startMessageId(messagesList[0].lastMessage.id).execute()         //пометка сообщения прочитанным
                val timeFinishSendMessage = System.currentTimeMillis()
                timeItogoSendMessage = timeFinishSendMessage - timeStartSendMessage
                countSendMessage += 1        // количество отправленных сообщений увеличилось
            }
            is GroupActor -> {
                val timeStartSendMessage = System.currentTimeMillis()
                val peerID: Int = messagesList[0].lastMessage.peerId                  //берем переменную ChatID для удобности и читаемости
                vkSendMessageUser(actor, message, peerID, vk)                                          //отправляем сообщение
                vk.messages().markAsRead(actor).peerId(peerID).execute()         //пометка сообщения прочитанным
                val timeFinishSendMessage = System.currentTimeMillis()
                timeItogoSendMessage = timeFinishSendMessage - timeStartSendMessage
                countSendMessage += 1        // количество отправленных сообщений увеличилось
            }
        }
    }

    //-----------------отправить сообщение пользователю---------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessageUser(actor: Actor, message: String, userID: Int, vk: VkApiClient) {
        when (actor) {
            is UserActor -> {
                vk.messages().send(actor)
                        .message(message)
                        .peerId(userID) // like PeerID
                        .randomId(super.other().randomId(8000))
                        .execute()
            }
            is GroupActor -> {
                vk.messages().send(actor)
                        .message(message)
                        .userId(userID)
                        .randomId(super.other().randomId(8000))
                        .execute()
            }
        }
    }

    //-----------------отправить сообшение в чат-----------------------------------//
    @Throws(ClientException::class, ApiException::class)
    private fun vkSendMessageChat(actor: Actor, message: String, chatID: Int, vk: VkApiClient) {
        when (actor) {
            is UserActor -> {
                vk.messages().send(actor)
                        .message(message)
                        .chatId(chatID)
                        .randomId(super.other().randomId(8000))
                        .execute()
            }
            is GroupActor -> {
                vk.messages().send(actor)
                        .message(message)
                        .chatId(chatID)
                        .randomId(super.other().randomId(8000))
                        .execute()
            }
        }
    }

    @Throws(ClientException::class, ApiException::class)
    fun vksendImageMessages(actor: Actor, messagesList: List<ConversationWithMessage>, vk: VkApiClient) {
        when (actor) {
            is UserActor -> {
                val memPath = "./src/resources/mem/"    // беру путь
                val path = Paths.get(memPath)             // перевожу в path
                if (Files.exists(path)) {                   // если путь существует
                    val file = File(memPath)
                    val files = file.listFiles()
                    val getMessagesUploadServer = vk.photos().getMessagesUploadServer(actor).execute()
                    assert(files != null)
                    val setImage = super.other().randomId(files!!.size)
                    val uploadPhotoMessage = vk.upload().photoMessage(getMessagesUploadServer.uploadUrl.toString(), files[setImage]).execute()
                    val saveMessagesPhoto = vk.photos().saveMessagesPhoto(actor, uploadPhotoMessage.photo).server(uploadPhotoMessage.server!!).hash(uploadPhotoMessage.hash).execute()

                    val peerID = messagesList[0].lastMessage.peerId
                    vk.messages().send(actor).peerId(peerID).attachment("photo" + saveMessagesPhoto[0].ownerId + "_" + saveMessagesPhoto[0].id).randomId(super.other().randomId(8000)).message("Держи бро").execute()

                    vk.messages().markAsRead(actor).peerId(peerID).execute() //пометка сообщения прочитанным
                }
            }
            is GroupActor -> {
                val memPath = "./src/resources/mem/"    // беру путь
                val path = Paths.get(memPath)             // перевожу в path
                if (Files.exists(path)) {                   // если путь существует
                    val file = File(memPath)
                    val files = file.listFiles()
                    val getMessagesUploadServer = vk.photos().getMessagesUploadServer(actor).execute()
                    assert(files != null)
                    val setImage = super.other().randomId(files!!.size)
                    val uploadPhotoMessage = vk.upload().photoMessage(getMessagesUploadServer.uploadUrl.toString(), files[setImage]).execute()
                    val saveMessagesPhoto = vk.photos().saveMessagesPhoto(actor, uploadPhotoMessage.photo).server(uploadPhotoMessage.server!!).hash(uploadPhotoMessage.hash).execute()

                    val peerID = messagesList[0].lastMessage.peerId!!
                    vk.messages().send(actor).chatId(peerID).attachment("photo" + saveMessagesPhoto[0].ownerId + "_" + saveMessagesPhoto[0].id).randomId(super.other().randomId(8000)).message("Держи бро").execute()

                    vk.messages().markAsRead(actor).peerId(peerID).execute()         //пометка сообщения прочитанным
                }
            }
        }
    }
}
