package com.example.firstandroidapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ShowDetails extends AppCompatActivity {
    private TextView nameView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        init();
        setLocalStorageValues();

    }

    private void init() {
        nameView = findViewById(R.id.nameView);
    }

    private void setLocalStorageValues() {
        sharedPreferences = getSharedPreferences("login_details", MODE_PRIVATE);
        nameView.setText(new StringBuilder().append("Hi, ").append(sharedPreferences.getString(Utility.firstNameKey, "")).append(" ").append(sharedPreferences.getString(Utility.lastNameKey, "")).append(" ").append("!").toString());
    }

}