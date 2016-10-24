package org.eyeseetea.uicapp;

import android.app.DatePickerDialog;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

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
        setCodeInView();
    }

    private void setCodeInView() {
        TextView textView = (TextView) findViewById(R.id.code_text);
        if(validations()){
            textView.setText(generateCode());
        }
        else{
            textView.setText(getApplicationContext().getString(R.string.code_invalid));
        }
    }

    private String generateCode() {
        String code="";
        code = addCodeChars(code, R.string.shared_key_mother);
        code = addCodeChars(code, R.string.shared_key_surname);
        code = addCodeChars(code, R.string.shared_key_district);
        code = addCodeChars(code, R.string.shared_key_mother);

        Long defaultNoDate=Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
        Long timestamp = getLongFromSharedPreference(R.string.shared_key_timestamp_date, defaultNoDate);
        Calendar newCalendar= Calendar.getInstance();
        newCalendar.setTimeInMillis(timestamp);
        String day = String.valueOf(getDay(newCalendar));
        String month = String.valueOf(getMonth(newCalendar));
        String year = String.valueOf(getYear(newCalendar));
        if(day.length()<2){
            day="0"+day;
        }
        code = code + day;
        if(month.length()<2){
            month="0"+month;
        }
        code = code + month;
        code = code + year.substring(year.length()-2);
        code = code + getStringFromSharedPreference(R.string.shared_key_sex).substring(0,1);
        return code.toUpperCase();
    }

    @NonNull
    private String addCodeChars(String code, int keyId) {
        String temporalValue = getStringFromSharedPreference(keyId);
        code = code + temporalValue.substring(temporalValue.length()-2);
        return code;
    }

    private boolean validations() {

        if(!validateText(R.string.shared_key_mother)) {
            return false;
        }

        if(!validateText(R.string.shared_key_surname)) {
            return false;
        }

        if(!validateText(R.string.shared_key_district)) {
            return false;
        }

        if(!validateText(R.string.shared_key_sex)) {
            return false;
        }

        Long defaultNoDate=Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
        Long timestamp = getLongFromSharedPreference(R.string.shared_key_timestamp_date, defaultNoDate);
        if(timestamp.equals(defaultNoDate)){
            return false;
        }

        return true;
    }

    private boolean validateText(int keyId) {
        String value = getStringFromSharedPreference(keyId);

        if(value.length()>=2){
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
        initTextValue((EditText) findViewById(R.id.mother_edit_text), R.string.shared_key_mother);
        //Init surname
        initTextValue((EditText) findViewById(R.id.surname_edit_text), R.string.shared_key_surname);
        //Init district
        initTextValue((EditText) findViewById(R.id.district_edit_text), R.string.shared_key_district);

        //Init district
        initDate();

        //Init sex
        initSex(R.string.shared_key_sex);
    }

    private void initDate() {
        TextView dayTextView =(TextView) findViewById(R.id.day_value);
        TextView monthTextView =(TextView) findViewById(R.id.month_value);
        TextView yearTextView =(TextView) findViewById(R.id.year_value);
        recoveryAndShowDate();
        View.OnClickListener dateOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerListener(v);
            }
        };

        yearTextView.setOnClickListener(dateOnClickListener);
        monthTextView.setOnClickListener(dateOnClickListener);
        dayTextView.setOnClickListener(dateOnClickListener);
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
        }
    }


    /**
     * Init editText and listeners
     *
     */
    private void initTextValue(EditText editText, final int keyId) {
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
    }


    /**
     *  On click on sex female this method save the female value
     * @return
     */
    public void onFemaleClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_female), R.string.shared_key_sex);
    }

    /**
     *  On click on sex transgender this method save the transgender value
     * @return
     */
    public void onTransgenderClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_transgender), R.string.shared_key_sex);
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
                    Calendar today= Calendar.getInstance();
                    if(newCalendar.before(today)) {
                        convertCalendarToLocalVariables(newCalendar);
                        putLongInSharedPreferences(newCalendar.getTimeInMillis(), R.string.shared_key_timestamp_date);
                        recoveryAndShowDate();
                    }
                }
            };

            //Init a datepicker with the old values if exist, of with new values.
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), datepickerlistener, year, month, day);
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
        TextView dayTextView =(TextView) findViewById(R.id.day_value);
        TextView monthTextView =(TextView) findViewById(R.id.month_value);
        TextView yearTextView =(TextView) findViewById(R.id.year_value);
        if(calendarDay.length()==1){
            calendarDay="0"+calendarDay;
        }

        if(calendarMonth.length()==1){
            calendarMonth="0"+calendarMonth;
        }

        if(calendarYear.length()==1){
            calendarYear="0"+calendarYear;
        }
        dayTextView.setText(calendarDay+"");
        monthTextView.setText(calendarMonth+"");
        yearTextView.setText(calendarYear+"");
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
