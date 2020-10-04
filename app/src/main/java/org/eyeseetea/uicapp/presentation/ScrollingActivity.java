package org.eyeseetea.uicapp.presentation;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.eyeseetea.uicapp.domain.CodeGenerator;
import org.eyeseetea.uicapp.R;
import org.eyeseetea.uicapp.Utils;
import org.eyeseetea.uicapp.domain.Client;
import org.eyeseetea.uicapp.domain.CodeResult;
import org.eyeseetea.uicapp.presentation.views.CustomButton;
import org.eyeseetea.uicapp.presentation.views.EditCard;
import org.eyeseetea.uicapp.presentation.views.TextCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    ViewHolders viewHolders;
    public static final String DEFAULT_VALUE = "";
    //Flag to prevent the bad positive errors in the validation when the user clear all the fields
    public static boolean isValidationErrorActive = true;

    private CodeGenerator codeGenerator = new CodeGenerator();

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
            Utils.showAlertWithHtmlMessageAndLastCommit(R.string.action_about_us, R.raw.about,
                    this);
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        initViews();
        createActionBar();
        initValues();
        refreshCode();
        hideKeyboardEvent();
        hideCollapsingBar();
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

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void refreshCode() {
        TextCard textView = viewHolders.code;

        Client client = getClient();

        CodeResult codeResult = codeGenerator.generateCode(client);

        if (codeResult.isValid()) {
            textView.setText(((CodeResult.Success) codeResult).getCode());
            viewHolders.codeButton.setEnabled(true);
        } else {
            textView.setText(getApplicationContext().getString(R.string.code_invalid));
            viewHolders.codeButton.setEnabled(false);
        }
    }

    private Client getClient() {
        String mother = getStringFromSharedPreference(R.string.shared_key_mother, DEFAULT_VALUE);
        String surname = getStringFromSharedPreference(R.string.shared_key_surname, DEFAULT_VALUE);

        String district = getStringFromSharedPreference(R.string.shared_key_district,
                DEFAULT_VALUE);

        if (district.equals(getString(R.string.default_district))) {
            district = DEFAULT_VALUE;
        }

        Long defaultNoDate = Long.parseLong(getString(R.string.default_no_date));
        Long dateOfBirth = getLongFromSharedPreference(R.string.shared_key_timestamp_date,
                defaultNoDate);

        String sex = getStringFromSharedPreference(R.string.shared_key_sex, DEFAULT_VALUE);

        boolean isTwin = getBooleanFromSharedPreference(R.string.shared_key_twin_checkbox, false);

        int twinNumber = tryParseInt(
                getStringFromSharedPreference(R.string.shared_key_twin_dropdown, "0"));


        return new Client(mother, surname, district, dateOfBirth, sex, isTwin, twinNumber);
    }

    public int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    /**
     * Creates the menu actionBar
     */
    private void createActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Init the saved or new values and add the listeners in the app components
     */
    private void initValues() {
        initTextValue(viewHolders.motherName, R.string.shared_key_mother, R.string.mother_error);
        initTextValue(viewHolders.surname, R.string.shared_key_surname, R.string.surname_error);
        initDropDown(viewHolders.district, R.string.shared_key_district, R.array.district_list,
                R.string.default_district);
        initDropDown(viewHolders.twinDropdown, R.string.shared_key_twin_dropdown, R.array.twin_list,
                R.string.default_twin);
        initDate();
        initSex(R.string.shared_key_sex);
        initTwin();
    }

    private void initTwin() {
        if (getBooleanFromSharedPreference(R.string.shared_key_twin_checkbox, false)) {
            viewHolders.twinCheckBox.setChecked(true);
            viewHolders.twinDropdownContainer.setVisibility(View.VISIBLE);
            String spinnerValue = getStringFromSharedPreference(R.string.shared_key_twin_dropdown,
                    getString(R.string.default_twin));
            for (int i = 0; i < viewHolders.twinDropdown.getCount(); i++) {
                if (viewHolders.twinDropdown.getItemAtPosition(i).toString().equals(spinnerValue)) {
                    viewHolders.twinDropdown.setSelection(i);
                    break;
                }
            }
        }
    }

    private void initDropDown(final Spinner spinner, final int keyId, int list_key,
            int id_default) {
        String defaultValue = getString(id_default);
        String value = getStringFromSharedPreference(keyId, defaultValue);

        List<String> itemsList = new ArrayList<>(
                Arrays.asList(getResources().getStringArray(list_key)));

        Collections.sort(itemsList);

        itemsList.add(0, defaultValue);

        ArrayAdapter<String> districtsList = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, itemsList);

        spinner.setAdapter(districtsList);
        if (!value.equals("")) {
            for (int i = 0; i < spinner.getCount(); i++) {
                if (value.equals(spinner.getAdapter().getItem(i).toString())) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                putStringInSharedPreference(spinner.getSelectedItem().toString(), keyId);
                refreshCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initDate() {
        EditCard dateEditCard = viewHolders.date;
        dateEditCard.setInputType(0);
        dateEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolders.date.requestFocus();
                showDatePicker(v);
            }
        });
        //This listener solve a problem when the user click on dateEditText but other editText
        // has the focus.
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
        String value = getStringFromSharedPreference(keyId, DEFAULT_VALUE);
        //Set value if exist in sharedPreferences
        if (!value.equals(DEFAULT_VALUE)) {
            final String male = getApplication().getApplicationContext().getString(
                    R.string.sex_male);
            String female = getApplication().getApplicationContext().getString(R.string.sex_female);
            String trasngender = getApplication().getApplicationContext().getString(
                    R.string.sex_transgender);
            if (value.equals(male)) {
                onMaleClicked(null);
            } else if (value.equals(female)) {
                onFemaleClicked(null);
            } else if (value.equals(trasngender)) {
                onTransgenderClicked(null);
            } else {
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
     */
    private void initTextValue(final EditCard editText, final int keyId, final int errorId) {
        //Has value? show it
        String value = getStringFromSharedPreference(keyId, DEFAULT_VALUE);
        if (!value.equals(DEFAULT_VALUE)) {
            editText.setText(value);
        }
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
                putStringInSharedPreference(String.valueOf(s), keyId);
                if (!codeGenerator.isValidMotherName(String.valueOf(s))
                        && isValidationErrorActive) {
                    editText.setError(getApplicationContext().getString(errorId));
                } else {
                    editText.setError(null);
                }
                //Refresh the generated code
                refreshCode();
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == editText.getId() && !hasFocus) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });

    }


    private boolean getBooleanFromSharedPreference(int keyId, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        return sharedPreferences.getBoolean(getApplicationContext().getResources().getString(keyId),
                defaultValue);
    }

    /**
     * Puts the string value in the given key
     */
    private void putBooleanInSharedPreference(int keyId, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getApplication().getBaseContext().getString(keyId), value);
        editor.commit();
    }

    /**
     * Gets the string value for the given key
     */
    private String getStringFromSharedPreference(int keyId, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        return sharedPreferences.getString(getApplicationContext().getResources().getString(keyId),
                defaultValue);
    }

    /**
     * Puts the string value in the given key
     */
    private void putStringInSharedPreference(String value, int keyId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getApplication().getBaseContext().getString(keyId), value);
        editor.commit();
    }

    /**
     * Puts the Long value in the given key
     */
    private void putLongInSharedPreferences(Long value, int keyId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(getApplication().getBaseContext().getString(keyId), value);
        editor.commit();
    }

    /**
     * Puts the Long value in the given key
     */
    private Long getLongFromSharedPreference(int keyId, Long defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        return sharedPreferences.getLong(getApplicationContext().getResources().getString(keyId),
                Long.parseLong(defaultValue + ""));
    }

    /**
     * On click on sex male this method save the male value
     */
    public void onMaleClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_male),
                R.string.shared_key_sex);
        viewHolders.male.setActivated(true);
        viewHolders.female.setActivated(false);
        viewHolders.transgender.setActivated(false);
        refreshCode();
    }


    /**
     * On click on sex female this method save the female value
     */
    public void onFemaleClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_female),
                R.string.shared_key_sex);
        viewHolders.male.setActivated(false);
        viewHolders.female.setActivated(true);
        viewHolders.transgender.setActivated(false);
        refreshCode();
    }

    /**
     * On click on sex transgender this method save the transgender value
     */
    public void onTransgenderClicked(View view) {
        putStringInSharedPreference(getApplicationContext().getString(R.string.sex_transgender),
                R.string.shared_key_sex);
        viewHolders.male.setActivated(false);
        viewHolders.female.setActivated(false);
        viewHolders.transgender.setActivated(true);
        refreshCode();
    }

    /**
     * On click on copy button this method copy the code in the clipboard.
     */
    public void copyCode(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                (getApplicationContext().getString(R.string.code_copy)),
                viewHolders.code.getText());
        clipboard.setPrimaryClip(clip);
    }

    /**
     * Clear shared preferences and reInit values and refresh generated code.
     */
    public void clearFields(View view) {
        isValidationErrorActive = false;
        viewHolders.motherName.setText(DEFAULT_VALUE);
        putStringInSharedPreference(DEFAULT_VALUE, R.string.shared_key_mother);
        viewHolders.surname.setText(DEFAULT_VALUE);
        putStringInSharedPreference(DEFAULT_VALUE, R.string.shared_key_surname);
        viewHolders.district.setSelection(0);
        putStringInSharedPreference(DEFAULT_VALUE, R.string.shared_key_district);

        viewHolders.male.setActivated(false);
        viewHolders.female.setActivated(false);
        viewHolders.transgender.setActivated(false);

        putStringInSharedPreference(DEFAULT_VALUE, R.string.shared_key_sex);
        Long defaultNoDate = Long.parseLong(
                getApplicationContext().getString(R.string.default_no_date));
        putLongInSharedPreferences(defaultNoDate, R.string.shared_key_timestamp_date);

        putStringInSharedPreference(getString(R.string.default_twin),
                R.string.shared_key_twin_dropdown);
        viewHolders.twinDropdownContainer.setVisibility(View.GONE);

        viewHolders.twinCheckBox.setChecked(false);
        putBooleanInSharedPreference(R.string.shared_key_twin_checkbox, false);

        refreshCode();
        isValidationErrorActive = true;
        //move to up
        scrollUp();
    }

    private void scrollUp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((NestedScrollView) findViewById(R.id.nested_scroll_view)).fullScroll(
                        ScrollView.FOCUS_UP);
            }
        });
    }

    private void hideCollapsingBar() {
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).setExpanded(false, false);

    }

    /**
     * Date editText listener
     */
    public void showDatePicker(View view) {
        new DatePickerListener(view);
    }

    public void changeTwinValues(View view) {
        boolean isChecked = viewHolders.twinCheckBox.isChecked();
        putBooleanInSharedPreference(R.string.shared_key_twin_checkbox, isChecked);
        if (isChecked) {
            viewHolders.twinDropdownContainer.setVisibility(View.VISIBLE);
            ((Spinner) viewHolders.twinDropdown.findViewById(R.id.twin_dropdown)).setSelection(0);
        } else {
            //Removes the spinner saved values
            putStringInSharedPreference(getString(R.string.default_twin),
                    R.string.shared_key_twin_dropdown);
            viewHolders.twinDropdownContainer.setVisibility(View.GONE);
        }
        refreshCode();
    }

    public void showMotherInfo(View view) {
        showSnackBar(R.string.mother_info);
    }

    public void showDistrictOfBirthInfo(View view) {
        showSnackBar(R.string.district_of_birth_info);
    }

    public void showDateInfo(View view) {
        showSnackBar(R.string.date_of_birth_info);
    }

    public void showTwinInfo(View view){
        showSnackBar(R.string.twin_info);
    }

    public void showBirthInfo(View view){
        showSnackBar(R.string.birth_order_info);
    }

    private void showSnackBar(int messageId) {
        final Snackbar sb = Snackbar
                .make(viewHolders.coordinator, messageId, Snackbar.LENGTH_INDEFINITE);
        View sbView = sb.getView();
        TextView textView = (TextView) sbView.findViewById(
                R.id.snackbar_text);
        //increase max lines of text in snackbar. default is 2.
        textView.setMaxLines(20);
        sb.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb.dismiss();
            }
        });
        sb.show();
    }




    /**
     * DatepickerListener
     *
     * @return
     */
    public class DatePickerListener implements Button.OnClickListener {
        int day, month, year;

        public DatePickerListener(View v) {
            Long defaultNoDate = Long.parseLong(
                    getApplicationContext().getString(R.string.default_no_date));
            Long timestamp = getLongFromSharedPreference(R.string.shared_key_timestamp_date,
                    defaultNoDate);
            if (timestamp.equals(defaultNoDate)) {
                //Set new calendar if the timestamp is a default date and set day,month,year.
                Calendar newCalendar = Calendar.getInstance();
                convertCalendarToLocalVariables(newCalendar);
            } else {
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
            DatePickerDialog.OnDateSetListener datepickerlistener =
                    new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int newYear, int newMonthOfYear,
                                int newDayOfMonth) {
                            Calendar newCalendar = Calendar.getInstance();
                            newCalendar.set(newYear, newMonthOfYear, newDayOfMonth);
                            convertCalendarToLocalVariables(newCalendar);
                            putLongInSharedPreferences(newCalendar.getTimeInMillis(),
                                    R.string.shared_key_timestamp_date);
                            recoveryAndShowDate();
                            if (!codeGenerator.isValidDateOfBirth(newCalendar.getTimeInMillis())) {
                                EditCard editCard = viewHolders.date;
                                editCard.setError(
                                        getApplicationContext().getString(R.string.date_error));
                            } else {
                                EditCard editCard = viewHolders.date;
                                editCard.setError(null);
                                //Refresh the generated code
                            }
                            refreshCode();
                        }
                    };

            //Init a datepicker with the old values if exist, of with new values.
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    datepickerlistener, year, month - 1, day);
            datePickerDialog.show();

            //Hide the week numbers on the datepickerdialog
            try {
                if (datePickerDialog.getDatePicker().getCalendarView() != null) {
                    datePickerDialog.getDatePicker().getCalendarView().setShowWeekNumber(false);
                }
                //In API23+ the showweeknumber is deprecated and week numbers is not shown in the
                // phone but the application crash
                //https://developer.android.com/reference/android/widget/CalendarView
                // .html#setShowWeekNumber(boolean)
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            }

        }

        /**
         * Used to set the datepicker local variables of  day/month/year
         */
        private void convertCalendarToLocalVariables(Calendar calendar) {
            day = Utils.getDay(calendar);
            month = Utils.getMonth(calendar);
            year = Utils.getYear(calendar);
        }
    }

    /**
     * Set date in day/month/year textviews
     */
    private void showDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
        String calendarDay = simpleDateFormat.format(calendar.getTime());
        viewHolders.date.setText(calendarDay);
    }

    /**
     * Recovery date from sharedPreferences and show in textviews
     */
    private void recoveryAndShowDate() {
        Long defaultNoDate = Long.parseLong(
                getApplicationContext().getString(R.string.default_no_date));
        Long timestamp = getLongFromSharedPreference(R.string.shared_key_timestamp_date,
                defaultNoDate);
        Calendar calendar;
        if (timestamp.equals(defaultNoDate)) {
            calendar = Calendar.getInstance();
        } else {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
        }
        showDate(calendar);
    }

    /**
     * Init holders on holders class
     */
    public void initViews() {
        if (viewHolders == null) {
            viewHolders = new ViewHolders();
        }
        if (viewHolders.coordinator == null) {
            viewHolders.coordinator = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        }
        if (viewHolders.motherName == null) {
            viewHolders.motherName = (EditCard) findViewById(R.id.mother_edit_text);
        }
        if (viewHolders.surname == null) {
            viewHolders.surname = (EditCard) findViewById(R.id.surname_edit_text);
        }
        if (viewHolders.district == null) {
            viewHolders.district = (Spinner) findViewById(R.id.district_dropdown);
        }
        if (viewHolders.date == null) {
            viewHolders.date = (EditCard) findViewById(R.id.date_value);
        }
        if (viewHolders.male == null) {
            viewHolders.male = (CustomButton) (findViewById(R.id.radio_male));
        }
        if (viewHolders.female == null) {
            viewHolders.female = (CustomButton) (findViewById(R.id.radio_female));
        }
        if (viewHolders.transgender == null) {
            viewHolders.transgender = (CustomButton) (findViewById(R.id.radio_transgender));
        }
        if (viewHolders.code == null) {
            viewHolders.code = (TextCard) findViewById(R.id.code_text);
        }
        if (viewHolders.codeButton == null) {
            viewHolders.codeButton = (ImageView) findViewById(R.id.code_button);
        }
        if (viewHolders.twinCheckBox == null) {
            viewHolders.twinCheckBox = (CheckBox) findViewById(R.id.twin_checkbox);
        }
        if (viewHolders.twinDropdown == null) {
            viewHolders.twinDropdown = (Spinner) findViewById(R.id.twin_dropdown);
        }

        if (viewHolders.twinDropdownContainer == null){
            viewHolders.twinDropdownContainer = findViewById(R.id.twin_dropdown_container);
        }
    }

    /**
     * Holders to boost views access avoiding to findById so often
     */
    static class ViewHolders {
        EditCard motherName;
        EditCard surname;
        EditCard date;
        Spinner district;
        CustomButton male;
        CustomButton female;
        CustomButton transgender;
        TextCard code;
        ImageView codeButton;
        CheckBox twinCheckBox;
        Spinner twinDropdown;
        LinearLayout twinDropdownContainer;
        CoordinatorLayout coordinator;
    }
}
