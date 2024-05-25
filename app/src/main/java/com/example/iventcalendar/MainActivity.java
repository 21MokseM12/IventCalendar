package com.example.iventcalendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.service.Translator;

import java.time.Month;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendar = findViewById(R.id.calendarView);
        final TextView titleApp = findViewById(R.id.titleOfApp);
        titleApp.setText("Ивент? " + new String(Character.toChars(0x0001F609)));
        final TextView textDateView = findViewById(R.id.currentDate);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                textDateView.setText(dayOfMonth + " " + Translator.monthTranslate(Month.of(month+1)) + " " + year);
            }
        });
    }
}
