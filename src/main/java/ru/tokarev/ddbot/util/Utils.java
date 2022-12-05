package ru.tokarev.ddbot.util;

import java.util.List;

public class Utils {
    //Парсит строку в кол-во секунд; строка должна иметь формат: число + постфикс (с для секунд, м для минут, ч для часов),
    //разделитель - пробелы.
    //Пример: 3ч 38м 2с; 68с 3м. и т.д.
    public static long parseStringTimeToSec(String text) {
        List<String> timeIntervals = List.of(text.split(" "));
        long timeCounter = 0L;

        for (String interval : timeIntervals) {
            long digits = Long.parseLong(interval.substring(0, interval.length() - 1));
            char lastLetter = interval.charAt(interval.length() - 1);

            switch (lastLetter) {
                case 'с' -> timeCounter += digits;
                case 'м' -> timeCounter += digits * 60;
                case 'ч' -> timeCounter += digits * 3600;
            }
        }

        return timeCounter;
    }
}
