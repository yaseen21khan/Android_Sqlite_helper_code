package com.example.kiipmadeeasy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private YourDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new YourDbHelper(this);
        // Assume you have Spinner instances for Level and Chapter

        // Fetch data from SQLite based on selected Level and Chapter
        Cursor cursor = dbHelper.getFilteredData("Level2", "Chapter5 ");

        ArrayList<DataItem> dataItems = new ArrayList<>();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String korean = cursor.getString(cursor.getColumnIndex(YourDbHelper.COLUMN_KOREAN));
            @SuppressLint("Range") String english = cursor.getString(cursor.getColumnIndex(YourDbHelper.COLUMN_ENGLISH));

            dataItems.add(new DataItem(korean, english));
        }

        cursor.close();

        // Create an ArrayAdapter and set it to the ListView
        ArrayAdapter<DataItem> adapter = new ArrayAdapter<DataItem>(
                this,
                R.layout.list_item_layout,
                R.id.textViewKorean,
                dataItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textViewKorean = view.findViewById(R.id.textViewKorean);
                TextView textViewEnglish = view.findViewById(R.id.textViewEnglish);

                // Set Korean and English texts separately
                textViewKorean.setText(dataItems.get(position).getKorean());
                textViewEnglish.setText(dataItems.get(position).getEnglish());

                return view;
            }
        };

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    // ... (other methods)

    /**
     * A native method that is implemented by the 'kiipmadeeasy' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // DataItem class to represent each item in the ListView
    // DataItem class to represent each item in the ListView
    private static class DataItem {
        private final String korean;
        private final String english;

        public DataItem(String korean, String english) {
            this.korean = korean;
            this.english = english;
        }

        public String getKorean() {
            return korean;
        }

        public String getEnglish() {
            return english;
        }
/*
        // Override toString to return the desired string representation
        @Override
        public String toString() {
            return "Korean: " + korean + ", English: " + english;
        }
        // Additional method to get a formatted representation for display
        public String getFormattedDisplay() {
            return korean + " - " + english;
        }*/
    }
}
