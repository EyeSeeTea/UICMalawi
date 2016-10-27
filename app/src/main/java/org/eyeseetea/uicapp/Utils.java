package org.eyeseetea.uicapp;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by idelcano on 25/10/2016.
 */

public class Utils {


    /**
     * Is the filter to prevent the input of characters
     *
     * @return
     */
    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String blockCharacterSet = "~#^|$%*!@/()-'\":;,?{}=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪:-);-):-D:-(:'(:O1234567890+.&";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
            }
            return null;
        }
    };

    /**
     * Get today date with the correct year month and day but: 00:00:00 00:00:00
     *
     * @return
     */
    public static Date getTodayFirstTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);

        return calendar.getTime();
    }

    /**
     *  Returns the year in a calendar date
     * @return
     */
    static int getYear(Calendar newCalendar) {
        return newCalendar.get(Calendar.YEAR);
    }

    /**
     *  Returns the day in a calendar date
     * @return
     */
    static int getDay(Calendar newCalendar) {
        return newCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     *  Returns the month in a calendar date
     * @return
     */
    static int getMonth(Calendar newCalendar) {
        return newCalendar.get(Calendar.MONTH) + 1;
    }
}
