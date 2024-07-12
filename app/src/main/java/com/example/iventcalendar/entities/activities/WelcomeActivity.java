package com.example.iventcalendar.entities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.iventcalendar.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_welcome);

        TextView text = findViewById(R.id.oleg);
        ImageView image = findViewById(R.id.olegImage);
        text.setText(R.string.splash_screen_text);

        image.setImageResource(R.drawable.oleg_image);

        int delay = 1000;
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent in = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(in);
                finish();

            }
        };
        timer.schedule(task, delay);

    }
}
