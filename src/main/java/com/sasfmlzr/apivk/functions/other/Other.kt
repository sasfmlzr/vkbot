package com.sasfmlzr.apivk.functions.other

import kotlin.math.roundToInt

class Other {

    //-----------------рандом ID для вывода бота или прочего-----------------------//
    fun randomId(sizeTable: Int): Int {
        val a = 0 // Начальное значение диапазона - "от"
        val b = sizeTable - 1 // Конечное значение диапазона - "до"
        return a + (Math.random() * b).roundToInt()
    }
}
