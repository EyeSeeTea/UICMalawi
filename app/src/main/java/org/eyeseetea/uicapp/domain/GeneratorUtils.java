package org.eyeseetea.uicapp.domain;

import org.eyeseetea.uicapp.Utils;

import java.util.Calendar;

public class GeneratorUtils {
    public static String extractLastLetters(String text, int count) {
        text = text.replace(" ", "");
        return text.substring(text.length() - count);
    }

    public static String extractFirstLetters(String text, int count) {
        text = text.replace(" ", "");
        return text.substring(0,count);
    }

    public static String extratcSecondAndThirdtLetters(String text) {
        text = text.replace(" ", "");
        return text.substring(1, 3);
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
