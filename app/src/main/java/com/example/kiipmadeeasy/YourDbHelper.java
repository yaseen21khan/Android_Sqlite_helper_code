package com.example.kiipmadeeasy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class YourDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Level2_vocabulary_database.db";
    private static final int DATABASE_VERSION = 1;

    // Define the table and column names
    public static final String TABLE_NAME = "YourTable";
    public static final String COLUMN_LEVEL = "Level";
    public static final String COLUMN_CHAPTER = "Chapter1";
    public static final String COLUMN_KOREAN = "Korean";
    public static final String COLUMN_ENGLISH = "English";

    // Create the table query
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_LEVEL + " TEXT, " +
                    COLUMN_CHAPTER + " TEXT, " +
                    COLUMN_KOREAN + " TEXT, " +
                    COLUMN_ENGLISH + " TEXT)";

    private Context context;

    public YourDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        // Copy the database from assets to internal storage
        copyDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Log creation of the table
        Log.d("YourDbHelper", "onCreate: Creating table " + TABLE_NAME);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades if needed
    }

    private void copyDatabase() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Log the exception
                Log.e("YourDbHelper", "copyDatabase: Error copying database", e);
            }
        }
    }

    public Cursor getFilteredData(String selectedLevel, String selectedChapter) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_KOREAN,
                COLUMN_ENGLISH
        };

        String selection = COLUMN_LEVEL + "=? AND " + COLUMN_CHAPTER + "=?";
        String[] selectionArgs = {selectedLevel, selectedChapter};

        return db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
}
