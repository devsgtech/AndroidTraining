package com.example.fourthAndroidApp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHandler extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "user.db";
    static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_GENDER = "gender";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //SQL Query for creating the databse in SqlLite.
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FIRST_NAME + " TEXT," + COLUMN_LAST_NAME + " TEXT," + COLUMN_EMAIL + " TEXT," + COLUMN_COUNTRY + " TEXT," + COLUMN_GENDER + " TEXT" + ")";

        //Command to create the table.
        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        onCreate(db);

    }
}
