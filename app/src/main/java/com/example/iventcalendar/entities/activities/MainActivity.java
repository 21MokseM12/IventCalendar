package com.example.iventcalendar.entities.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.iventcalendar.R;
import com.example.iventcalendar.services.implementations.decorators.ActiveEventDecorator;
import com.example.iventcalendar.services.implementations.decorators.ChosenDateDecorator;
import com.example.iventcalendar.managers.SharedPreferencesManager;
import com.example.iventcalendar.services.implementations.decorators.CurrentDateDecorator;
import com.google.android.material.button.MaterialButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static SharedPreferences eventFlags;

    private SharedPreferences descriptions;

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
        descriptions = getSharedPreferences("Descriptions", MODE_PRIVATE);
        eventFlags = getSharedPreferences("Existing_Events", MODE_PRIVATE);
        totalEventDays = getSharedPreferences("Total_Event_Days", MODE_PRIVATE);
        totalEventDays.registerOnSharedPreferenceChangeListener(this);

        if (!SharedPreferencesManager.containsKey(isFirstLaunch, "true")) showFirstDialog();

        TextView titleApp = findViewById(R.id.titleOfApp);
        titleApp.setText(new String(Character.toChars(0x0001F609)) +
                getString(R.string.title_text_main_activity) +
                new String(Character.toChars(0x0001F609)));

        totalEventDaysView = findViewById(R.id.totalEventDays);
        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        totalEventDaysView.setText(getString(R.string.total_count_events_text) +
                totalEventDays.getInt(currentYear, 0));

        calendar = findViewById(R.id.calendarView);
        calendar.setOnMonthChangedListener((widget, date) -> totalEventDaysView.setText(getString(R.string.total_count_events_text) +
                totalEventDays.getInt(String.valueOf(date.getYear()), 0)));

        calendar.setShowOtherDates(MaterialCalendarView.SHOW_NONE);

        ActiveEventDecorator eventDecorator = new ActiveEventDecorator(this, eventFlags, R.drawable.event_day_date);
        ChosenDateDecorator chosenDateDecorator = new ChosenDateDecorator(this, R.drawable.chosen_date);
        CurrentDateDecorator currentDateDecorator = new CurrentDateDecorator(this, CalendarDay.today(), R.drawable.circle_current_date);

        calendar.addDecorator(eventDecorator);
        calendar.addDecorator(chosenDateDecorator);
        calendar.addDecorator(currentDateDecorator);

        calendar.setOnDateChangedListener((widget, date, selected) -> {
            chosenDateDecorator.setDate(date);
            calendar.invalidateDecorators();

            key = String.valueOf(date.getDay()) + (date.getMonth()+1) + date.getYear();

            EditText description = findViewById(R.id.description);
            MaterialButton saveDescription = findViewById(R.id.saveDescription);
            saveDescription.setOnClickListener(onClickSaveDescription(description, widget));

            description.setVisibility(View.VISIBLE);
            saveDescription.setVisibility(View.VISIBLE);

            if (eventFlags.contains(key)) {
                saveDescription.setText(R.string.changeEvent);
                description.setText(descriptions.getString(key, ""));
            } else {
                saveDescription.setText(R.string.saveEvent);
                description.setText("");
            }
        });
    }

    private View.OnClickListener onClickSaveDescription(EditText description, MaterialCalendarView widget) {
        return v -> {
            String descriptionText = description.getText().toString().trim();
            SharedPreferencesManager.saveString(descriptions, key, descriptionText);
            if (eventFlags.contains(key)) onClickToEventInfoActivity().onClick(widget);
            else onClickToEventSettingsActivity().onClick(widget);
        };
    }

    private View.OnClickListener onClickToEventSettingsActivity() {
        return v -> {
            Intent intent = new Intent(MainActivity.this, EventSettingsActivity.class);
            intent.putExtra("date", key);
            startActivity(intent);
        };
    }

    private View.OnClickListener onClickToEventInfoActivity() {
        return v -> {
            Intent intent = new Intent(MainActivity.this, EventInfoActivity.class);
            intent.putExtra("date", key);
            startActivity(intent);
        };
    }

    private void showFirstDialog() {
        firstDialog = new Dialog(MainActivity.this);
        firstDialog.setContentView(R.layout.custom_first_launch_dialog);
        Objects.requireNonNull(firstDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        firstDialog.setCancelable(false);

        MaterialButton button = firstDialog.findViewById(R.id.firstLaunchButton);
        button.setText(R.string.oyoyoy);
        button.setOnClickListener(onClickFirstDialogButton());

        firstDialog.show();
    }

    private View.OnClickListener onClickFirstDialogButton() {
        return v -> {
            SharedPreferencesManager.saveBoolean(isFirstLaunch, "true", true);
            firstDialog.dismiss();
        };
    }

    public static SharedPreferences getEventFlags() {return eventFlags;}

    public static SharedPreferences getTotalEventDays() {return totalEventDays;}

    @SuppressLint("SetTextI18n")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        TextView total = findViewById(R.id.totalEventDays);
        total.setText("Мероприятий за год: " + sharedPreferences.getInt(key, 0));
    }
}
