package com.example.laba4mobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private final int POLL_INTERVAL = 20000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        Button viewTracksButton = findViewById(R.id.view_tracks_button);

        viewTracksButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrackListActivity.class);
            startActivity(intent);
        });

        if (!isInternetAvailable(this)) {
            Toast.makeText(this, "Запуск в автономном режиме", Toast.LENGTH_SHORT).show();
        } else {
            startPolling();
        }
    }

    private boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void startPolling() {
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isInternetAvailable(getApplicationContext())) {
                    SongFetcher songFetcher = new SongFetcher(db, getApplicationContext());
                    songFetcher.fetchCurrentSong();
                } else {
                    Toast.makeText(getApplicationContext(), "Запуск в автономном режиме", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(this, POLL_INTERVAL);
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}