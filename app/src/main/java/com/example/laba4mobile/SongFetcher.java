package com.example.laba4mobile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SongFetcher {
    private DatabaseHelper db;
    private Context context;
    private final String url = "https://webradio.io/api/radio/shayzu/current-song"; // URL API

    public SongFetcher(DatabaseHelper db, Context context) {
        this.db = db;
        this.context = context;
    }

    public void fetchCurrentSong() {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("ServerResponse", response.toString());

                        if (response.has("artist") && response.has("title")) {
                            String artist = response.getString("artist");
                            String title = response.getString("title");

                            if (!db.isTrackExists(artist, title)) {
                                db.addTrack(artist, title);

                                Intent intent = new Intent("NEW_SONG_ADDED");
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                                Toast.makeText(context, "Текущая песня: " + title + " - " + artist, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Песня уже играет: " + title + " - " + artist, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Нет доступных треков", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("FetchCurrentSong", "Ошибка обработки данных: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("FetchCurrentSong", "Ошибка загрузки данных: " + error.getMessage());
                }
        );
        queue.add(request);
    }
}
