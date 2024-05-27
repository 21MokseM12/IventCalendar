package com.example.iventcalendar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.iventcalendar.R;
import com.example.iventcalendar.service.Translator;

import java.time.Month;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences eventFlags;
    private CalendarView calendar;
    private TextView titleApp;
    private TextView textDateView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setContentView(R.layout.activity_main);
            eventFlags = getSharedPreferences("Existing_Events", MODE_PRIVATE);

            calendar = findViewById(R.id.calendarView);
            titleApp = findViewById(R.id.titleOfApp);
            textDateView = findViewById(R.id.date);

            titleApp.setText(new String(Character.toChars(0x0001F609)) + " Календарь ивентов " + new String(Character.toChars(0x0001F609)));
            String[] currentDate = new Date().toString().split(" ");
            textDateView.setText(currentDate[2] + " " + Translator.monthTranslate(currentDate[1].toUpperCase()) + " " + currentDate[5]);

        View.OnClickListener toSettingsActivityListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventSettingsActivity.class);
                startActivity(intent);
            }
        };
//        View.OnClickListener toCurrentActivityListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CurrentEventActivity.class);
//                startActivity(intent);
//            }
//        };

            calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    textDateView.setText(dayOfMonth + " " + Translator.monthTranslate(Month.of(month + 1).toString()) + " " + year);
                    toSettingsActivityListener.onClick(view);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
