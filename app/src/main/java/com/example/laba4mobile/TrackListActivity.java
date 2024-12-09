package com.example.laba4mobile;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TrackListActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

        db = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.table_layout);

        loadSongs();
    }

    private void loadSongs() {
        ArrayList<String[]> tracks = db.getAllTracks();
        for (String[] track : tracks) {
            TableRow tableRow = new TableRow(this);

            TextView textId = new TextView(this);
            textId.setText(track[0]);
            textId.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

            TextView textArtist = new TextView(this);
            textArtist.setText(track[1]);
            textArtist.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2));

            TextView textTitle = new TextView(this);
            textTitle.setText(track[2]);
            textTitle.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2));

            TextView textTimestamp = new TextView(this);
            textTimestamp.setText(track[3]);
            textTimestamp.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2));

            tableRow.addView(textId);
            tableRow.addView(textArtist);
            tableRow.addView(textTitle);
            tableRow.addView(textTimestamp);

            tableLayout.addView(tableRow);
        }
    }
}