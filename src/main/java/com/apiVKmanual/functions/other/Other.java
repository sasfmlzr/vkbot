package com.apiVKmanual.functions.other;

public class Other {

    //-----------------рандом ID для вывода бота или прочего-----------------------//
    public int randomId(int sizeTable) {
        int a = 0; // Начальное значение диапазона - "от"
        int b = sizeTable-1; // Конечное значение диапазона - "до"
        return (a + (int)Math.round( (Math.random() * b)));
    }
}
