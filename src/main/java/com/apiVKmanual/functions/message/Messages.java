package com.apiVKmanual.functions.message;

import com.apiVKmanual.client.BotApiClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Dialog;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import com.vk.api.sdk.objects.photos.responses.MessageUploadResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.apiVKmanual.object.StatisticsVariable.timeItogoSendMessage;
import static com.apiVKmanual.object.StatisticsVariable.countSendMessage;


    public class Messages extends BotApiClient {

    public Messages (VkApiClient vk){
        super(vk);
    }

    //-----------------отправить сообщения без разницы кому, отметка о том, что сообщение прочитано и отслеживание времени, затраченного на отправку------------------------//
    public void vkSendMessage(UserActor actor, String message, List<Dialog> messagesList, VkApiClient vk) throws ClientException, ApiException {
        Boolean readState =  messagesList.get(0).getMessage().isReadState();            //статус прочтения
        if (!readState) {
            long timeStartSendMessage=      System.currentTimeMillis();
            int chatID, userID;
            if (messagesList.get(0).getMessage().getChatId()!=null) {                // если это беседа, то использовать чат. если пользователь, то user ID
                chatID = messagesList.get(0).getMessage().getChatId();                 //берем переменную ChatID для удобности и читаемости
                vkSendMessageChat(actor, message, chatID, vk);                                      //отправляем сообщение
            }else {
                userID = messagesList.get(0).getMessage().getUserId();                  //берем переменную ChatID для удобности и читаемости
                vkSendMessageUser(actor, message, userID, vk);                                          //отправляем сообщение
                //              System.out.print("Сообщение = " + message+"\n");
                vk.messages().markAsRead(actor).messageIds(messagesList.get(0).getMessage().getId()).execute();         //пометка сообщения прочитанным
                long timeFinishSendMessage=      System.currentTimeMillis();
                timeItogoSendMessage = timeFinishSendMessage - timeStartSendMessage;
                //          System.out.print("время отправки сообщения= "+ timeItogoSendMessage + "\n");
                countSendMessage=countSendMessage+1;        // количество отправленных сообщений увеличилось
            }
        }
    }
    //-----------------отправить сообщение пользователюу---------------------------//
    public void vkSendMessageUser(UserActor actor, String message, int userID, VkApiClient vk) throws ClientException, ApiException {
        vk.messages().send(actor)
                .message(message)
                .userId(userID)
                .randomId(super.other().randomId(8000))
                .execute();
    }
    //-----------------отправить сообшение в чат-----------------------------------//
    private void vkSendMessageChat(UserActor actor, String message, int chatID, VkApiClient vk) throws ClientException, ApiException {
        vk.messages().send(actor)
                .message(message)
                .chatId(chatID)
                .randomId(super.other().randomId(8000))
                .execute();
    }

    private boolean vkMessageUserOrChat(List<Dialog> messagesList){
        // если это беседа, то использовать чат. если пользователь, то user ID
        return messagesList.get(0).getMessage().getChatId() == null;
    }
    public void vksendImageMessages(UserActor actor, List<Dialog> messagesList, VkApiClient vk) throws ClientException, ApiException {


        String memPath = "./src/resources/mem/";    // беру путь
        Path path = Paths.get(memPath);             // перевожу в path
        if (Files.exists(path)) {                   // если путь существует
            File file = new File(memPath); //
            File[]  files = file.listFiles();
            //    ((WindowsPathWithAttributes)listMem.get(0))
            PhotoUpload getMessagesUploadServer = vk.photos().getMessagesUploadServer(actor).execute();
                        assert files != null;
          //  String memPathJPG= files[0].getPath();//(String) listMem.get(0);
            int setImage = super.other().randomId(files.length);
      //      String memPathJPG= files[setImage].getPath();//(String) listMem.get(0);
       //     Path pathJPG = Paths.get(memPathJPG);
       //     List listMem;
       //     File imageMem = new File("");
      //      System.out.println(files[1].exists());  //файл существует
            MessageUploadResponse uploadPhotoMessage = vk.upload().photoMessage(getMessagesUploadServer.getUploadUrl(), files[setImage]).execute();
            List<Photo> saveMessagesPhoto = vk.photos().saveMessagesPhoto(actor, uploadPhotoMessage.getPhoto()).server(uploadPhotoMessage.getServer()).hash(uploadPhotoMessage.getHash()).execute();
            if (vkMessageUserOrChat(messagesList)){
                int UserID=messagesList.get(0).getMessage().getUserId();
                vk.messages().send(actor).userId(UserID).attachment("photo"+saveMessagesPhoto.get(0).getOwnerId()+"_"+saveMessagesPhoto.get(0).getId()).randomId(super.other().randomId(8000)).message("Держи бро").execute();
            }else{
                int ChatID=messagesList.get(0).getMessage().getChatId();
                vk.messages().send(actor).chatId(ChatID).attachment("photo"+saveMessagesPhoto.get(0).getOwnerId()+"_"+saveMessagesPhoto.get(0).getId()).randomId(super.other().randomId(8000)).message("Держи бро").execute();
            }


            System.out.println("ioooooooo");
            vk.messages().markAsRead(actor).messageIds(messagesList.get(0).getMessage().getId()).execute();         //пометка сообщения прочитанным

        }
    }












}
