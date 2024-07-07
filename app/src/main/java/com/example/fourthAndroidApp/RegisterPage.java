package com.example.fourthAndroidApp;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.firstandroidapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterPage extends AppCompatActivity {

    // Tag for logging purposes
    private final String TAG = "REGISTER_PAGE";
    // Flag to track input validation status
    private boolean isValid;
    // Views and Widgets
    private TextInputLayout fnameLayout, emailLayout;
    private TextInputEditText fnameEditText, lnameEditText, emailEditText;
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
        radioButtonErrorTextView = findViewById(R.id.radioButtonErrorTextView);
        agreeTermsErrorTextView = findViewById(R.id.agreeTermsErrorTextView);
        genderHeading = findViewById(R.id.genderHeading);
        genderRadioGroupBottomView = findViewById(R.id.genderRadioGroupBottomView);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        countryDropdownSpinner.setAdapter(adapter);

        // Initializing the shared preference and editor
        sharedPreferences = getSharedPreferences(Utility.saveDetailsFilename, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Method to validate user inputs
    private boolean validateInputs(View v) {
        // Retrieve text from the input fields
        String fname = fnameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        // Validate first name
        if (fname.isEmpty()) {
            fnameLayout.setError("First name is required");
            isValid = false;
        } else {
            fnameLayout.setError(null);
            isValid = true;
        }

        // Validate gender selection
        if (maleRadioButton.isChecked() || femaleRadioButton.isChecked() || othersRadioButton.isChecked()) {
            isValid = true;
        } else {
            radioButtonErrorTextView.setText(R.string.gender_selection);
            genderHeading.setTextColor(ContextCompat.getColor(this, R.color.faliure));
            genderRadioGroupBottomView.setBackgroundColor(ContextCompat.getColor(this, R.color.faliure));
            isValid = false;
        }

        // Validate email address
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email address");
            isValid = false;
        } else {
            emailLayout.setError(null);
            isValid = true;
        }

        // Validate terms and conditions checkbox
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
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear any previous error when user starts typing
                emailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                // Validate inputs when register button is clicked
                if (validateInputs(v)) {
                    // Save data to shared preferences
                    saveData();
                    // Show a success message
                    Utility.displaySuccessSnackbar(v, "Registered Successfully", RegisterPage.this);
                    // Navigate to the ShowDetails activity
                    intent = new Intent(RegisterPage.this, ShowDetails.class);
                    startActivity(intent);
                    // Clear all input fields
                    clearFields();
                }
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
        maleRadioButton.setChecked(false);
        femaleRadioButton.setChecked(false);
        othersRadioButton.setChecked(false);
        termsCheckbox.setChecked(false);
        countryDropdownSpinner.setSelection(0);
        // Hide the keyboard on the click of the button
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnRegister.getWindowToken(), 0);
    }

    // Method to save data to SharedPreferences
    private void saveData() {
        // Save first name, last name, and email to shared preferences
        editor.putString(Utility.firstNameKey, fnameEditText.getText().toString());
        editor.putString(Utility.lastNameKey, lnameEditText.getText().toString());
        editor.putString(Utility.emailAddressKey, emailEditText.getText().toString());

        // Save the selected country
        String selectedCountry = countryDropdownSpinner.getSelectedItem().toString();
        if (!"Select Country".equals(selectedCountry)) {
            editor.putString(Utility.countryKey, selectedCountry);
        }

        // Save the selected gender
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedGenderId);
            String selectedGender = selectedRadioButton.getText().toString();
            editor.putString(Utility.genderKey, selectedGender);
        }

        // Apply changes to shared preferences
        editor.apply();

        // Log the saved data for debugging purposes
        Log.d(TAG, "savedData " + sharedPreferences.getString(Utility.firstNameKey, "") + " " + sharedPreferences.getString(Utility.lastNameKey, "") + " " + sharedPreferences.getString(Utility.emailAddressKey, "") + " " + sharedPreferences.getString(Utility.countryKey, "") + " " + sharedPreferences.getString(Utility.genderKey, ""));
    }

}
