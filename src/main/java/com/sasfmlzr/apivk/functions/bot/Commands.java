package com.sasfmlzr.apivk.functions.bot;

import com.sasfmlzr.apivk.actions.Messages;
import com.sasfmlzr.apivk.client.BotApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static com.sasfmlzr.apivk.object.StatisticsVariable.*;

public class Commands extends Messages {

    public Commands(BotApiClient client) {
        super(client);
    }

    //-----------------команды бота-----------------------------------------------//
    public String commandsBot(String textMessageString, String messages, UserActor actor, List<com.vk.api.sdk.objects.messages.Dialog> messagesList, BotApiClient bot) throws SQLException, ClientException, ApiException {
        String message = messages;

        if (textMessageString.equals("го стих")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = BotApiClient.Companion.getDatabase().getStihMessagesData().get(bot.other().randomId(BotApiClient.Companion.getDatabase().getStihMessagesData().size())).response;
        }
        if (textMessageString.equals("го афоризм")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = BotApiClient.Companion.getDatabase().getAforismMessagesData().get(bot.other().randomId(BotApiClient.Companion.getDatabase().getAforismMessagesData().size())).response;
        }
        if (textMessageString.equals("го анекдот")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = BotApiClient.Companion.getDatabase().getAnekdotMessagesData().get(bot.other().randomId(BotApiClient.Companion.getDatabase().getAnekdotMessagesData().size())).response;
        }
        if (textMessageString.equals("го статус")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = BotApiClient.Companion.getDatabase().getStatusMessagesData().get(bot.other().randomId(BotApiClient.Companion.getDatabase().getStatusMessagesData().size())).response;
        }
        if (textMessageString.equals("го справку")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");

            message = "го статус - модный статус про машину в вк \n" +
                    "го анекдот - анекдот про хонду \n" +
                    "го афоризм - афоризм про машину в вк \n" +
                    "го стих - стих про машину в вк \n";
        }
        if (textMessageString.equals("го админ справку")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");

            message = "го средний пинг \n" +
                    "го время работы \n" +
                    "го приостановка бота \n";
        }


        if (textMessageString.contains("добавь")) {

            System.out.print("Пришло сообщение = " + textMessageString + "\n");

            message = BotApiClient.Companion.getDatabase().databaseRequest(BotApiClient.Companion.getDatabase().getStatmt()).addToDB(textMessageString, actor.getId());
        }

        if (textMessageString.contains("го мем")) {

            System.out.print("Пришло сообщение = " + textMessageString + "\n");

            bot.messages().vksendImageMessages(actor, messagesList);
            getClient().getStateBot().setReduction(true);
        }
        if (textMessageString.equals("го приостановка бота")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = "Ок, бот приостановлен на минуту ";
            getClient().getStateBot().setPriostanovka(true);
        }
        if (textMessageString.equals("го пинг")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = "Среднее время запроса до вк равно " + Math.round(100 * timeZaprosFinishSumm / countSendMessageUser) / 100 + "мс\n";
        }
        if (textMessageString.equals("го время работы")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            long timeProgramFinish = System.currentTimeMillis();
            long timeProgramItog = timeProgramFinish - timeProgramStart;
            message = "Время работы равно " + Math.round(timeProgramItog / 1000) + "c";
        }

        if (!Objects.equals(message, messages)) {
            getClient().getStateBot().setFindMessage(true);
            System.out.print("сработали команды \n");
        }
        return message;
    }

    //-----------------админские команды бота-----------------------------------------------//
    public String adminCommandsBot(String textMessageString, String messages) {
        String message = messages;


        if (textMessageString.equals("выключись")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = "Выключаюсь(";
            getClient().getStateBot().setBotStopped(true);
        }
        if (textMessageString.equals("включись")) {
            System.out.print("Пришло сообщение = " + textMessageString + "\n");
            message = "Смотри скай поехал";
            getClient().getStateBot().setBotStopped(false);
        }

        if (!Objects.equals(message, messages)) {
            getClient().getStateBot().setFindMessage(true);
            System.out.print("сработали админ команды \n");
        }
        return message;
    }
}
