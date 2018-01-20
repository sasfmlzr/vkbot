package com.apiVKmanual.object;

/**
 * Created by User on 23.08.2017.
 */
public class StatisticsVariable {

    //-----------------ебучая статистика------------------------------------------//
    public static int timeDelayThread;                     // время задержки потока // это возможно оптимизировать
    public static long timeZaprosFinishItogo;               // время, затраченное на последнюю операцию запроса непрочитанных сообщений
    public static long timeItogoMsMinusVK;                      // время прохода минус запрос вк непрочитанных сообщений
    public static long timeZaprosFinishSumm=0;             //  время, затраченное на запрос непрочитанных сообщений вк всего
    public static long timeItogoSendMessage=0;
    public static long timeConsumedMillisBigBD=0;    // время выборки из большой БД
    public static long timeConsumedMillisBD=0;    // время выборки из БД коляна
    public static long timeProgramStart;           // начало работы бота
    public static int countSendMessageUser;                    // счетчик количества исполнений sendMessageUser
    public static int countSendMessage;                    // счетчик количества отправленных сообщений
    public static int countUsedBigBD=0;                    // счетчик количества обращений к большой БД
    public static int countUsedBD=0;                       // счетчик количества обращений к БД коляна
    //-----------------ебучая статистика------------------------------------------//

}
