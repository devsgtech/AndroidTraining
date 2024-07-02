package com.example.thirdAndroidApp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstandroidapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterPage extends AppCompatActivity {

    // Tag for logging purposes
    private final String TAG = "REGISTER_PAGE";

    // Views and Widgets
    private TextInputLayout fnameLayout, emailLayout, lnameLayout;
    private TextInputEditText fnameEditText, lnameEditText, emailEditText;
    private Button btnRegister;

    // Flag to track input validation status
    private boolean isValid;

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
        fnameLayout = findViewById(R.id.fnameLayout);
        lnameLayout = findViewById(R.id.lnameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        fnameEditText = findViewById(R.id.fname);
        lnameEditText = findViewById(R.id.lname);
        emailEditText = findViewById(R.id.email);
        btnRegister = findViewById(R.id.btnRegister);
    }

    // Method to clear input fields and remove the focus from them
    private void clearFields() {
        fnameEditText.setText("");
        fnameEditText.setFocusable(false);
        lnameEditText.setText("");
        lnameEditText.setFocusable(false);
        emailEditText.setText("");
        emailEditText.setFocusable(false);
    }

    // Method to validate user inputs
    private boolean validateInputs(View v) {
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
            Utility.displayErrorSnackbar(v, "Invalid Email Address.", RegisterPage.this);
            isValid = false;
        } else {
            emailLayout.setError(null);
            isValid = true;
        }

        return isValid;
    }

    // Method to set up text change listeners for input fields
    private void textWatchListener() {

        fnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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

        emailEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
                    // If inputs are valid, show success message and clear fields
                    Utility.displaySuccessSnackbar(v, "Registered Successfully", RegisterPage.this);
                    clearFields();
                }
            }
        });
    }

}
