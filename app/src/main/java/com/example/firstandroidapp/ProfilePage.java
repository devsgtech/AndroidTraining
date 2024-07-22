package com.example.firstandroidapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ProfilePage extends AppCompatActivity {

    // Tag for logging purposes
    private final String TAG = "REGISTER_PAGE";
    // Flag to track input validation status
    private boolean isValid;
    // Views and Widgets
    private TextInputLayout fnameLayout, emailLayout, passwordLayout, confirmPasswordLayout, contactNumberLayout, dateSelectionLayout;
    private TextInputEditText fnameEditText, lnameEditText, emailEditText, contactNumber, password, confirmPassword, dateSelection;
    private Button btnUpdate;
    private Spinner countryDropdownSpinner;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton, othersRadioButton;
    private CheckBox showPasswordCheckbox;
    private TextView genderHeading, radioButtonErrorTextView, agreeTermsErrorTextView, goToLogin;
    private ArrayAdapter<String> adapter;
    private static final String defaultString = "";
    private Intent intent;
    private View genderRadioGroupBottomView;
    private Boolean proceedTheValidations = true;
    private final String passwordMatcher = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private final String numberMatcher = "^[0-9]{0,10}$";
    private boolean dateIsValid = true;
    private DataBaseHandler dataBaseHandler;
    private String passwordToBeSaved;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView uploadImage, profileImageView;
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        // Setting the Toolbar as the app bar for the activity
        setSupportActionBar(findViewById(R.id.my_toolbar));
        // Setting the title for the app bar
        getSupportActionBar().setTitle("My Profile");

        init();
        // getCurrentFocus(); is used to get the "VIEW"
        buttonClick();
        // Setting the local storage values.
        setLocalStorageValues();
        // To show the password fields.
        showPasswordFields();
        // Text watcher for the inout fields.
        textWatchListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // To get all the user on the start of the app in logcat.
        dataBaseHandler.getAllUsers();
    }

    // To set the values
    private void setLocalStorageValues() {
        fnameEditText.setText(sharedPreferences.getString(Utility.firstNameKey, ""));
        lnameEditText.setText(sharedPreferences.getString(Utility.lastNameKey, ""));
        emailEditText.setText(sharedPreferences.getString(Utility.emailAddressKey, ""));
        contactNumber.setText(sharedPreferences.getString(Utility.contactNumberKey, ""));
        dateSelection.setText(sharedPreferences.getString(Utility.dateOfBirthKey, ""));

        if (sharedPreferences.getString(Utility.countryKey, "").equals("Australia")) {
            countryDropdownSpinner.setSelection(1);
        } else if (sharedPreferences.getString(Utility.countryKey, "").equals("India")) {
            countryDropdownSpinner.setSelection(2);
        } else if (sharedPreferences.getString(Utility.countryKey, "").equals("Japan")) {
            countryDropdownSpinner.setSelection(3);
        } else if (sharedPreferences.getString(Utility.countryKey, "").equals("United Kingdom")) {
            countryDropdownSpinner.setSelection(4);
        } else if (sharedPreferences.getString(Utility.countryKey, "").equals("United States of America")) {
            countryDropdownSpinner.setSelection(5);
        }

        if (sharedPreferences.getString(Utility.genderKey, "").equals("Male")) {
            maleRadioButton.setChecked(true);
        } else if (sharedPreferences.getString(Utility.genderKey, "").equals("Female")) {
            femaleRadioButton.setChecked(true);
        } else if (sharedPreferences.getString(Utility.genderKey, "").equals("Others")) {
            othersRadioButton.setChecked(true);
        }

    }

    // Method to initialize views and widgets
    private void init() {

        // Link the variables to the views in the layout
        fnameLayout = findViewById(R.id.fnameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        fnameEditText = findViewById(R.id.fname);
        lnameEditText = findViewById(R.id.lname);
        emailEditText = findViewById(R.id.email);
        btnUpdate = findViewById(R.id.btnUpdate);
        countryDropdownSpinner = findViewById(R.id.countryDropdownSpinner);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        othersRadioButton = findViewById(R.id.othersRadioButton);
        showPasswordCheckbox = findViewById(R.id.showPasswordCheckbox);
        contactNumberLayout = findViewById(R.id.contactNumberLayout);
        contactNumber = findViewById(R.id.contactNumber);
        radioButtonErrorTextView = findViewById(R.id.radioButtonErrorTextView);
        agreeTermsErrorTextView = findViewById(R.id.agreeTermsErrorTextView);
        goToLogin = findViewById(R.id.goToLogin);
        genderHeading = findViewById(R.id.genderHeading);
        genderRadioGroupBottomView = findViewById(R.id.genderRadioGroupBottomView);
        contactNumber = findViewById(R.id.contactNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        dateSelection = findViewById(R.id.dateSelection);
        dateSelectionLayout = findViewById(R.id.dateSelectionLayout);
        dataBaseHandler = new DataBaseHandler(ProfilePage.this);
        uploadImage = findViewById(R.id.uploadImage);
        profileImageView = findViewById(R.id.profileImageView);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_list, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        countryDropdownSpinner.setAdapter(adapter);

        // On click listener for the Date Selection
        dateSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        // Shared Preference.
        sharedPreferences = getSharedPreferences("login_details", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    //Method for the validation of the date input
    private void datePicker() {
        final Calendar c = Calendar.getInstance();

        // on below line we are getting our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                ProfilePage.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //To get the current date
                        Calendar currentDate = Calendar.getInstance();

                        // To get the choosen date
                        Calendar chosenDate = Calendar.getInstance();
                        chosenDate.set(year, monthOfYear, dayOfMonth);

                        // Checking the difference in milliSeconds.
                        long diffInMillis = Math.abs(currentDate.getTimeInMillis() - chosenDate.getTimeInMillis());

                        // Convert the difference to days
                        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

                        if (diffInDays < 6575) {
                            dateIsValid = false;
                        } else {
                            dateIsValid = true;
                        }

                        // on below line we are setting date to our text view.
                        dateSelection.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                },
                // on below line we are passing year, month and day for selected date in our date picker.
                year, month, day);

        //To set the maximum selection to the current date.
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        // at last we are calling show display our date picker dialog.
        datePickerDialog.show();
    }

    // Method to set up onClick listener on the Update button
    private void buttonClick() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlertDialogBoxCreationForUpdation(view);
            }
        });

    }

    private void showPasswordFields() {
        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    passwordLayout.setVisibility(View.VISIBLE);
                    confirmPasswordLayout.setVisibility(View.VISIBLE);
                } else if(!buttonView.isChecked()) {
                    passwordLayout.setVisibility(View.GONE);
                    confirmPasswordLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //------------ S A V E  D A T A ------------
    // Method to save data to Database.
    private void saveData(View view) {
        // Save the selected country
        String selectedCountry = countryDropdownSpinner.getSelectedItem().toString() == "Select Country" ? "" : countryDropdownSpinner.getSelectedItem().toString();
        // Save the selected gender
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        String selectedGender = "";
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedGenderId);
            selectedGender = selectedRadioButton.getText().toString();
        }
        // For storing the Hashed password.
        try {
            if(showPasswordCheckbox.isChecked()){
                passwordToBeSaved = Utility.passwordHashing(password.getText().toString().trim());
                dataBaseHandler.updateUserWithPassword(view, getApplicationContext(), fnameEditText.getText().toString().trim(), lnameEditText.getText().toString().trim(), emailEditText.getText().toString().trim().toLowerCase(), contactNumber.getText().toString().trim(), dateSelection.getText().toString().trim(), selectedCountry.toString().trim(), selectedGender.toString().trim(), passwordToBeSaved, passwordToBeSaved);
            }else{
                dataBaseHandler.updateUserWithoutPassword(view, getApplicationContext(), fnameEditText.getText().toString().trim(), lnameEditText.getText().toString().trim(), emailEditText.getText().toString().trim().toLowerCase(), contactNumber.getText().toString().trim(), dateSelection.getText().toString().trim(), selectedCountry.toString().trim(), selectedGender.toString().trim());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // Save data to the SQLite Database.
        dataBaseHandler.getAllUsers();
    }

    // Method to validate user inputs
    private boolean validDataInputs(View v) {

        // Validate first name.
        if (fnameEditText.getText().toString().isEmpty()) {
            fnameLayout.setError(getString(R.string.first_name_required));
            isValid = false;
        } else {
            fnameLayout.setError(null);
            isValid = true;
        }

        //Validation for the contact number
        if (contactNumber.getText().toString().trim().length() > 10) {
            isValid = false;
        }

        // Do the conformation if the checkbox is enabled.
        if(showPasswordCheckbox.isChecked()){
            // Validation for the Password.
            if (password.getText().toString().trim().isEmpty()) {
                passwordLayout.setError(getString(R.string.password_required));
                isValid = false;
            }

            // Validation for the Confirm Password.
            if (confirmPassword.getText().toString().trim().isEmpty()  ) {
                confirmPasswordLayout.setError(getString(R.string.confirm_password_required));
                isValid = false;
            }

            // Validate password and confirm password.
            if (!password.getText().toString().trim().isEmpty() && !confirmPassword.getText().toString().trim().isEmpty()) {
                if (!password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                    Utility.displayErrorSnackbar(getCurrentFocus(), getString(R.string.passwords_do_not_match), getApplicationContext());
                    isValid = false;
                }
            }

        }

        return isValid;
    }


    // Method to set up text change listeners for input fields
    private void textWatchListener() {

        // Listener for first name input field
        fnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fnameEditText.setCursorVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear any previous error when user starts typing
                fnameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Listener for last name input field
        lnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lnameEditText.setCursorVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Listener for Contact Number input field
        contactNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactNumberLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!contactNumber.getText().toString().trim().isEmpty()) {
                    if (!Utility.validator(s.toString().trim(), numberMatcher)) {
                        contactNumberLayout.setError("Should contain utmost 10 number digits");
                    }
                }
            }
        });

        // Listener for Date Selection field
        dateSelection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateSelectionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!dateSelection.getText().toString().isEmpty()) {
                    if (!dateIsValid) {
                        dateSelectionLayout.setError("Age should be 18 years or above.");

                    }
                }
            }
        });

        // Listener for the password
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (proceedTheValidations) {
                    if (!Utility.validator(s.toString().trim(), passwordMatcher)) {
                        passwordLayout.setError("Password is Invalid.");
                    }
                }
            }
        });

        // Listener for the confirm password
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (proceedTheValidations) {
                    if (!Utility.validator(s.toString().trim(), passwordMatcher)) {
                        confirmPasswordLayout.setError("Confirm Password is Invalid");
                    }
                }

            }
        });

    }

    //------------ M E N U  C R E A T I O N ------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Inbuilt method for onClick Listener for the menu options.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item != null) {
            switch (item.getItemId()) {
                case R.id.logoutButton:
                    // your logic here.
                    customAlertDialogBoxCreationForLogout();
                    return true;

            }
        }

        return false;

    }

    //------------ C U S T O M  A L E R T  D I A L O G B O X S ------------
    //Custom alert dialog box for Update.
    private void customAlertDialogBoxCreationForUpdation(View view) {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePage.this);

        // Set the message show for the Alert time
        builder.setMessage(R.string.are_you_sure_you_want_to_update_the_values);

        // Set Alert Title
        builder.setTitle(getString(R.string.dialog_box_title));

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.yes, (DialogInterface.OnClickListener) (dialog, which) -> {
            // saving the data.
            if(validDataInputs(view)){
                saveData(view);
            }
        });

        builder.setNegativeButton(R.string.no, (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();

    }

    //Custom alert dialog box for Logout.
    private void customAlertDialogBoxCreationForLogout() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePage.this);

        // Set the message show for the Alert time
        builder.setMessage(getString(R.string.dialog_box_message));

        // Set Alert Title
        builder.setTitle(getString(R.string.dialog_box_title));

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton(R.string.yes, (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button the activity will be finished and the the values of the shared preference will be cleared.
            editor.clear();
            editor.apply();
            // TO finish the activity
            Intent i = new Intent(ProfilePage.this, LoginPage.class);
            startActivity(i);
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton(R.string.no, (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();

    }

}