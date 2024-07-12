package com.example.fourthAndroidApp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firstandroidapp.R;

public class ShowDetails extends AppCompatActivity {
    private TextView nameView, emailView, genderView, countryView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        init();
        setLocalStorageValues();
    }

    // Initialising the variables
    private void init() {
        nameView = findViewById(R.id.nameView);
        emailView = findViewById(R.id.emailView);
    }

    //For setting the Local Storage Values
    private void setLocalStorageValues() {
        sharedPreferences = getSharedPreferences("login_details", MODE_PRIVATE);
        nameView.setText(new StringBuilder().append("Hi, ").append(sharedPreferences.getString(Utility.firstNameKey, "")).append(" ").append(sharedPreferences.getString(Utility.lastNameKey, "")).append(" ").append("!").toString());
        emailView.setText(sharedPreferences.getString(Utility.emailAddressKey, ""));
    }

}