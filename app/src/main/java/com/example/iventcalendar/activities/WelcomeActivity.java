package com.example.iventcalendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
        text.setText("Олег");

        image.setImageResource(R.drawable.oleg_image);

//        Handler handler = new Handler();
//
//        handler.postDelayed(() -> {
//            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }, 1500);

        int delay = 1000;
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Intent in = new Intent().setClass(WelcomeActivity.this,
                        MainActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();

            }
        };
        timer.schedule(task, delay);

    }
}
