package com.example.iventcalendar.entities.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iventcalendar.R;
import com.example.iventcalendar.entities.tabs.settings_tabs.TabFirstPhotos;
import com.example.iventcalendar.entities.tabs.settings_tabs.TabSecondLocation;
import com.example.iventcalendar.entities.tabs.settings_tabs.TabThirdPeople;
import com.example.iventcalendar.entities.database.Event;
import com.example.iventcalendar.services.implementations.factories.EventDAOFactory;
import com.example.iventcalendar.services.interfaces.database.EventDAO;
import com.example.iventcalendar.managers.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.services.implementations.adapters.settings.SectionsPagerAdapter;
import com.example.iventcalendar.databinding.ActivityEventSettingsBinding;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class EventSettingsActivity extends AppCompatActivity {

    private String date;

    private Dialog exitDialog;

    private String photoURI;

    private String locations;

    private String people;

    private int crazyCount = 0;

    private ProgressBar crazyBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.iventcalendar.databinding.ActivityEventSettingsBinding binding = ActivityEventSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        date = getIntent().getStringExtra("date");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), date);
        ViewPager viewPager = binding.viewPager;
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        TextView title = binding.title;
        title.setText(new String(Character.toChars(0x1f9d0)) +
                getString(R.string.title_text_settings_activity) +
                new String(Character.toChars(0x1f9d0)));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for (Fragment fragment: fragments) {
                    if (fragment instanceof TabSecondLocation) locations = ((TabSecondLocation) fragment).getFragmentData();
                    else if (fragment instanceof TabThirdPeople) people = ((TabThirdPeople) fragment).getFragmentData();
                    else if (fragment instanceof TabFirstPhotos) photoURI = ((TabFirstPhotos) fragment).getFragmentData();
                }
                if (locations.isEmpty() && people.isEmpty() && photoURI.isEmpty()) finish();
                else showExitDialog();
            }
        });
    }

    private void showExitDialog() {
        exitDialog = new Dialog(EventSettingsActivity.this);
        exitDialog.setContentView(R.layout.custom_exit_dialog);
        Objects.requireNonNull(exitDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        exitDialog.setCancelable(true);

        TextView title = exitDialog.findViewById(R.id.exitTitle);
        title.setText(R.string.choose_crazy_level);

        crazyBar = exitDialog.findViewById(R.id.crazyProgressBar);
        FloatingActionButton minusCrazyButton = exitDialog.findViewById(R.id.minusOfCrazyButton);
        FloatingActionButton plusCrazyButton = exitDialog.findViewById(R.id.plusOfCrazyButton);

        minusCrazyButton.setOnClickListener(onClickMinusCrazyBar());
        plusCrazyButton.setOnClickListener(onClickPlusCrazyBar());

        MaterialButton saveButton = exitDialog.findViewById(R.id.saveAndExitButton);
        MaterialButton exitButton = exitDialog.findViewById(R.id.exitButton);

        saveButton.setOnClickListener(onClickSaveButton());
        exitButton.setOnClickListener(onClickExitButton());
        exitDialog.show();
    }

    private View.OnClickListener onClickMinusCrazyBar() {
        return v -> {
            if (crazyCount > 0) {
                crazyCount--;
                crazyBar.setProgress(crazyCount);
            }
        };
    }

    private View.OnClickListener onClickPlusCrazyBar() {
        return v -> {
            if (crazyCount < 10) {
                crazyCount++;
                crazyBar.setProgress(crazyCount);
            }
        };
    }

    private View.OnClickListener onClickSaveButton() {
        return v -> {
            exitDialog.dismiss();
            try {
                EventDAO eventDAO = EventDAOFactory.get(getApplicationContext());

                if (!locations.isEmpty() || !people.isEmpty() || !photoURI.isEmpty()) {
                    Event event = new Event(date, photoURI, locations, people, crazyCount);

                    Executors.newSingleThreadExecutor().execute(() -> eventDAO.upsertEvent(event));

                    SharedPreferencesManager.saveBoolean(MainActivity.getEventFlags(), date, true);
                    SharedPreferencesManager.incrementIntByKey(MainActivity.getTotalEventDays(),
                            date.substring(date.length()-4));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            finish();
        };
    }

    private View.OnClickListener onClickExitButton() {
        return v -> {
            exitDialog.dismiss();
            finish();
        };
    }
}