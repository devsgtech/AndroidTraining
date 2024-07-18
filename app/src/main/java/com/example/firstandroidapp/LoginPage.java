package com.example.firstandroidapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.security.NoSuchAlgorithmException;

public class LoginPage extends AppCompatActivity {

    private TextInputEditText email, password;
    private TextInputLayout emailLayout, passwordLayout;
    private TextView goToRegister;
    private Button btnLogin;
    private String hashedPassword;
    private boolean isValid,proceedTheValidations = true;
    private DataBaseHandler dataBaseHandler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String TAG = "LoginPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        init();
        textWatcher();
        buttonClicked();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBaseHandler.getAllUsers();
    }

    private void init(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        btnLogin = findViewById(R.id.btnLogin);
        goToRegister = findViewById(R.id.goToRegister);
        dataBaseHandler = new DataBaseHandler(LoginPage.this);


    }

    private void buttonClicked(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedTheValidations = false;
                if (validInput()){
                    try {
                        hashedPassword = Utility.passwordHashing(password.getText().toString().trim());
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    if(dataBaseHandler.isLoginDetailValid(email.getText().toString().trim().toLowerCase(),hashedPassword)){
                        clearValues();
                        Intent i = new Intent(LoginPage.this, ShowDetails.class);
                        startActivity(i);
                    }else{
                        Utility.displayErrorSnackbar(v, "Entered email or password is wrong",LoginPage.this);
                    }
                }
                proceedTheValidations = true;
            }
        });
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedTheValidations = false;
                clearValues();
                proceedTheValidations = true;
                Intent i = new Intent(LoginPage.this,RegisterPage.class);
                startActivity(i);

            }
        });
    }

    private boolean validInput(){

        // Check for email address.
        if (email.getText().toString().isEmpty()) {
            emailLayout.setError(getString(R.string.email_required));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailLayout.setError(getString(R.string.email_valid_required));
            isValid = false;
        } else {
            emailLayout.setError(null);
            isValid = true;
        }

        // Check for the password
        if (password.getText().toString().trim().isEmpty()) {
            passwordLayout.setError(getString(R.string.password_required));
            isValid = false;
        }

        return isValid;
    }

    private void textWatcher(){
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    emailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (proceedTheValidations) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        emailLayout.setError(getString(R.string.email_valid_required));
                    }
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setError("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (proceedTheValidations) {
                    if (password.getText().toString().trim().isEmpty()) {
                        passwordLayout.setError(getString(R.string.password_valid_required));
                    }
                }
            }
        });
    }

    private void clearValues(){
        email.setText("");

        password.setText("");
    }

    private void saveDataToSharedPreference(){


        // Save first name, last name, and email to shared preferences


        // Log the saved data for debugging purposes
        Log.d(TAG, "savedData " + sharedPreferences.getString(Utility.firstNameKey, "") + " " + sharedPreferences.getString(Utility.lastNameKey, "") + " " + sharedPreferences.getString(Utility.emailAddressKey, "") + " " + sharedPreferences.getString(Utility.countryKey, "") + " " + sharedPreferences.getString(Utility.genderKey, ""));


    }

}