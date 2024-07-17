package com.example.firstandroidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

public class DataBaseHandler extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "user.db";
    static final int DATABASE_VERSION = 1;
    final String TAG = "DataBaseHandler";

    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String CONTACT_NUMBER = "contact_number";
    private static final String DATE_OF_BIRTH = "date_of_birth";
    private static final String COUNTRY = "country";
    private static final String GENDER = "gender";
    private static final String PASSWORD = "password";
    private static final String CONFIRM_PASSWORD = "confirm_password";
    private static final String TERMS_AGREEMENT = "terms_agreement";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT NOT NULL,"
                + COLUMN_LAST_NAME + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + CONTACT_NUMBER + " TEXT NOT NULL,"
                + DATE_OF_BIRTH + " TEXT NOT NULL,"
                + COUNTRY + " TEXT NOT NULL,"
                + GENDER + " TEXT NOT NULL,"
                + PASSWORD + " TEXT NOT NULL,"
                + CONFIRM_PASSWORD + " TEXT NOT NULL,"
                + TERMS_AGREEMENT + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // Method to add the new user.
    public void addUser(View v, Context context, String firstName, String lastName, String email, String contactNumber, String dateOfBirth, String country, String gender, String password, String confirmPassword, boolean termsAgreement) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRST_NAME, firstName);
            values.put(COLUMN_LAST_NAME, lastName);
            values.put(COLUMN_EMAIL, email);
            values.put(CONTACT_NUMBER, contactNumber);
            values.put(DATE_OF_BIRTH, dateOfBirth);
            values.put(COUNTRY, country);
            values.put(GENDER, gender);
            values.put(PASSWORD, password);
            values.put(CONFIRM_PASSWORD, confirmPassword);
            values.put(TERMS_AGREEMENT, termsAgreement ? 1 : 0);

            long result = db.insert(TABLE_USER, null, values);
            if (result == -1) {
                Log.e(TAG, "Failed to insert row");
                Utility.displayErrorSnackbar(v, "Failed to insert Data", context);
            } else {
                Log.d(TAG, "Row inserted successfully with ID: " + result);
                Utility.displaySuccessSnackbar(v, "Data Saved Successfully to Database.", context);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting user", e);
            Utility.displayErrorSnackbar(v, "Error inserting user", context);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Method to get all the users successfully registered.
    public void getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

        if (cursor.moveToFirst()) {
            do {
                Log.d(TAG, "ID: " + cursor.getInt(0));
                Log.d(TAG, "First Name: " + cursor.getString(1));
                Log.d(TAG, "Last Name: " + cursor.getString(2));
                Log.d(TAG, "Email: " + cursor.getString(3));
                Log.d(TAG, "Contact Number: " + cursor.getString(4));
                Log.d(TAG, "Date of Birth: " + cursor.getString(5));
                Log.d(TAG, "Country: " + cursor.getString(6));
                Log.d(TAG, "Gender: " + cursor.getString(7));
                Log.d(TAG, "Password: " + cursor.getString(8));
                Log.d(TAG, "Confirm Password: " + cursor.getString(9));
                Log.d(TAG, "Terms Agreement: " + cursor.getInt(10));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "No data found");
        }

        cursor.close();
        db.close();
    }

    // Method to check the already email existence at the time of Registration.
    public boolean alreadyExist(String enteredEmail){
        // Declared the variables.
        SQLiteDatabase db = null;
        Cursor c = null;
        // Try catch for the smooth functioning.
        try{
            // Initialized the variables.
            db = this.getReadableDatabase();
            // String Query
            String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?";
            // Searching for the entered email.
            c = db.rawQuery(query, new String[]{enteredEmail});
            // Condition for checking the email if email already exist.
            if (c.getCount()>0){
                // Returning true if it exists.
                return true;
            }
        }catch (Exception e){
            // Logging the error.
            Log.d(TAG, "alreadyExist Exception: " + e);
        } finally {
            // Closing the Cursor.
            if(c!=null){
                c.close();
            }
            // Closing the DB.
            if(db!=null && db.isOpen()){
                db.close();
            }
        }
        // Returning false if email doesn't exist.
        return false;
    }



}
