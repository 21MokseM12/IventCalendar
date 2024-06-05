package com.example.iventcalendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
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
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//        );

        super.onCreate(saveInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_welcome);

        TextView text = findViewById(R.id.oleg);
        ImageView image = findViewById(R.id.olegImage);
        text.setText("Олег");

        image.setImageResource(R.drawable.oleg_image);

//        Window window = getWindow();
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//        );
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        int delay = 1000;
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

//                Intent in = new Intent().setClass(WelcomeActivity.this,
//                        MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent in = new Intent(WelcomeActivity.this, MainActivity.class);
                finish();
                startActivity(in);

            }
        };
        timer.schedule(task, delay);

    }
}
