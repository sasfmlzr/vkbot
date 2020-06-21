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
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class Messages(vk: VkApiClient, actor: Actor) : BotApiClient(vk, actor) {

    //-----------------отправить сообщения без разницы кому, отметка о том, что сообщение прочитано и отслеживание времени, затраченного на отправку------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessage(actor: UserActor, message: String, messagesList: List<ConversationWithMessage>, vk: VkApiClient) {


        val timeStartSendMessage = System.currentTimeMillis()

        val peerID: Int = messagesList[0].lastMessage.peerId

        //берем переменную ChatID для удобности и читаемости
        vkSendMessageUser(actor, message, peerID, vk)                                          //отправляем сообщение
        //              System.out.print("Сообщение = " + message+"\n");
        vk.messages().markAsRead(actor).startMessageId(messagesList[0].lastMessage.id).execute()         //пометка сообщения прочитанным
        val timeFinishSendMessage = System.currentTimeMillis()
        timeItogoSendMessage = timeFinishSendMessage - timeStartSendMessage
        //          System.out.print("время отправки сообщения= "+ timeItogoSendMessage + "\n");
        countSendMessage += 1        // количество отправленных сообщений увеличилось


    }

    //-----------------отправить сообщение пользователюу---------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessageUser(actor: UserActor, message: String, peerID: Int, vk: VkApiClient) {
        vk.messages().send(actor)
                .message(message)
                .peerId(peerID)
                .randomId(super.other().randomId(8000))
                .execute()
    }

    //-----------------отправить сообшение в чат-----------------------------------//
    @Throws(ClientException::class, ApiException::class)
    private fun vkSendMessageChat(actor: UserActor, message: String, chatID: Int, vk: VkApiClient) {
        vk.messages().send(actor)
                .message(message)
                .chatId(chatID)
                .randomId(super.other().randomId(8000))
                .execute()
    }

    @Throws(ClientException::class, ApiException::class)
    fun vksendImageMessages(actor: UserActor, messagesList: List<ConversationWithMessage>, vk: VkApiClient) {
        val memPath = "./src/resources/mem/"    // беру путь
        val path = Paths.get(memPath)             // перевожу в path
        if (Files.exists(path)) {                   // если путь существует
            val file = File(memPath) //
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


    //-----------------отправить сообщения без разницы кому, отметка о том, что сообщение прочитано и отслеживание времени, затраченного на отправку------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessage(actor: GroupActor, message: String, messagesList: List<ConversationWithMessage>, vk: VkApiClient) {
        val timeStartSendMessage = System.currentTimeMillis()

        val peerID: Int = messagesList[0].lastMessage.peerId!!                  //берем переменную ChatID для удобности и читаемости
        vkSendMessageUser(actor, message, peerID, vk)                                          //отправляем сообщение
        //              System.out.print("Сообщение = " + message+"\n");
        vk.messages().markAsRead(actor).peerId(peerID).execute()         //пометка сообщения прочитанным
        val timeFinishSendMessage = System.currentTimeMillis()
        timeItogoSendMessage = timeFinishSendMessage - timeStartSendMessage
        //          System.out.print("время отправки сообщения= "+ timeItogoSendMessage + "\n");
        countSendMessage += 1        // количество отправленных сообщений увеличилось
    }

    //-----------------отправить сообщение пользователюу---------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessageUser(actor: GroupActor, message: String, userID: Int, vk: VkApiClient) {
        vk.messages().send(actor)
                .message(message)
                .userId(userID)
                .randomId(super.other().randomId(8000))
                .execute()
    }

    //-----------------отправить сообшение в чат-----------------------------------//
    @Throws(ClientException::class, ApiException::class)
    private fun vkSendMessageChat(actor: GroupActor, message: String, chatID: Int, vk: VkApiClient) {
        vk.messages().send(actor)
                .message(message)
                .chatId(chatID)
                .randomId(super.other().randomId(8000))
                .execute()
    }

    @Throws(ClientException::class, ApiException::class)
    fun vksendImageMessages(actor: GroupActor, messagesList: List<ConversationWithMessage>, vk: VkApiClient) {
        val memPath = "./src/resources/mem/"    // беру путь
        val path = Paths.get(memPath)             // перевожу в path
        if (Files.exists(path)) {                   // если путь существует
            val file = File(memPath) //
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
