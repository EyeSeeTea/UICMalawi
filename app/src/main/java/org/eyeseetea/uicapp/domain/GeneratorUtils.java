package org.eyeseetea.uicapp.domain;

import org.eyeseetea.uicapp.Utils;

import java.util.Calendar;

public class GeneratorUtils {
    public static String extractLetters(String text, int beginIndex) {
        text = text.replace(" ", "");
        return text.substring(beginIndex);
    }

    public static String extractLetters(String text, int beginIndex, int endIndex) {
        text = text.replace(" ", "");
        return text.substring(beginIndex, endIndex);
    }

    public static String formatDate (long timestamp){
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(timestamp);

        String day = String.format("%02d", Utils.getDay(newCalendar));
        String month = String.format("%02d", Utils.getMonth(newCalendar));
        String year = String.valueOf(Utils.getYear(newCalendar));

        return day + month + year.substring(year.length() - 2);
    }

}
