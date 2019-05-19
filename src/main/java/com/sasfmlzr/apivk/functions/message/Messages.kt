package com.sasfmlzr.apivk.functions.message

import com.sasfmlzr.apivk.`object`.StatisticsVariable.countSendMessage
import com.sasfmlzr.apivk.`object`.StatisticsVariable.timeItogoSendMessage
import com.sasfmlzr.apivk.client.BotApiClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.Dialog
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class Messages(vk: VkApiClient) : BotApiClient(vk) {

    //-----------------отправить сообщения без разницы кому, отметка о том, что сообщение прочитано и отслеживание времени, затраченного на отправку------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessage(actor: UserActor, message: String, messagesList: List<Dialog>, vk: VkApiClient) {
        val readState = messagesList[0].message.isReadState            //статус прочтения
        if (!readState) {
            val timeStartSendMessage = System.currentTimeMillis()
            val chatID: Int
            val userID: Int
            if (messagesList[0].message.chatId != null) {                // если это беседа, то использовать чат. если пользователь, то user ID
                chatID = messagesList[0].message.chatId!!                 //берем переменную ChatID для удобности и читаемости
                vkSendMessageChat(actor, message, chatID, vk)                                      //отправляем сообщение
            } else {
                userID = messagesList[0].message.userId!!                  //берем переменную ChatID для удобности и читаемости
                vkSendMessageUser(actor, message, userID, vk)                                          //отправляем сообщение
                //              System.out.print("Сообщение = " + message+"\n");
                vk.messages().markAsRead(actor).messageIds(messagesList[0].message.id).execute()         //пометка сообщения прочитанным
                val timeFinishSendMessage = System.currentTimeMillis()
                timeItogoSendMessage = timeFinishSendMessage - timeStartSendMessage
                //          System.out.print("время отправки сообщения= "+ timeItogoSendMessage + "\n");
                countSendMessage = countSendMessage + 1        // количество отправленных сообщений увеличилось
            }
        }
    }

    //-----------------отправить сообщение пользователюу---------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessageUser(actor: UserActor, message: String, userID: Int, vk: VkApiClient) {
        vk.messages().send(actor)
                .message(message)
                .userId(userID)
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

    private fun vkMessageUserOrChat(messagesList: List<Dialog>): Boolean {
        // если это беседа, то использовать чат. если пользователь, то user ID
        return messagesList[0].message.chatId == null
    }

    @Throws(ClientException::class, ApiException::class)
    fun vksendImageMessages(actor: UserActor, messagesList: List<Dialog>, vk: VkApiClient) {
        val memPath = "./src/resources/mem/"    // беру путь
        val path = Paths.get(memPath)             // перевожу в path
        if (Files.exists(path)) {                   // если путь существует
            val file = File(memPath) //
            val files = file.listFiles()
            //    ((WindowsPathWithAttributes)listMem.get(0))
            val getMessagesUploadServer = vk.photos().getMessagesUploadServer(actor).execute()
            assert(files != null)
            //  String memPathJPG= files[0].getPath();//(String) listMem.get(0);
            val setImage = super.other().randomId(files!!.size)
            //      String memPathJPG= files[setImage].getPath();//(String) listMem.get(0);
            //     Path pathJPG = Paths.get(memPathJPG);
            //     List listMem;
            //     File imageMem = new File("");
            //      System.out.println(files[1].exists());  //файл существует
            val uploadPhotoMessage = vk.upload().photoMessage(getMessagesUploadServer.uploadUrl, files[setImage]).execute()
            val saveMessagesPhoto = vk.photos().saveMessagesPhoto(actor, uploadPhotoMessage.photo).server(uploadPhotoMessage.server!!).hash(uploadPhotoMessage.hash).execute()
            if (vkMessageUserOrChat(messagesList)) {
                val UserID = messagesList[0].message.userId!!
                vk.messages().send(actor).userId(UserID).attachment("photo" + saveMessagesPhoto[0].ownerId + "_" + saveMessagesPhoto[0].id).randomId(super.other().randomId(8000)).message("Держи бро").execute()
            } else {
                val ChatID = messagesList[0].message.chatId!!
                vk.messages().send(actor).chatId(ChatID).attachment("photo" + saveMessagesPhoto[0].ownerId + "_" + saveMessagesPhoto[0].id).randomId(super.other().randomId(8000)).message("Держи бро").execute()
            }
            vk.messages().markAsRead(actor).messageIds(messagesList[0].message.id).execute()         //пометка сообщения прочитанным
        }
    }


    //-----------------отправить сообщения без разницы кому, отметка о том, что сообщение прочитано и отслеживание времени, затраченного на отправку------------------------//
    @Throws(ClientException::class, ApiException::class)
    fun vkSendMessage(actor: GroupActor, message: String, messagesList: List<Dialog>, vk: VkApiClient) {
        val readState = messagesList[0].message.isReadState            //статус прочтения
        if (!readState) {
            val timeStartSendMessage = System.currentTimeMillis()
            val chatID: Int
            val userID: Int
            if (messagesList[0].message.chatId != null) {                // если это беседа, то использовать чат. если пользователь, то user ID
                chatID = messagesList[0].message.chatId!!                 //берем переменную ChatID для удобности и читаемости
                vkSendMessageChat(actor, message, chatID, vk)                                      //отправляем сообщение
            } else {
                userID = messagesList[0].message.userId!!                  //берем переменную ChatID для удобности и читаемости
                vkSendMessageUser(actor, message, userID, vk)                                          //отправляем сообщение
                //              System.out.print("Сообщение = " + message+"\n");
                vk.messages().markAsRead(actor).messageIds(messagesList[0].message.id).execute()         //пометка сообщения прочитанным
                val timeFinishSendMessage = System.currentTimeMillis()
                timeItogoSendMessage = timeFinishSendMessage - timeStartSendMessage
                //          System.out.print("время отправки сообщения= "+ timeItogoSendMessage + "\n");
                countSendMessage = countSendMessage + 1        // количество отправленных сообщений увеличилось
            }
        }
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
    fun vksendImageMessages(actor: GroupActor, messagesList: List<Dialog>, vk: VkApiClient) {
        val memPath = "./src/resources/mem/"    // беру путь
        val path = Paths.get(memPath)             // перевожу в path
        if (Files.exists(path)) {                   // если путь существует
            val file = File(memPath) //
            val files = file.listFiles()
            //    ((WindowsPathWithAttributes)listMem.get(0))
            val getMessagesUploadServer = vk.photos().getMessagesUploadServer(actor).execute()
            assert(files != null)
            //  String memPathJPG= files[0].getPath();//(String) listMem.get(0);
            val setImage = super.other().randomId(files!!.size)
            //      String memPathJPG= files[setImage].getPath();//(String) listMem.get(0);
            //     Path pathJPG = Paths.get(memPathJPG);
            //     List listMem;
            //     File imageMem = new File("");
            //      System.out.println(files[1].exists());  //файл существует
            val uploadPhotoMessage = vk.upload().photoMessage(getMessagesUploadServer.uploadUrl, files[setImage]).execute()
            val saveMessagesPhoto = vk.photos().saveMessagesPhoto(actor, uploadPhotoMessage.photo).server(uploadPhotoMessage.server!!).hash(uploadPhotoMessage.hash).execute()
            if (vkMessageUserOrChat(messagesList)) {
                val UserID = messagesList[0].message.userId!!
                vk.messages().send(actor).userId(UserID).attachment("photo" + saveMessagesPhoto[0].ownerId + "_" + saveMessagesPhoto[0].id).randomId(super.other().randomId(8000)).message("Держи бро").execute()
            } else {
                val ChatID = messagesList[0].message.chatId!!
                vk.messages().send(actor).chatId(ChatID).attachment("photo" + saveMessagesPhoto[0].ownerId + "_" + saveMessagesPhoto[0].id).randomId(super.other().randomId(8000)).message("Держи бро").execute()
            }
            vk.messages().markAsRead(actor).messageIds(messagesList[0].message.id).execute()         //пометка сообщения прочитанным
        }
    }
}