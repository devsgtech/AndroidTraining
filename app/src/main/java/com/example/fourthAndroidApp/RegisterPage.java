package com.example.fourthAndroidApp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.firstandroidapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterPage extends AppCompatActivity {

    // Tag for logging purposes
    private final String TAG = "REGISTER_PAGE";
    // Flag to track input validation status
    private boolean isValid;
    // Views and Widgets
    private TextInputLayout fnameLayout, emailLayout, lnameLayout;
    private TextInputEditText fnameEditText, lnameEditText, emailEditText;
    private Button btnRegister;
    private Spinner countryDropdownSpinner;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton, othersRadioButton;
    private CheckBox termsCheckbox;
    private TextView radioButtonErrorTextView;
    private ArrayAdapter<String> adapter;

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
        lnameLayout = findViewById(R.id.lnameLayout);
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

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        countryDropdownSpinner.setAdapter(adapter);
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
            isValid = false;
        }

        // Validate email address
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email address");
            Utility.displayErrorSnackbar(v, "Invalid Email Address", RegisterPage.this);
            isValid = false;
        } else {
            emailLayout.setError(null);
            isValid = true;
        }

        // Validate terms and conditions checkbox
        if (!termsCheckbox.isChecked()) {
            isValid = false;
            // Delay showing the snackbar to avoid overlap with other error messages
            v.postDelayed(() -> Utility.displayErrorSnackbar(v, "Please agree to terms and condition", RegisterPage.this), 500);
        }

        return isValid;
    }

    // Method to set up text change listeners for input fields
    private void textWatchListener() {

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

        // Add "android.nonFinalResIds=false" in gradle.properties to avoid NonConstantResourceId warning
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case (R.id.maleRadioButton):
                        radioButtonErrorTextView.setText("");
                        break;
                    case (R.id.femaleRadioButton):
                        radioButtonErrorTextView.setText("");
                        break;
                    case (R.id.othersRadioButton):
                        radioButtonErrorTextView.setText("");
                        break;
                    default:
                        radioButtonErrorTextView.setText("");
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
                    // If inputs are valid, show success message and clear fields
                    Utility.displaySuccessSnackbar(v, "Registered Successfully", RegisterPage.this);
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

}
