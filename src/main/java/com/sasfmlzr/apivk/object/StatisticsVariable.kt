package com.sasfmlzr.apivk.`object`

/**
 * Created by User on 23.08.2017.
 */
object StatisticsVariable {

    //-----------------ебучая статистика------------------------------------------//
    var timeDelayThread: Int = 0                     // время задержки потока // это возможно оптимизировать
    var timeZaprosFinishItogo: Long = 0               // время, затраченное на последнюю операцию запроса непрочитанных сообщений
    var timeItogoMsMinusVK: Long = 0                      // время прохода минус запрос вк непрочитанных сообщений
    var timeZaprosFinishSumm: Long = 0             //  время, затраченное на запрос непрочитанных сообщений вк всего
    var timeItogoSendMessage: Long = 0
    var timeConsumedMillisBigBD: Long = 0    // время выборки из большой БД
    var timeConsumedMillisBD: Long = 0    // время выборки из БД коляна
    var timeProgramStart: Long = 0           // начало работы бота
    var countSendMessageUser: Int = 0                    // счетчик количества исполнений sendMessageUser
    var countSendMessage: Int = 0                    // счетчик количества отправленных сообщений
    var countUsedBigBD = 0                    // счетчик количества обращений к большой БД
    var countUsedBD = 0                       // счетчик количества обращений к БД коляна
    //-----------------ебучая статистика------------------------------------------//

}
