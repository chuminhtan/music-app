package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.myteam.myapplication.R;
import com.myteam.myapplication.model.Song;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Intent intent = getIntent();
        if (intent.hasExtra("song")) {
            Song song = (Song) intent.getSerializableExtra("song");
            Toast.makeText(this, song.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}