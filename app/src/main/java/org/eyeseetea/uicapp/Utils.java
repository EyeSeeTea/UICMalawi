package org.eyeseetea.uicapp;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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


    /**
     * Shows an alert dialog with a big message inside based on a raw resource HTML formatted
     * @param titleId Id of the title resource
     * @param rawId Id of the raw text resource in HTML format
     */
    public static void showAlertWithHtmlMessageAndLastCommit(int titleId, int rawId, Context context){
        String stringMessage = getMessageWithCommit(rawId, context);
        final SpannableString linkedMessage = new SpannableString(Html.fromHtml(stringMessage));
        Linkify.addLinks(linkedMessage, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);

        showAlertWithLogoAndVersion(titleId, linkedMessage, context);
    }
    /**
     * Merge the lastcommit into the raw file
     * @param rawId Id of the raw text resource in HTML format
     */
    public static String getMessageWithCommit(int rawId, Context context) {
        InputStream message = context.getResources().openRawResource(rawId);
        String stringCommit = getCommitHash(context);
        String stringMessage= Utils.convertFromInputStreamToString(message).toString();
        if(stringCommit.contains(context.getString(R.string.unavailable))){
            stringCommit=String.format(context.getString(R.string.lastcommit),stringCommit);
            stringCommit=stringCommit+" "+context.getText(R.string.lastcommit_unavailable);
        }
        else {
            stringCommit = String.format(context.getString(R.string.lastcommit), stringCommit);
        }
        stringMessage=String.format(stringMessage,stringCommit);
        return stringMessage;
    }


    public static String getCommitHash(Context context){
        String stringCommit;
        //Check if lastcommit.txt file exist, and if not exist show as unavailable.
        int layoutId = context.getResources().getIdentifier("lastcommit", "raw", context.getPackageName());
        if (layoutId == 0){
            stringCommit=context.getString(R.string.unavailable);
        } else {
            InputStream commit = context.getResources().openRawResource( layoutId);
            stringCommit= Utils.convertFromInputStreamToString(commit).toString();
        }
        return stringCommit;
    }

    public static void showAlertWithLogoAndVersion(int titleId, CharSequence text, Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_about);
        dialog.setTitle(titleId);
        dialog.setCancelable(true);

        //set up text title
        TextView textTile = (TextView) dialog.findViewById(R.id.aboutTitle);
        textTile.setText(BuildConfig.FLAVOR.toUpperCase() + "(bb) " + BuildConfig.VERSION_NAME);
        textTile.setGravity(Gravity.RIGHT);

        //set up text title
        TextView textContent = (TextView) dialog.findViewById(R.id.aboutMessage);
        textContent.setMovementMethod(LinkMovementMethod.getInstance());
        textContent.setText(text);
        //set up button
        Button button = (Button) dialog.findViewById(R.id.aboutButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        dialog.show();
    }
    public static StringBuilder convertFromInputStreamToString(InputStream inputStream){
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = r.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            Log.d("AUtils", String.format("Error reading inputStream [%s]", inputStream));
            e.printStackTrace();
        }

        return stringBuilder;
    }

}
