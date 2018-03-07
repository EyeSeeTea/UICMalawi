package org.eyeseetea.uicapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.Calendar;

public class CodeGenerator {
    public static final String DEFAULT_VALUE = "";
    private Context mContext;

    public CodeGenerator(Context context) {
        mContext = context;
    }

    public String generateCode() {
        String code = "";
        code += get2And3CharsFromPreferences(R.string.shared_key_mother);
        code += get2And3CharsFromPreferences(R.string.shared_key_surname);
        code += getLast2CharsFromPreference(R.string.shared_key_district);

        Long defaultNoDate = Long.parseLong(mContext.getString(R.string.default_no_date));
        Long timestamp = getLongFromSharedPreference(R.string.shared_key_timestamp_date,
                defaultNoDate);
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(timestamp);

        String day = String.format("%02d", Utils.getDay(newCalendar));
        String month = String.format("%02d", Utils.getMonth(newCalendar));
        String year = String.valueOf(Utils.getYear(newCalendar));

        code += day;
        code += month;
        code += year.substring(year.length() - 2);
        code += getStringFromSharedPreference(R.string.shared_key_sex, DEFAULT_VALUE).substring(0,
                1);

        if (getBooleanFromSharedPreference(R.string.shared_key_twin_checkbox, false)) {
            code += "T" + getStringFromSharedPreference(R.string.shared_key_twin_dropdown, "");
        }

        return code.toUpperCase();
    }

    private String get2And3CharsFromPreferences(int keyId) {
        String temporalValue = getStringFromSharedPreference(keyId, DEFAULT_VALUE);
        temporalValue = temporalValue.replace(" ", "");
        return temporalValue.substring(1, 3);
    }

    /**
     * Given a String key ID, this method looks for it in the preferences and return the last 2
     * chars
     *
     * @param keyId String key to look for in preferences
     * @return String containing the last 2 characters
     */
    @NonNull
    private String getLast2CharsFromPreference(int keyId) {
        String temporalValue = getStringFromSharedPreference(keyId, DEFAULT_VALUE);
        temporalValue = temporalValue.replace(" ", "");
        return temporalValue.substring(temporalValue.length() - 2);
    }

    /**
     * Gets the string value for the given key
     */
    private String getStringFromSharedPreference(int keyId, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                mContext);
        return sharedPreferences.getString(mContext.getResources().getString(keyId), defaultValue);
    }

    /**
     * Puts the Long value in the given key
     */
    private Long getLongFromSharedPreference(int keyId, Long defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                mContext);
        return sharedPreferences.getLong(mContext.getResources().getString(keyId),
                Long.parseLong(defaultValue + ""));
    }

    private boolean getBooleanFromSharedPreference(int keyId, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                mContext);
        return sharedPreferences.getBoolean(mContext.getResources().getString(keyId), defaultValue);
    }

}
