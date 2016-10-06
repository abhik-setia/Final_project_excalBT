package com.example.vinay.bluesample;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MediaActivity extends AppCompatActivity {
MediaPlayer media;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        media= MediaPlayer.create(MediaActivity.this, R.raw.alert );
        media.start();

    }
}
