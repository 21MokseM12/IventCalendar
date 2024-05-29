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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.ui.settings.SectionsPagerAdapter;
import com.example.iventcalendar.databinding.ActivityEventSettingsBinding;

import java.util.List;
import java.util.Objects;

public class EventSettingsActivity extends AppCompatActivity {

    private ActivityEventSettingsBinding binding;
    Dialog exitDialog;
    private String photoPath;
    private List<String> locations;
    private List<String> people;
    private int crazyCount = 0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
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
        exitDialog.setContentView(R.layout.custom_exit_dialog_layout);
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(crazyCount);
                exitDialog.dismiss();
                finish();
            }
        });
        exitDialog.show();
    }
}