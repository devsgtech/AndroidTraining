package com.example.firstandroidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static final String saveDetailsFilename = "login_details";
    public static final String firstNameKey = "first_name_key";
    public static final String lastNameKey = "last_name_key";
    public static final String emailAddressKey = "email_address_key";
    public static final String countryKey = "country_selection_key";
    public static final String genderKey = "gender_selection_key";
    public static final String dateOfBirthKey = "date_of_birth_key";
    public static final String contactNumberKey = "contact_number_key";
    public static final String isUserLoggedInKey = "is_user_logged_in_key";


    // Method to display a Snackbar for success messages
    public static void displaySuccessSnackbar(View view, String message, Context context) {
        // Create a Snackbar with the provided message and indefinite duration
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

        // Customize the text color and background tint of the Snackbar
        snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.success));

        // Get the Snackbar view to modify its layout parameters
        View snackBarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();

        // Set gravity of the Snackbar to be at the top and horizontally centered
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        snackBarView.setLayoutParams(params);

        // Add an action button (close button) to dismiss the Snackbar when clicked
        snackbar.setAction("✕", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        // Set text color of the action button
        snackbar.setActionTextColor(ContextCompat.getColor(context, android.R.color.white));

        // Show the Snackbar
        snackbar.show();
    }

    // Method to display a Snackbar for error messages
    public static void displayErrorSnackbar(View view, String message, Context context) {
        // Create a Snackbar with the provided message and a longer duration
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

        // Customize the text color and background tint of the Snackbar
        snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.faliure));

        // Get the Snackbar view to modify its layout parameters
        View snackBarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();

        // Set gravity of the Snackbar to be at the top and horizontally centered
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        snackBarView.setLayoutParams(params);

        // Add an action button (close button) to dismiss the Snackbar when clicked
        snackbar.setAction("✕", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        // Set text color of the action button
        snackbar.setActionTextColor(ContextCompat.getColor(context, android.R.color.white));

        // Show the Snackbar
        snackbar.show();
    }

    // Validator checker.
    public static boolean validator(final String validInput, final String validPattern) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(validPattern);
        matcher = pattern.matcher(validInput);
        return matcher.matches();
    }

    // Password Hashing.
    public static String passwordHashing(String passwordInput) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(passwordInput.getBytes());
        byte[] mdArray = md.digest();
        StringBuilder sb = new StringBuilder(mdArray.length * 2);
        for(byte b : mdArray) {
            int v = b & 0xff;
            if(v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }





}
