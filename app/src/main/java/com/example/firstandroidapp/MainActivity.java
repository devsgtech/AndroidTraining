// Package declaration, defining the package name for this class
package com.example.firstandroidapp;

// Import statements to include necessary Android and Java classes
import android.os.Bundle; // Importing the Bundle class used to pass data between activities

import androidx.appcompat.widget.Toolbar; // Importing the Toolbar widget for app bar
import androidx.activity.EdgeToEdge; // Importing the EdgeToEdge class for enabling edge-to-edge display
import androidx.appcompat.app.AppCompatActivity; // Importing AppCompatActivity class for compatibility support
import androidx.core.graphics.Insets; // Importing the Insets class for handling window insets
import androidx.core.view.ViewCompat; // Importing the ViewCompat class for view compatibility
import androidx.core.view.WindowInsetsCompat; // Importing the WindowInsetsCompat class for window insets compatibility

// Main activity class declaration, extending AppCompatActivity to use support library features
public class MainActivity extends AppCompatActivity {

    // Overriding the onCreate method which is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Calling the superclass's onCreate method to perform its default behavior
        // Setting the content view to the layout defined in activity_main.xml
        setContentView(R.layout.activity_main);
        // Finding the Toolbar view by its ID and assigning it to the myToolbar variable
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        // Setting the Toolbar as the app bar for the activity
        setSupportActionBar(myToolbar);
        // Setting the title for the app bar
        getSupportActionBar().setTitle(R.string.toolbar_heading);

    }
}
