package org.eyeseetea.uicapp;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.eyeseetea.uicapp.views.EditCard;
import org.eyeseetea.uicapp.views.TextCard;

import java.util.Calendar;

public class ScrollingActivity extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_us) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        createActionBar();
        initValues();
        refreshCode();
    }

    private void refreshCode() {
        TextCard textView = (TextCard) findViewById(R.id.code_text);
        if(validateAllFields()){
            textView.setText(generateCode());
        } else {
            textView.setText(getApplicationContext().getString(R.string.code_invalid));
        }
    }

    private String generateCode() {
        String code="";
        code += getLast2CharsFromPreference(R.string.shared_key_mother);
        code += getLast2CharsFromPreference(R.string.shared_key_surname);
        code += getLast2CharsFromPreference(R.string.shared_key_district);

        Long defaultNoDate=Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
        Long timestamp = getLongFromSharedPreference(R.string.shared_key_timestamp_date, defaultNoDate);
        Calendar newCalendar= Calendar.getInstance();
        newCalendar.setTimeInMillis(timestamp);

        String day = String.format("%02d", getDay(newCalendar));
        String month = String.format("%02d", getMonth(newCalendar));
        String year = String.valueOf(getYear(newCalendar));

        code += day;
        code += month;
        code += year.substring(year.length()-2);
        code += getStringFromSharedPreference(R.string.shared_key_sex).substring(0,1);

        return code.toUpperCase();
    }

    /**
     * Given a String key ID, this method looks for it in the preferences and return the last 2 chars
     * @param keyId String key to look for in preferences
     * @return String containing the last 2 characters
     */
    @NonNull
    private String getLast2CharsFromPreference(int keyId) {
        String temporalValue = getStringFromSharedPreference(keyId);
        temporalValue = temporalValue.replace(" ", "");
        return temporalValue.substring(temporalValue.length()-2);
    }

    private boolean validateAllFields() {

        if(!validateText(R.string.shared_key_mother)) {
            return false;
        }

        if(!validateText(R.string.shared_key_surname)) {
            return false;
        }

        if(!validateText(R.string.shared_key_district)) {
            return false;
        }

        if(getStringFromSharedPreference(R.string.shared_key_sex).equals("")) {
            return false;
        }

        if(!validateDate()) {
            return false;
        }

        return true;
    }

    private boolean validateDate() {
        Long defaultNoDate=Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
        Long timestamp = getLongFromSharedPreference(R.string.shared_key_timestamp_date, defaultNoDate);
        if(timestamp.equals(defaultNoDate)){
            return false;
        }
        Calendar savedDate = Calendar.getInstance();
        savedDate.setTimeInMillis(timestamp);
        Calendar today = Calendar.getInstance();
        //Not pass the validation the dates after today or equals to today.
        if(savedDate.after(today) || (getDay(today) == getDay(savedDate)
                && getMonth(today) == getMonth(savedDate)
                && getYear(today) == getYear(savedDate))){
            return false;
        }
        return true;
    }

    private boolean validateText(int keyId) {
        String value = getStringFromSharedPreference(keyId);
        //At least two characters without numbers and with possible blank spaces
        String regExp="^[ a-zA-Z]*([a-zA-Z]{1,}[ ]*[a-zA-Z]{1,})[ a-zA-Z]*$";
        if(value.matches(regExp)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Creates the menu actionBar
     *
     * @return
     */
    private void createActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Init the saved or new values and add the listeners in the app components
     *
     * @return
     */
    private void initValues() {
        //Init mother
        initTextValue((EditCard) findViewById(R.id.mother_edit_text), R.string.shared_key_mother, R.string.mother_error);
        //Init surname
        initTextValue((EditCard) findViewById(R.id.surname_edit_text), R.string.shared_key_surname, R.string.surname_error);
        //Init district
        initTextValue((EditCard) findViewById(R.id.district_edit_text), R.string.shared_key_district, R.string.district_error);

        //Init district
        initDate();

        //Init sex
        initSex(R.string.shared_key_sex);
    }

    private void initDate() {
        LinearLayout dateFields =(LinearLayout) findViewById(R.id.day_date);
        recoveryAndShowDate();
        View.OnClickListener dateOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerListener(v);
            }
        };

        dateFields.setOnClickListener(dateOnClickListener);
    }

    private void initSex(final int keyId) {
        String value= getStringFromSharedPreference(keyId);
        //Set value if exist in sharedPreferences
        if(!value.equals("")){
            final String male=getApplication().getApplicationContext().getString(R.string.sex_male);
            String female=getApplication().getApplicationContext().getString(R.string.sex_female);
            String trasngender=getApplication().getApplicationContext().getString(R.string.sex_transgender);
            if(value.equals(male)){
                ((RadioButton)findViewById(R.id.radio_male)).setChecked(true);
            }else if( value.equals(female)){
                ((RadioButton)findViewById(R.id.radio_female)).setChecked(true);
            }else if (value.equals(trasngender)){
                ((RadioButton)findViewById(R.id.radio_transgender)).setChecked(true);
            }
            //Refresh the generated code
            refreshCode();
        }
    }


    /**
     * Init editText and listeners
     *
     */
    private void initTextValue(final EditCard editText, final int keyId, final int errorId) {
        //Has value? show it
        String value= getStringFromSharedPreference(keyId);
        if(!value.equals("")){
            editText.setText(value);
        }
        //Editable? add listener
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                putStringInSharedPreference(String.valueOf(s),keyId);
                if(!validateText(keyId)){
                    editText.setError(getApplicationContext().getString(errorId));
                }
                //Refresh the generated code
                refreshCode();
            }
        });

    }

    /**
     * Gets the string value for the given key
     * @return
     */
    private String getStringFromSharedPreference(int keyId){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(getApplicationContext().getResources().getString(keyId), "");
    }
    /**
     *  Puts the string value in the given key
     * @return
     */
    private void putStringInSharedPreference(String value, int keyId){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(getApplication().getBaseContext().getString(keyId), value);
        editor.commit();
    }
    /**
     *  Puts the Long value in the given key
     * @return
     */
    private void putLongInSharedPreferences(Long value, int keyId){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putLong(getApplication().getBaseContext().getString(keyId), value);
        editor.commit();
    }
    /**
     *  Puts the Long value in the given key
     * @return
     */
    private Long getLongFromSharedPreference(int keyId, Long defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getLong(getApplicationContext().getResources().getString(keyId), Long.parseLong(defaultValue+""));
    }

    /**
     *  On click on sex male this method save the male value
     * @return
     */
    public void onMaleClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_male), R.string.shared_key_sex);
        refreshCode();
    }


    /**
     *  On click on sex female this method save the female value
     * @return
     */
    public void onFemaleClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_female), R.string.shared_key_sex);
        refreshCode();
    }

    /**
     *  On click on sex transgender this method save the transgender value
     * @return
     */
    public void onTransgenderClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_transgender), R.string.shared_key_sex);
        refreshCode();
    }

    /**
     *  On click on copy button this method copy the code in the clipboard.
     * @return
     */
    public void copyCode(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText((getApplicationContext().getString(R.string.code_copy)), ((TextCard)findViewById(R.id.code_text)).getText());
        clipboard.setPrimaryClip(clip);
    }


    /**
     * DatepickerListener
     * @return
     */
    public class DatePickerListener implements Button.OnClickListener {
        int day,month,year;
        public DatePickerListener(View v) {
            Long defaultNoDate=Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
            Long timestamp=getLongFromSharedPreference(R.string.shared_key_timestamp_date,defaultNoDate);
            if(timestamp.equals(defaultNoDate)){
                //Set new calendar if the timestamp is a default date and set day,month,year.
                Calendar newCalendar = Calendar.getInstance();
                convertCalendarToLocalVariables(newCalendar);
            }
            else{
                //Parse the saved date in SharedPreference to calendar and set day,month,year.
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTimeInMillis(timestamp);
                convertCalendarToLocalVariables(newCalendar);
            }
            onClick(v);
        }

        @Override
        public void onClick(final View v) {
            if (!v.isShown()) {
                return;
            }
            DatePickerDialog.OnDateSetListener datepickerlistener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int newYear, int newMonthOfYear, int newDayOfMonth) {
                    Calendar newCalendar = Calendar.getInstance();
                    newCalendar.set(newYear, newMonthOfYear, newDayOfMonth);
                    convertCalendarToLocalVariables(newCalendar);
                    putLongInSharedPreferences(newCalendar.getTimeInMillis(), R.string.shared_key_timestamp_date);
                    recoveryAndShowDate();
                    if(!validateDate()){
                        TextCard textView = (TextCard)findViewById(R.id.date_header);
                        textView.setError(getApplicationContext().getString(R.string.date_error));
                        textView.callOnClick();
                        textView.requestLayout();
                    }else {
                        TextCard textView = (TextCard)findViewById(R.id.date_header);
                        textView.setError(null);
                        //Refresh the generated code
                        refreshCode();
                    }
                }
            };

            //Init a datepicker with the old values if exist, of with new values.
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), datepickerlistener, year, month-1, day);
            datePickerDialog.show();

            //Hide the week numbers on the datepickerdialog
            try {
                if(datePickerDialog.getDatePicker().getCalendarView()!=null)
                    datePickerDialog.getDatePicker().getCalendarView().setShowWeekNumber(false);
                //In API23+ the showweeknumber is deprecated and week numbers is not shown in the phone but the application crash
                //https://developer.android.com/reference/android/widget/CalendarView.html#setShowWeekNumber(boolean)
            }catch (UnsupportedOperationException e) {
                e.printStackTrace();
            }

        }

        /**
         *  Used to set the datepicker local variables of  day/month/year
         * @return
         */
        private void convertCalendarToLocalVariables(Calendar calendar) {
            day = getDay(calendar);
            month = getMonth(calendar);
            year = getYear(calendar);
        }
    }

    /**
     *  Set date in day/month/year textviews
     * @return
     */
    private void setDateInViews(String calendarDay, String calendarMonth, String calendarYear) {
        TextCard dayTextCard =(TextCard) findViewById(R.id.day_value);
        TextCard monthTextCard =(TextCard) findViewById(R.id.month_value);
        TextCard yearTextCard =(TextCard) findViewById(R.id.year_value);
        if(calendarDay.length()==1){
            calendarDay="0"+calendarDay;
        }

        if(calendarMonth.length()==1){
            calendarMonth="0"+calendarMonth;
        }

        if(calendarYear.length()==1){
            calendarYear="0"+calendarYear;
        }
        dayTextCard.setText(calendarDay+"");
        monthTextCard.setText(calendarMonth+"");
        yearTextCard.setText(calendarYear+"");
    }

    /**
     *  Recovery date from sharedPreferences and show in textviews
     * @return
     */
    private void recoveryAndShowDate() {
        Long defaultNoDate=Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
        Long timestamp=getLongFromSharedPreference(R.string.shared_key_timestamp_date,defaultNoDate);
        Calendar calendar;
        if(timestamp.equals(defaultNoDate)){
             calendar = Calendar.getInstance();
        }
        else{
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
        }
        int day = getDay(calendar);
        int month = getMonth(calendar);
        int year = getYear(calendar);
        setDateInViews(day+"", month+"", year+"");
    }
    /**
     *  Returns the year in a calendar date
     * @return
     */
    private int getYear(Calendar newCalendar) {
        return newCalendar.get(Calendar.YEAR);
    }

    /**
     *  Returns the month in a calendar date
     * @return
     */
    private int getMonth(Calendar newCalendar) {
        return newCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     *  Returns the day in a calendar date
     * @return
     */
    private int getDay(Calendar newCalendar) {
        return newCalendar.get(Calendar.DAY_OF_MONTH);
    }
}
