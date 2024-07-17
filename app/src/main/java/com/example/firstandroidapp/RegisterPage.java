package com.example.firstandroidapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage extends AppCompatActivity {

    // Tag for logging purposes
    private final String TAG = "REGISTER_PAGE";
    // Flag to track input validation status
    private boolean isValid;
    // Views and Widgets
    private TextInputLayout fnameLayout, emailLayout, passwordLayout, confirmPasswordLayout, contactNumberLayout, dateSelectionLayout;
    private TextInputEditText fnameEditText, lnameEditText, emailEditText, contactNumber, password, confirmPassword, dateSelection;
    private Button btnRegister;
    private Spinner countryDropdownSpinner;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton, othersRadioButton;
    private CheckBox termsCheckbox;
    private TextView genderHeading;
    private TextView radioButtonErrorTextView;
    private TextView agreeTermsErrorTextView;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String defaultString = "";
    private Intent intent;
    private View genderRadioGroupBottomView;
    private Boolean proceedTheValidations = true;
    private final String passwordMatcher = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private final String numberMatcher = "^[0-9]{0,10}$";
    private boolean dateIsValid = true;
    private DataBaseHandler dataBaseHandler;
    private String passwordToBeSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the register page layout
        setContentView(R.layout.activity_register_page);
        // Get the root view of the activity's layout
        View rootView = findViewById(android.R.id.content);
        // Initialize views and widgets
        init();
        // Set up text change listeners for input validation
        textWatchListener();
        // Set up click listener for the registration button
        onClickRegisterButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // To get all the user on the start of the app in logcat.
        dataBaseHandler.getAllUsers();
    }

    // Method to initialize views and widgets
    private void init() {

        // Link the variables to the views in the layout
        fnameLayout = findViewById(R.id.fnameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        fnameEditText = findViewById(R.id.fname);
        lnameEditText = findViewById(R.id.lname);
        emailEditText = findViewById(R.id.email);
        btnRegister = findViewById(R.id.btnRegister);
        countryDropdownSpinner = findViewById(R.id.countryDropdownSpinner);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        othersRadioButton = findViewById(R.id.othersRadioButton);
        termsCheckbox = findViewById(R.id.termsCheckbox);
        contactNumberLayout = findViewById(R.id.contactNumberLayout);
        contactNumber = findViewById(R.id.contactNumber);
        radioButtonErrorTextView = findViewById(R.id.radioButtonErrorTextView);
        agreeTermsErrorTextView = findViewById(R.id.agreeTermsErrorTextView);
        genderHeading = findViewById(R.id.genderHeading);
        genderRadioGroupBottomView = findViewById(R.id.genderRadioGroupBottomView);
        contactNumber = findViewById(R.id.contactNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        dateSelection = findViewById(R.id.dateSelection);
        dateSelectionLayout = findViewById(R.id.dateSelectionLayout);
        dataBaseHandler = new DataBaseHandler(RegisterPage.this);

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

        // Initializing the shared preference and editor
        sharedPreferences = getSharedPreferences(Utility.saveDetailsFilename, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Method to validate user inputs
    private boolean validateInputs(View v) {

        // Validate first name.
        if (fnameEditText.getText().toString().isEmpty()) {
            fnameLayout.setError(getString(R.string.first_name_required));
            isValid = false;
        } else {
            fnameLayout.setError(null);
            isValid = true;
        }

        // Validate gender selection.
        if (maleRadioButton.isChecked() || femaleRadioButton.isChecked() || othersRadioButton.isChecked()) {
            isValid = true;
        } else {
            radioButtonErrorTextView.setText(R.string.gender_selection);
            genderHeading.setTextColor(ContextCompat.getColor(this, R.color.faliure));
            genderRadioGroupBottomView.setBackgroundColor(ContextCompat.getColor(this, R.color.faliure));
            isValid = false;
        }

        // Validate email address.
        if (emailEditText.getText().toString().isEmpty()) {
            emailLayout.setError(getString(R.string.email_required));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            emailLayout.setError(getString(R.string.email_valid_required));
            isValid = false;
        } else {
            emailLayout.setError(null);
            isValid = true;
        }

        // Validation for the Password.
        if (password.getText().toString().trim().isEmpty()) {
            passwordLayout.setError(getString(R.string.password_required));
            isValid = false;
        }

        //Validation for the contact number
        if (contactNumber.getText().toString().trim().length() > 10) {
            isValid = false;
        }

        // Validation for the Confirm Password.
        if (confirmPassword.getText().toString().trim().isEmpty()) {
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

        // Validate terms and conditions checkbox.
        if (!termsCheckbox.isChecked()) {
            isValid = false;
            agreeTermsErrorTextView.setText(R.string.agree_terms);
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

        // Listener for email input field
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailEditText.setCursorVisible(true);
                emailLayout.setError(null);
                emailLayout.setFocusable(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear any previous error when user starts typing
                emailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // To check if the value entered is valid.Here the proceedTheValidations check is given so that error is not thrown at the time of the validation check on click of the register button.
                if (proceedTheValidations) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        emailLayout.setError(getString(R.string.email_valid_required));
                    }
                }
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
                        contactNumberLayout.setError("Should contain numbers and utmost 10 digits");
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

        // Listener for gender radio group
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle radio button check changes
                switch (checkedId) {
                    case (R.id.maleRadioButton):
                        radioButtonErrorTextView.setText("");
                        genderHeading.setTextColor(ContextCompat.getColor(RegisterPage.this, R.color.black));
                        genderRadioGroupBottomView.setBackgroundColor(ContextCompat.getColor(RegisterPage.this, R.color.black));
                        break;
                    case (R.id.femaleRadioButton):
                        radioButtonErrorTextView.setText("");
                        genderHeading.setTextColor(ContextCompat.getColor(RegisterPage.this, R.color.black));
                        genderRadioGroupBottomView.setBackgroundColor(ContextCompat.getColor(RegisterPage.this, R.color.black));
                        break;
                    case (R.id.othersRadioButton):
                        radioButtonErrorTextView.setText("");
                        genderHeading.setTextColor(ContextCompat.getColor(RegisterPage.this, R.color.black));
                        genderRadioGroupBottomView.setBackgroundColor(ContextCompat.getColor(RegisterPage.this, R.color.black));
                        break;
                    default:
                        radioButtonErrorTextView.setText("");
                        genderHeading.setTextColor(ContextCompat.getColor(RegisterPage.this, R.color.black));
                        genderRadioGroupBottomView.setBackgroundColor(ContextCompat.getColor(RegisterPage.this, R.color.black));
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

        // Listener for terms and conditions checkbox
        termsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Clear any error when checkbox is checked
                if (buttonView.isChecked()) {
                    agreeTermsErrorTextView.setText("");
                }
            }
        });

    }

    // Method to set up click listener for the registration button
    private void onClickRegisterButton() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for stopping the TextWatcher Validations.
                proceedTheValidations = false;
                // Validate inputs when register button is clicked.
                if (validateInputs(v)) {
                    if (!dataBaseHandler.alreadyExist(emailEditText.getText().toString().trim())) {
                        // Save data to shared preferences.
                        saveData(v);
                        // Show a success message if you want to.
                         Utility.displaySuccessSnackbar(v, getString(R.string.registered_successfully), RegisterPage.this);
                        // Navigate to the ShowDetails activity.
                        intent = new Intent(RegisterPage.this, ShowDetails.class);
                        startActivity(intent);
                        // Clear all input fields.
                        clearFields();
                    }else{
                        // Showing an error message.
                        Utility.displayErrorSnackbar(v, getString(R.string.email_already_exists) ,RegisterPage.this);
                    }
                }
                // Check for resuming the TextWatcher Validations.
                proceedTheValidations = true;
            }
        });
    }

    // Method to clear input fields and remove the cursor blinking from them
    private void clearFields() {

        fnameEditText.setText("");
        fnameEditText.setCursorVisible(false);

        lnameEditText.setText("");
        lnameEditText.setCursorVisible(false);

        emailEditText.setText("");
        emailEditText.setCursorVisible(false);
        emailEditText.setFocusable(false);

        maleRadioButton.setChecked(false);
        femaleRadioButton.setChecked(false);
        othersRadioButton.setChecked(false);

        contactNumber.setText("");
        contactNumber.setCursorVisible(false);

        dateSelection.setText("");

        password.setText("");
        password.setCursorVisible(false);

        confirmPassword.setText("");
        confirmPassword.setCursorVisible(false);

        termsCheckbox.setChecked(false);
        countryDropdownSpinner.setSelection(0);
        // Hide the keyboard on the click of the button
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnRegister.getWindowToken(), 0);
    }

    // Method to save data to SharedPreferences
    private void saveData(View view) {
        // Save first name, last name, and email to shared preferences
        editor.putString(Utility.firstNameKey, fnameEditText.getText().toString());
        editor.putString(Utility.lastNameKey, lnameEditText.getText().toString());
        editor.putString(Utility.emailAddressKey, emailEditText.getText().toString());

        // Save the selected country
        String selectedCountry = countryDropdownSpinner.getSelectedItem().toString() == "Select Country" ? "" : countryDropdownSpinner.getSelectedItem().toString();

        // Save the selected gender
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        String selectedGender = "";
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedGenderId);
            selectedGender = selectedRadioButton.getText().toString();
        }

        // Apply changes to shared preferences
        editor.apply();

        // For storing the Hashed password.
        try {
            passwordToBeSaved = Utility.passwordHashing(password.getText().toString().trim());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Save data to the SQLite Database.
        dataBaseHandler.addUser(view, getApplicationContext(), fnameEditText.getText().toString().trim(), lnameEditText.getText().toString().trim(), emailEditText.getText().toString().trim(), contactNumber.getText().toString().trim(), dateSelection.getText().toString().trim(), selectedCountry.toString().trim(), selectedGender.toString().trim(), passwordToBeSaved, passwordToBeSaved, termsCheckbox.isChecked());
        dataBaseHandler.getAllUsers();

        // Log the saved data for debugging purposes
        Log.d(TAG, "savedData " + sharedPreferences.getString(Utility.firstNameKey, "") + " " + sharedPreferences.getString(Utility.lastNameKey, "") + " " + sharedPreferences.getString(Utility.emailAddressKey, "") + " " + sharedPreferences.getString(Utility.countryKey, "") + " " + sharedPreferences.getString(Utility.genderKey, ""));
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
                RegisterPage.this,
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

}
