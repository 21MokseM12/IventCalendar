package com.example.iventcalendar.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.iventcalendar.R;
import com.example.iventcalendar.service.EventDecorator;
import com.example.iventcalendar.service.Translator;
import com.google.android.material.button.MaterialButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.time.Month;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences eventFlags;
    private SharedPreferences isFirstLaunch;
    private Dialog firstDialog;
    private MaterialCalendarView calendar;
    private TextView textDateView;
    private String key;
    private EventDecorator decorator;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        eventFlags = getSharedPreferences("Existing_Events", MODE_PRIVATE);
        isFirstLaunch = getSharedPreferences("First_Launch_App", MODE_PRIVATE);
//        SharedPreferences.Editor editor1 = isFirstLaunch.edit();
//        editor1.clear();
//        editor1.apply();
        if (!isFirstLaunch.contains("true")) {
            firstDialog = new Dialog(MainActivity.this);
            showFirstDialog();
            SharedPreferences.Editor editor = isFirstLaunch.edit();
            editor.putBoolean("true", true);
            editor.apply();
        }
//        System.out.println(isFirstLaunch.getAll());

        calendar = findViewById(R.id.calendarView);
        TextView titleApp = findViewById(R.id.titleOfApp);
        textDateView = findViewById(R.id.date);

        calendar.setShowOtherDates(MaterialCalendarView.SHOW_NONE);

        titleApp.setText(new String(Character.toChars(0x0001F609)) + " Календарь ивентов " + new String(Character.toChars(0x0001F609)));
        String[] currentDate = new Date().toString().split(" ");
        textDateView.setText(currentDate[2] + " " + Translator.monthTranslate(currentDate[1].toUpperCase()) + " " + currentDate[5]);

        View.OnClickListener toSettingsActivityListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventSettingsActivity.class);
                intent.putExtra("date", key);
                startActivity(intent);
            }
        };
        View.OnClickListener toEventInfoActivityListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventInfoActivity.class);
                intent.putExtra("date", key);
                startActivity(intent);
            }
        };
//        this.clearEventDaysFlags();
//        System.out.println(eventFlags.getAll());

        decorator = new EventDecorator(this, eventFlags, R.drawable.skate_circle);
        calendar.addDecorator(decorator);
        calendar.setSelectionColor(Color.TRANSPARENT);
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                textDateView.setText(date.getDay() + " " + Translator.monthTranslate(Month.of(date.getMonth() + 1).toString()) + " " + date.getYear());
                key = String.valueOf(date.getDay()) + (date.getMonth()+1) + date.getYear();
                if (eventFlags.contains(key)) toEventInfoActivityListener.onClick(widget);
                else toSettingsActivityListener.onClick(widget);
                calendar.invalidateDecorators();
            }
        });
    }
    private void showFirstDialog() {
        firstDialog.setContentView(R.layout.custom_first_launch_dialog);
        Objects.requireNonNull(firstDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        firstDialog.setCancelable(false);
        MaterialButton button = firstDialog.findViewById(R.id.firstLaunchButton);
        button.setText("ОЙОЙОЙ");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstDialog.dismiss();
            }
        });
        firstDialog.show();
    }

    protected static void saveEventDayFlag(String date) {
        SharedPreferences.Editor editor = eventFlags.edit();
        editor.putBoolean(date, true);
        editor.apply();
    }
    protected static void deleteEventDayFlag(String key) {
        SharedPreferences.Editor editor = eventFlags.edit();
        editor.remove(key);
        editor.apply();
    }
    protected static void clearAllEventDaysFlags() {
        SharedPreferences.Editor editor = eventFlags.edit();
        editor.clear();
        editor.apply();
    }
    protected static boolean isEventFlagExist(String date) {
        return eventFlags.contains(date);
    }
}
