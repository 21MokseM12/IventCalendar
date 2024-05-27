package com.example.iventcalendar.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.ui.settings.SectionsPagerAdapter;
import com.example.iventcalendar.databinding.ActivityEventSettingsBinding;

public class EventSettingsActivity extends AppCompatActivity {

    private ActivityEventSettingsBinding binding;

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
    }
}