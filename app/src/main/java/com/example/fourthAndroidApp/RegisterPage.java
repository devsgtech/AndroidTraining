package com.example.fourthAndroidApp;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstandroidapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterPage extends AppCompatActivity {

    // Tag for logging purposes
    private final String TAG = "REGISTER_PAGE";
    // Flag to track input validation status
    private boolean isValid;
    // Views and Widgets
    private TextInputLayout fnameLayout, emailLayout;
    private TextInputEditText fnameEditText, lnameEditText, emailEditText;
    private Button btnRegister;
    private DataBaseHandler dataBaseHandler;

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
        dataBaseHandler = new DataBaseHandler(RegisterPage.this);
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

    }

    // Method to set up click listener for the registration button
    private void onClickRegisterButton() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate inputs when register button is clicked
                if (validateInputs(v)) {
                    // Save data to local database.
                    saveData(v);
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
        // Hide the keyboard on the click of the button
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnRegister.getWindowToken(), 0);
    }

    // Method to save data to SharedPreferences
    private void saveData(View view) {
        //Getting the values.
        String firstName = Objects.requireNonNull(fnameEditText.getText()).toString();
        String lastName = Objects.requireNonNull(lnameEditText.getText()).toString();
        String email = Objects.requireNonNull(emailEditText.getText()).toString();
        //Passing the values in the addUser function.
        dataBaseHandler.addUser(view, getApplicationContext() , firstName, lastName, email);
        dataBaseHandler.getAllUsers();

    }

}
