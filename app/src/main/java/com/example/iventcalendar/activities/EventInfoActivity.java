package com.example.iventcalendar.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.info_tabs.TabFirstPhotos;
import com.example.iventcalendar.entities.Event;
import com.example.iventcalendar.service.database.EventDAO;
import com.example.iventcalendar.service.database.EventDataBase;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.ui.info.SectionsPagerAdapter;
import com.example.iventcalendar.databinding.ActivityEventInfoBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class EventInfoActivity extends AppCompatActivity {
    private List<String> inputData;
    private ActivityEventInfoBinding binding;
    private Dialog waitDialog;
    private EventDataBase dataBase;
    private String date;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        date = getIntent().getStringExtra("date");

        final Integer[] exist = {0};
        dataBase = Room.databaseBuilder(getApplicationContext(), EventDataBase.class, "app-database").build();
        EventDAO eventDAO = dataBase.eventDAO();
        waitDialog = new Dialog(EventInfoActivity.this);
        showWaitDialog();
        LiveData<Integer> liveDataExist = eventDAO.isEventExist(date);
        liveDataExist.observe(EventInfoActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                exist[0] = integer;
                if (exist[0] == 1) {
                    LiveData<Event> event = eventDAO.getEventInfoByDate(date);
                    event.observe(EventInfoActivity.this, new Observer<Event>() {
                        @Override
                        public void onChanged(Event event) {
                            if (event != null) {
                                inputData = new ArrayList<>();
                                inputData.add(event.getPhotoPath());
                                inputData.add(event.getLocation());
                                inputData.add(event.getPeople());
                                inputData.add(String.valueOf(event.getCrazyCount()));
                            }

                            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), inputData);
                            ViewPager viewPager = binding.viewPager;
                            viewPager.setAdapter(sectionsPagerAdapter);
                            viewPager.setOffscreenPageLimit(4);
                            TabLayout tabs = binding.tabs;
                            tabs.setupWithViewPager(viewPager);
                            TextView title = binding.title;
                            title.setText(new String(Character.toChars(0x1f973)) + " МЕРОПРИЯТИЕ " + new String(Character.toChars(0x1f973)));
                            waitDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
    private void showWaitDialog() {
        waitDialog.setContentView(R.layout.wait_for_loading_dialog_layout);
        Objects.requireNonNull(waitDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        waitDialog.setCancelable(false);
        waitDialog.show();
    }
}