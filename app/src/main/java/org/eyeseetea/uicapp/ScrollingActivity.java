package org.eyeseetea.uicapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        createActionBar();
        initValues();

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
        showDate();
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
        //Has value? show it
        String value= getStringFromSharedPreference(keyId);
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


    /**
     * Inits editText and listeners
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
     *  Puts the string value in the given key
     * @return
     */
    private void putLongInSharedPreferences(Long value, int keyId){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putLong(getApplication().getBaseContext().getString(keyId), value);
        editor.commit();
    }    /**
     *  Puts the string value in the given key
     * @return
     */
    private Long getLongFromSharedPreference(int keyId, Long defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getLong(getApplicationContext().getResources().getString(keyId), Long.parseLong(defaultValue+""));
    }
    
    public void onMaleClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_male), R.string.shared_key_sex);
    }

    public void onFemaleClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_female), R.string.shared_key_sex);
    }

    public void onTransgenderClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_transgender), R.string.shared_key_sex);
    }


    public class DatePickerListener implements Button.OnClickListener {

        int day,month,year;
        public DatePickerListener(View v) {
            Long defaultNoDate=Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
            Long timestamp=getLongFromSharedPreference(R.string.shared_key_timestamp_date,defaultNoDate);
            if(timestamp.equals(defaultNoDate)){
                Calendar newCalendar = Calendar.getInstance();
                day = newCalendar.get(Calendar.DAY_OF_MONTH);
                month = newCalendar.get(Calendar.MONTH) + 1;
                year = newCalendar.get(Calendar.YEAR);
            }
            else{
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTimeInMillis(timestamp);
                day = newCalendar.get(Calendar.DAY_OF_MONTH);
                month = newCalendar.get(Calendar.MONTH) + 1;
                year = newCalendar.get(Calendar.YEAR);
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
                        day = newCalendar.get(Calendar.DAY_OF_MONTH);
                        month = newCalendar.get(Calendar.MONTH) + 1; // Note: zero based!
                        year = newCalendar.get(Calendar.YEAR);
                        putLongInSharedPreferences(newCalendar.getTimeInMillis(), R.string.shared_key_timestamp_date);
                        showDate();
                    }
                }
            };
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
    }

    private void putDateInViews(String calendarDay, String calendarMonth, String calendarYear) {
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

    private void showDate() {
        Long defaultNoDate=Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
        Long timestamp=getLongFromSharedPreference(R.string.shared_key_timestamp_date,defaultNoDate);
        int day,month,year;
        if(timestamp.equals(defaultNoDate)){
            Calendar newCalendar = Calendar.getInstance();
            day = newCalendar.get(Calendar.DAY_OF_MONTH);
            month = newCalendar.get(Calendar.MONTH) + 1;
            year = newCalendar.get(Calendar.YEAR);
        }
        else{
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTimeInMillis(timestamp);
            day = newCalendar.get(Calendar.DAY_OF_MONTH);
            month = newCalendar.get(Calendar.MONTH) + 1;
            year = newCalendar.get(Calendar.YEAR);
        }
        putDateInViews(day+"", month+"", year+"");
    }
}
