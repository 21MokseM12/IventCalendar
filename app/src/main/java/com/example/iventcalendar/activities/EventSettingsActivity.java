package com.example.iventcalendar.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.settings_tabs.TabFirstPhotos;
import com.example.iventcalendar.activities.tabs.settings_tabs.TabSecondLocation;
import com.example.iventcalendar.activities.tabs.settings_tabs.TabThirdPeople;
import com.example.iventcalendar.entities.Event;
import com.example.iventcalendar.service.database.EventDAO;
import com.example.iventcalendar.service.database.EventDataBase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.ui.settings.SectionsPagerAdapter;
import com.example.iventcalendar.databinding.ActivityEventSettingsBinding;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class EventSettingsActivity extends AppCompatActivity {

    private ActivityEventSettingsBinding binding;
    private EventDataBase dataBase;
    private String date;
    private Dialog exitDialog;
    private Dialog waitDialog;
    private String photoURI;
    private String locations;
    private String people;
    private int crazyCount = 0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        date = getIntent().getStringExtra("date");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        TextView title = binding.title;
        title.setText(new String(Character.toChars(0x1f9d0)) + " Структура мероприятия " + new String(Character.toChars(0x1f9d0)));
        exitDialog = new Dialog(EventSettingsActivity.this);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });
    }
    private void showExitDialog() {
        exitDialog.setContentView(R.layout.custom_exit_dialog);
        Objects.requireNonNull(exitDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        exitDialog.setCancelable(true);

        TextView title = exitDialog.findViewById(R.id.exitTitle);
        title.setText("Укажи уровень безумия!");
        ProgressBar crazyBar = exitDialog.findViewById(R.id.crazyProgressBar);
        FloatingActionButton minusCrazyButton = exitDialog.findViewById(R.id.minusOfCrazyButton);
        FloatingActionButton plusCrazyButton = exitDialog.findViewById(R.id.plusOfCrazyButton);
        minusCrazyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (crazyCount > 0) {
                    crazyCount--;
                    crazyBar.setProgress(crazyCount);
                }
            }
        });
        plusCrazyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (crazyCount < 10) {
                    crazyCount++;
                    crazyBar.setProgress(crazyCount);
                }
            }
        });
        MaterialButton saveButton = exitDialog.findViewById(R.id.saveAndExitButton);
        MaterialButton exitButton = exitDialog.findViewById(R.id.exitButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for (Fragment fragment: fragments) {
                    if (fragment instanceof TabSecondLocation) locations = ((TabSecondLocation) fragment).getFragmentData();
                    else if (fragment instanceof TabThirdPeople) people = ((TabThirdPeople) fragment).getFragmentData();
                    else if (fragment instanceof TabFirstPhotos) photoURI = ((TabFirstPhotos) fragment).getFragmentData();
                }
                exitDialog.dismiss();

                try {
                    dataBase = Room.databaseBuilder(getApplicationContext(), EventDataBase.class, "app-database").build();
                    EventDAO eventDAO = dataBase.eventDAO();
                    if (photoURI == null) photoURI = "";
                    if (locations == null) locations = "";
                    if (people == null) people = "";
                    if (!locations.isEmpty() || !people.isEmpty() || !photoURI.isEmpty()) {
                        Event event = new Event(date, photoURI, locations, people, crazyCount);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            eventDAO.upsertEvent(event);
                        });
                        MainActivity.saveEventDayFlag(date);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                finish();
            }
        });
        exitDialog.show();
    }
}