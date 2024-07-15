package com.example.firstandroidapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ShowDetails extends AppCompatActivity {
    private TextView nameView, emailView, genderView, countryView;
    private Button logoutButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        init();
        setLocalStorageValues();
        setLogoutButton();

    }

    private void init() {
        nameView = findViewById(R.id.nameView);
        emailView = findViewById(R.id.emailView);
        genderView = findViewById(R.id.genderView);
        countryView = findViewById(R.id.countryView);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void setLocalStorageValues() {
        sharedPreferences = getSharedPreferences("login_details", MODE_PRIVATE);
        nameView.setText(new StringBuilder().append("Hi, ").append(sharedPreferences.getString(Utility.firstNameKey, "")).append(" ").append(sharedPreferences.getString(Utility.lastNameKey, "")).append(" ").append("!").toString());
        emailView.setText(sharedPreferences.getString(Utility.emailAddressKey, ""));
        genderView.setText(sharedPreferences.getString(Utility.genderKey, ""));
        countryView.setText(sharedPreferences.getString(Utility.countryKey, ""));
    }

    private void setLogoutButton(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(ShowDetails.this, "Log out Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}