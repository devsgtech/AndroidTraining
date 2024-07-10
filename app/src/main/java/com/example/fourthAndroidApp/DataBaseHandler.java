package com.example.fourthAndroidApp;

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

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Default function required to be made.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL Query for creating the databse in SqlLite.
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FIRST_NAME + " TEXT NOT NULL," + COLUMN_LAST_NAME + " TEXT NOT NULL," + COLUMN_EMAIL + " TEXT NOT NULL " + ")";
        //Command to create the table.
        db.execSQL(CREATE_USER_TABLE);
    }

    // Default function required to be made.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // Function to insert the values.
    public void addUser(View v, Context context, String firstName, String lastName, String email) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRST_NAME, firstName);
            values.put(COLUMN_LAST_NAME, lastName);
            values.put(COLUMN_EMAIL, email);

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

    // To get the users in db.
    public void getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

        if (cursor.moveToFirst()) {
            do {
                Log.d(TAG, "ID: " + cursor.getInt(0));
                Log.d(TAG, "First Name: " + cursor.getString(1));
                Log.d(TAG, "Last Name: " + cursor.getString(2));
                Log.d(TAG, "Email: " + cursor.getString(3));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "No data found");
        }

        cursor.close();
        db.close();
    }

    // To find the data in the device.
    // /data/data/com.example.firstandroidapp/databases/user.db
}
