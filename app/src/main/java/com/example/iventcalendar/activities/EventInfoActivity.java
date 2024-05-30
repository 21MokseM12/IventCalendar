package com.example.iventcalendar.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.iventcalendar.entities.Event;
import com.example.iventcalendar.service.database.EventDAO;
import com.example.iventcalendar.service.database.EventDataBase;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.ui.info.SectionsPagerAdapter;
import com.example.iventcalendar.databinding.ActivityEventInfoBinding;

import java.util.concurrent.Executors;

public class EventInfoActivity extends AppCompatActivity {
    private ActivityEventInfoBinding binding;
    private EventDataBase dataBase;
    private String date;
    private String photoURI;
    private String locations;
    private String people;
    private int crazyCount = 0;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        date = getIntent().getStringExtra("date");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        TextView title = binding.title;
        title.setText(new String(Character.toChars(0x1f973)) + " МЕРОПРИЯТИЕ " + new String(Character.toChars(0x1f973)));

        dataBase = Room.databaseBuilder(getApplicationContext(), EventDataBase.class, "app-database").build();
        EventDAO eventDAO = dataBase.eventDAO();
        if (eventDAO.isEventExist(date).getValue() == 1) {
            Executors.newSingleThreadExecutor().execute(() -> {
                Event event = eventDAO.getEventInfoByDate(date).getValue();
                if (event != null) {
                    photoURI = event.getPhotoPath();
                    locations = event.getLocation();
                    people = event.getPeople();
                    crazyCount = event.getCrazyCount();
                }
                System.out.println(photoURI);
                System.out.println(locations);
                System.out.println(people);
                System.out.println(crazyCount);
            });
        }


    }
    public String getPhotoURI() {return this.photoURI;}
    public String getLocations() {return this.locations;}
    public String getPeople() {return this.people;}
    public int getCrazyCount() {return this.crazyCount;}
}