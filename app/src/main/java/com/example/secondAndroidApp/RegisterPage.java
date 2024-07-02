package com.example.secondAndroidApp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firstandroidapp.R;


public class RegisterPage extends AppCompatActivity {

    // Tag for logging purposes
    private final String TAG = "REGISTER_PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enabling edge-to-edge display for a more immersive UI experience
        EdgeToEdge.enable(this);
        // Setting the content view to the register page layout
        setContentView(R.layout.activity_register_page);


        // Logging a message to indicate that the Register page has been created
        Log.d(TAG, "onCreate: Welcome to Register page");
    }
}