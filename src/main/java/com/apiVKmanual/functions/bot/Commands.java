package com.apiVKmanual.functions.bot;

import com.apiVKmanual.client.BotApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import net.aksingh.owmjapis.api.APIException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import static com.apiVKmanual.functions.bot.Functional.weather;
import static com.apiVKmanual.functions.botdatabase.DatabaseRequest.addToDB;
import static com.apiVKmanual.object.StatisticsVariable.countSendMessageUser;
import static com.apiVKmanual.object.StatisticsVariable.timeProgramStart;
import static com.apiVKmanual.object.StatisticsVariable.timeZaprosFinishSumm;
import static com.fomenko.vkbot.controller.menuprogram.DataBaseWindowController.*;
import static com.fomenko.vkbot.controller.BotTabController.priostanovka;
import static com.fomenko.vkbot.controller.BotTabController.findMessage;
import static com.fomenko.vkbot.controller.BotTabController.botStopped;
import static com.fomenko.vkbot.controller.BotTabController.reduction;

public class Commands {

    //-----------------команды бота-----------------------------------------------//
    public static String commandsBot(String textMessageString, String messages, UserActor actor, List<com.vk.api.sdk.objects.messages.Dialog> messagesList, BotApiClient bot) throws  SQLException, ClassNotFoundException, ClientException, ApiException {
        String message=messages;

        if (textMessageString.equals("го стих")){
            System.out.print("Пришло сообщение = " +textMessageString+ "\n");
            message = StihMessagesData.get(bot.other().randomId(StihMessagesData.size())).response;
        }
        if (textMessageString.equals("го афоризм")){
            System.out.print("Пришло сообщение = " +textMessageString+ "\n");
            message = AforismMessagesData.get(bot.other().randomId(AforismMessagesData.size())).response;
        }
        if (textMessageString.equals("го анекдот")){
            System.out.print("Пришло сообщение = " +textMessageString+ "\n");
            message = AnekdotMessagesData.get(bot.other().randomId(AnekdotMessagesData.size())).response;
        }
        if (textMessageString.equals("го статус")){
            System.out.print("Пришло сообщение = " +textMessageString+ "\n");
            message = StatusMessagesData.get(bot.other().randomId(StatusMessagesData.size())).response;
        }
        if (textMessageString.equals("го справку")){
            System.out.print("Пришло сообщение = " + textMessageString + "\n");

            message = "го статус - модный статус про машину в вк \n" +
                    "го анекдот - анекдот про хонду \n" +
                    "го афоризм - афоризм про машину в вк \n" +
                    "го стих - стих про машину в вк \n";
        }
        if (textMessageString.equals("го админ справку")){
            System.out.print("Пришло сообщение = " + textMessageString + "\n");

            message = "го средний пинг \n" +
                    "го время работы \n" +
                    "го приостановка бота \n";
        }


        if (textMessageString.contains("добавь")){

            System.out.print("Пришло сообщение = " + textMessageString + "\n");

            message = addToDB(textMessageString);
        }

        if (textMessageString.contains("го мем")){

            System.out.print("Пришло сообщение = " + textMessageString + "\n");

            bot.messages().vksendImageMessages(actor,messagesList);
            reduction=true;
        }



        String city;
        if (textMessageString.contains("го погоду")){
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            city= textMessageString.substring(9);
            city = city.trim();
            if (Objects.equals(city, ""))
            {
                message="Введите город по типу -Колян, го погоду Москва-";
            }else {
                System.out.println("Запрос на погоду - город: " + city);
                try {
                    message= weather(city);
                } catch (APIException e) {
                    e.printStackTrace();
                }
            }
        }
        if (textMessageString.equals("го приостановка бота")){
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = "Ок, бот приостановлен на минуту ";
            priostanovka =true;
        }
        if (textMessageString.equals("го пинг")){
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = "Среднее время запроса до вк равно " + Math.round (100*timeZaprosFinishSumm/ countSendMessageUser)/100 +"мс\n";
        }
        if (textMessageString.equals("го время работы")){
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            long timeProgramFinish = System.currentTimeMillis();
            long timeProgramItog = timeProgramFinish-timeProgramStart;
            message = "Время работы равно " + Math.round(timeProgramItog/1000) + "c";
        }

        if(!Objects.equals(message, messages)){
            findMessage=true;
            System.out.print("сработали команды \n");
        }
        return message;
    }
    //-----------------админские команды бота-----------------------------------------------//
    public static String adminCommandsBot(String textMessageString, String messages)  {
        String message=messages;


        if (textMessageString.equals("выключись")){
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = "Выключаюсь(";
            botStopped =true;
        }
        if (textMessageString.equals("включись")){
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = "Смотри скай поехал";
            botStopped =false;
        }

        if(!Objects.equals(message, messages)){
            findMessage=true;
            System.out.print("сработали админ команды \n");
        }
        return message;
    }
}
