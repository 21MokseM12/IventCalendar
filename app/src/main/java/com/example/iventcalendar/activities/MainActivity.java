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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.iventcalendar.R;
import com.example.iventcalendar.service.decorators.ActiveEventDecorator;
import com.example.iventcalendar.service.decorators.CurrentDateDecorator;
import com.google.android.material.button.MaterialButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static SharedPreferences eventFlags;
    private static SharedPreferences totalEventDays;
    private SharedPreferences isFirstLaunch;
    private Dialog firstDialog;
    private MaterialCalendarView calendar;
    private TextView totalEventDaysView;
    private String key;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        isFirstLaunch = getSharedPreferences("First_Launch_App", MODE_PRIVATE);
        eventFlags = getSharedPreferences("Existing_Events", MODE_PRIVATE);
        totalEventDays = getSharedPreferences("Total_Event_Days", MODE_PRIVATE);
        totalEventDays.registerOnSharedPreferenceChangeListener(this);

        if (!isFirstLaunch.contains("true")) {
            firstDialog = new Dialog(MainActivity.this);
            showFirstDialog();
        }


        TextView titleApp = findViewById(R.id.titleOfApp);
        titleApp.setText(new String(Character.toChars(0x0001F609)) + " Календарь ивентов " + new String(Character.toChars(0x0001F609)));


        totalEventDaysView = findViewById(R.id.totalEventDays);
        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        totalEventDaysView.setText("Мероприятий за год: " +
                totalEventDays.getInt(currentYear, 0));


        calendar = findViewById(R.id.calendarView);
        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                totalEventDaysView.setText("Мероприятий за год: " +
                        totalEventDays.getInt(String.valueOf(date.getYear()), 0));
            }
        });
        calendar.setShowOtherDates(MaterialCalendarView.SHOW_NONE);
        ActiveEventDecorator eventDecorator = new ActiveEventDecorator(this, eventFlags, R.drawable.skate_circle);
        CurrentDateDecorator currentDateDecorator = new CurrentDateDecorator(this, R.drawable.oleg_image);
        calendar.addDecorator(eventDecorator);
        calendar.addDecorator(currentDateDecorator);
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

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                currentDateDecorator.setDate(date);
                calendar.invalidateDecorators();
                key = String.valueOf(date.getDay()) + (date.getMonth()+1) + date.getYear();
                if (eventFlags.contains(key)) toEventInfoActivityListener.onClick(widget);
                else toSettingsActivityListener.onClick(widget);
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
                SharedPreferences.Editor editor = isFirstLaunch.edit();
                editor.putBoolean("true", true);
                editor.apply();
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
    protected static void addTotalEventDaysCount(String year) {
        totalEventDays.edit().putInt(year, totalEventDays.getInt(year, 0)+1).apply();
    }
    protected static void subtractTotalEventDaysCount(String year) {
        totalEventDays.edit().putInt(year, totalEventDays.getInt(year, 0)-1).apply();
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        TextView total = findViewById(R.id.totalEventDays);
        total.setText("Мероприятий за год: " + sharedPreferences.getInt(key, 0));
    }
}
