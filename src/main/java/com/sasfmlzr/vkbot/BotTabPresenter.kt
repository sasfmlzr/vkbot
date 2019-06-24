package com.sasfmlzr.vkbot

import com.sasfmlzr.apivk.`object`.StatisticsVariable

class BotTabPresenter {

    fun fillLog() : String{
        if (StaticModel.userBot.botApiClient().stateBot.botWork) {
            var statisticMsg = "Время, затраченное на последнюю операцию запроса непрочитанных сообщений " + StatisticsVariable.timeZaprosFinishItogo + "мс\n" +
                    "Время, затраченное на последние остальные операции " + StatisticsVariable.timeItogoMsMinusVK + "мс\n" +
                    "Подробнее:\n"
            if (StatisticsVariable.timeConsumedMillisBD != 0L)
                statisticMsg = statisticMsg + "Время последней выборки из бд коляна составляет " + StatisticsVariable.timeConsumedMillisBD + "мс\n"
            if (StatisticsVariable.timeConsumedMillisBigBD != 0L)
                statisticMsg = statisticMsg + "Время последней выборки из большой бд составляет " + StatisticsVariable.timeConsumedMillisBigBD + "мс\n"
            if (StatisticsVariable.timeItogoSendMessage != 0L)
                statisticMsg = statisticMsg + "Время затраченное на последнюю отправку сообщения составляет " + StatisticsVariable.timeItogoSendMessage + "мс\n" +
                        "Количество отправленных сообщений равно " + StatisticsVariable.countSendMessage + "\n"
            val timeProgramFinish = System.currentTimeMillis()
            val timeProgramItog = timeProgramFinish - StatisticsVariable.timeProgramStart
            statisticMsg = statisticMsg + "Время работы бота равно " + Math.round((timeProgramItog / 1000).toFloat()) + "c\n" +
                    "Среднее время запроса до вк равно " + Math.round((100 * StatisticsVariable.timeZaprosFinishSumm / StatisticsVariable.countSendMessageUser).toFloat()) / 100 + "мс\n" +
                    "Количество совершенных циклов работы бота равно " + StatisticsVariable.countSendMessageUser + "\n"
            if (StatisticsVariable.countUsedBD != 0)
                statisticMsg = statisticMsg + "Количество обращений к основной таблицы БД равно " + StatisticsVariable.countUsedBD + "\n"
            if (StatisticsVariable.countUsedBigBD != 0)
                statisticMsg = statisticMsg + "Количество обращений к большой таблице БД равно " + StatisticsVariable.countUsedBigBD + "\n"
            return statisticMsg
        } else
            return "Сначала включи бота"
    }
}
