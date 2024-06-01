package com.example.kevinsight.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Users.db";
    private static final Integer DATABASE_VERSION = 1;
    private static final String TABLE_USER = "user";
    private static final String COL_ID = "id";
    private static final String COL_FULLNAME = "fullname";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    public static final String PREF_NAME = "user_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_LOGGED_IN_USERNAME = "logged_in_username";
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_FULLNAME + " TEXT," +
                COL_USERNAME + " TEXT," +
                COL_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public boolean insertData(String fullname, String username, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULLNAME, fullname);
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_PASSWORD, password);
        long result = myDB.insert(TABLE_USER, null, contentValues);
        return result != -1;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = null;
        boolean result = false;
        try {
            cursor = myDB.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});
            if (cursor != null && cursor.getCount() > 0) {
                result = true;
                setUserLoggedIn(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public boolean isUserLoggedIn() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUserLoggedIn(String username) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_LOGGED_IN_USERNAME, username);
        editor.apply();
    }

    public void logoutUser() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.remove(KEY_LOGGED_IN_USERNAME);
        editor.apply();
    }

    public Cursor getUserData(String username) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        return myDB.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
    }

    public boolean updateUserData(String username, String fullname, String newUsername, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FULLNAME, fullname);
        values.put(COL_USERNAME, newUsername);
        values.put(COL_PASSWORD, newPassword);
        int rowsAffected = db.update(TABLE_USER, values, COL_USERNAME + " = ?", new String[]{username});
        return rowsAffected > 0;
    }

    public boolean deleteUserData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USER, COL_USERNAME + " = ?", new String[]{username});
        return rowsAffected > 0;
    }
}
