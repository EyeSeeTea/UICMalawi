package org.eyeseetea.uicapp;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.eyeseetea.uicapp.views.CustomButton;
import org.eyeseetea.uicapp.views.EditCard;
import org.eyeseetea.uicapp.views.TextCard;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScrollingActivity extends AppCompatActivity {

    ViewHolders viewHolders;

    //Flag to prevent the bad positive errors in the validation when the user clear all the fields
    public static boolean isValidationErrorActive =true;

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
            Utils.showAlertWithHtmlMessageAndLastCommit(R.string.action_about_us, R.raw.about, this);
            return true;
        }
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_scrolling);
        initViews();
        createActionBar();
        initValues();
        refreshCode();
        hideKeyboardEvent();
    }



    private void hideKeyboardEvent() {
        (findViewById(R.id.container_scrolled)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(v);
                return true;
            }
        });

    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void refreshCode() {
        TextCard textView = viewHolders.code;
        if(validateAllFields()){
            textView.setText(generateCode());
            viewHolders.codeButton.setEnabled(true);
        } else {
            textView.setText(getApplicationContext().getString(R.string.code_invalid));
            viewHolders.codeButton.setEnabled(false);
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

        String day = String.format("%02d", Utils.getDay(newCalendar));
        String month = String.format("%02d", Utils.getMonth(newCalendar));
        String year = String.valueOf(Utils.getYear(newCalendar));

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
        //Not pass the validation if the saved data is bigger than today.
        if(savedDate.getTimeInMillis()>= Utils.getTodayFirstTimestamp().getTime()){
            return false;
        }
        return true;
    }

    private boolean validateText(int keyId) {
        String value = getStringFromSharedPreference(keyId);
        //At least two characters without numbers and with possible blank spaces
        String regExp="^[ A-zÀ-ÿ]*([A-zÀ-ÿ]{1,}[ ]*[A-zÀ-ÿ]{1,})[ A-zÀ-ÿ]*$";
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_arrow);
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
        initTextValue(viewHolders.motherName, R.string.shared_key_mother, R.string.mother_error);
        //Init surname
        initTextValue(viewHolders.surname, R.string.shared_key_surname, R.string.surname_error);
        //Init district
        initTextValue(viewHolders.district, R.string.shared_key_district, R.string.district_error);

        //Init district
        initDate();

        //Init sex
        initSex(R.string.shared_key_sex);
    }

    private void initDate() {
        EditCard dateEditCard= viewHolders.date;
        dateEditCard.setInputType(0);
        dateEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolders.date.requestFocus();
                showDatePicker(v);
            }
        });
        //This listener solve a problem when the user click on dateEditText but other editText has the focus.
        dateEditCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewHolders.date.requestFocus();
                return false;
            }
        });
        recoveryAndShowDate();
    }

    private void initSex(final int keyId) {
        String value= getStringFromSharedPreference(keyId);
        //Set value if exist in sharedPreferences
        if(!value.equals("")){
            final String male=getApplication().getApplicationContext().getString(R.string.sex_male);
            String female=getApplication().getApplicationContext().getString(R.string.sex_female);
            String trasngender=getApplication().getApplicationContext().getString(R.string.sex_transgender);
            if(value.equals(male)){
                onMaleClicked(null);
            }else if( value.equals(female)){
                onFemaleClicked(null);
            }else if (value.equals(trasngender)){
                onTransgenderClicked(null);
            }
            else{
                viewHolders.male.setEnabled(false);
                viewHolders.female.setEnabled(false);
                viewHolders.transgender.setEnabled(false);
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
        editText.setFilters(new InputFilter[] { Utils.filter });
        //Editable? add listener
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Ignore the clear fields validation.
                String oldValue=getStringFromSharedPreference(keyId);
                putStringInSharedPreference(String.valueOf(s),keyId);
                if(!validateText(keyId) && isValidationErrorActive){
                    editText.setError(getApplicationContext().getString(errorId));
                }
                else{
                    editText.setError(null);
                }
                //Refresh the generated code
                refreshCode();
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId() == editText.getId() && !hasFocus) {

                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
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
        viewHolders.male.setActivated(true);
        viewHolders.female.setActivated(false);
        viewHolders.transgender.setActivated(false);
        refreshCode();
    }


    /**
     *  On click on sex female this method save the female value
     * @return
     */
    public void onFemaleClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_female), R.string.shared_key_sex);
        viewHolders.male.setActivated(false);
        viewHolders.female.setActivated(true);
        viewHolders.transgender.setActivated(false);
        refreshCode();
    }

    /**
     *  On click on sex transgender this method save the transgender value
     * @return
     */
    public void onTransgenderClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_transgender), R.string.shared_key_sex);
        viewHolders.male.setActivated(false);
        viewHolders.female.setActivated(false);
        viewHolders.transgender.setActivated(true);
        refreshCode();
    }

    /**
     *  On click on copy button this method copy the code in the clipboard.
     * @return
     */
    public void copyCode(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText((getApplicationContext().getString(R.string.code_copy)), viewHolders.code.getText());
        clipboard.setPrimaryClip(clip);
    }

    /**
     *  Clear shared preferences and reInit values and refresh generated code.
     * @return
     */
    public void clearFields(View view) {
        isValidationErrorActive=false;
        viewHolders.motherName.setText("");
        putStringInSharedPreference("", R.string.shared_key_mother);
        viewHolders.surname.setText("");
        putStringInSharedPreference("", R.string.shared_key_surname);
        viewHolders.district.setText("");
        putStringInSharedPreference("", R.string.shared_key_district);

        viewHolders.male.setActivated(false);
        viewHolders.female.setActivated(false);
        viewHolders.transgender.setActivated(false);

        putStringInSharedPreference("", R.string.shared_key_sex);
        Long defaultNoDate = Long.parseLong(getApplicationContext().getString(R.string.default_no_date));
        putLongInSharedPreferences(defaultNoDate, R.string.shared_key_timestamp_date);

        refreshCode();
        isValidationErrorActive=true;
        //move to up
        runOnUiThread( new Runnable(){
            @Override
            public void run(){
                ((NestedScrollView)findViewById(R.id.nested_scroll_view)).fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    /**
     *  Date editText listener
     * @return
     */
    public void showDatePicker(View view) {
        new DatePickerListener(view);
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
                        EditCard editCard = viewHolders.date;
                        editCard.setError(getApplicationContext().getString(R.string.date_error));
                    }else {
                        EditCard editCard = viewHolders.date;
                        editCard.setError(null);
                        //Refresh the generated code
                    }
                    refreshCode();
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
            day = Utils.getDay(calendar);
            month = Utils.getMonth(calendar);
            year = Utils.getYear(calendar);
        }
    }

    /**
     *  Set date in day/month/year textviews
     * @return
     */
    private void showDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("d MMM yyyy");
        String calendarDay= simpleDateFormat.format(calendar.getTime());
        viewHolders.date.setText(calendarDay);
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
        showDate(calendar);
    }

    /**
     * Init holders on holders class
     */
    public void initViews(){
        if (viewHolders == null) {
            viewHolders = new ViewHolders();
        }
        if (viewHolders.motherName==null){
            viewHolders.motherName = (EditCard) findViewById(R.id.mother_edit_text);
        }
        if (viewHolders.surname==null){
            viewHolders.surname = (EditCard) findViewById(R.id.surname_edit_text);
        }
        if (viewHolders.district==null){
            viewHolders.district = (EditCard) findViewById(R.id.district_edit_text);
        }
        if (viewHolders.date==null){
            viewHolders.date = (EditCard) findViewById(R.id.date_value);
        }
        if (viewHolders.male==null){
            viewHolders.male = (CustomButton) (findViewById(R.id.radio_male));
        }
        if (viewHolders.female==null){
            viewHolders.female = (CustomButton) (findViewById(R.id.radio_female));
        }
        if (viewHolders.transgender==null){
            viewHolders.transgender = (CustomButton) (findViewById(R.id.radio_transgender));
        }
        if (viewHolders.code==null){
            viewHolders.code = (TextCard) findViewById(R.id.code_text);
        }
        if (viewHolders.codeButton==null) {
            viewHolders.codeButton = (ImageView) findViewById(R.id.code_button);
        }
    }

    /**
     * Holders to boost views access avoiding to findById so often
     */
    static class ViewHolders{
        EditCard motherName;
        EditCard surname;
        EditCard district;
        EditCard date;
        CustomButton male;
        CustomButton female;
        CustomButton transgender;
        TextCard code;
        ImageView codeButton;
    }
}
