package com.example.iventcalendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.service.Translator;

import java.time.Month;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendar = findViewById(R.id.calendarView);
        final TextView titleApp = findViewById(R.id.titleOfApp);
        titleApp.setText(new String(Character.toChars(0x0001F609)) + " Календарь ивентов " + new String(Character.toChars(0x0001F609)));
        final TextView textDateView = findViewById(R.id.date);
        String[] currentDate = new Date().toString().split(" ");
        textDateView.setText(currentDate[2] + " " + Translator.monthTranslate(currentDate[1].toUpperCase()) + " " + currentDate[5]);

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) ->
                textDateView.setText(dayOfMonth + " " + Translator.monthTranslate(Month.of(month+1).toString()) + " " + year));
    }
}
