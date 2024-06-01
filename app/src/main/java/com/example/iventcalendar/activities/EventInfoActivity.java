package com.example.iventcalendar.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.info_tabs.TabFirstPhotos;
import com.example.iventcalendar.activities.tabs.info_tabs.TabFourthCrazyCount;
import com.example.iventcalendar.activities.tabs.info_tabs.TabSecondLocation;
import com.example.iventcalendar.activities.tabs.info_tabs.TabThirdPeople;
import com.example.iventcalendar.entities.Event;
import com.example.iventcalendar.service.database.EventDAO;
import com.example.iventcalendar.service.database.EventDataBase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.OnBackPressedCallback;
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
    private List<String> outputData;
    private ActivityEventInfoBinding binding;
    private Dialog waitDialog;
    private Dialog exitDialog;
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

                            exitDialog = new Dialog(EventInfoActivity.this);
                            getOnBackPressedDispatcher().addCallback(EventInfoActivity.this, new OnBackPressedCallback(true) {
                                @Override
                                public void handleOnBackPressed() {
                                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                                    outputData = new ArrayList<>();
                                    for (Fragment fragment: fragments) {
                                        if (fragment instanceof TabFirstPhotos) {
                                            if (((TabFirstPhotos) fragment).getFragmentData() == null) outputData.add("");
                                            else outputData.add(((TabFirstPhotos) fragment).getFragmentData());
                                        }
                                        else if (fragment instanceof TabSecondLocation) outputData.add(((TabSecondLocation) fragment).getFragmentData());
                                        else if (fragment instanceof TabThirdPeople) outputData.add(((TabThirdPeople) fragment).getFragmentData());
                                        else if (fragment instanceof TabFourthCrazyCount) outputData.add(((TabFourthCrazyCount) fragment).getFragmentData());
                                    }
                                    if (inputData.equals(outputData)) finish();
                                    else showExitDialog();
                                }
                            });
                        }
                    });
                }
            }
        });
    }
    private void showExitDialog() {
        exitDialog.setContentView(R.layout.confirm_changes_dialog);
        Objects.requireNonNull(exitDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        exitDialog.setCancelable(true);
        TextView title = exitDialog.findViewById(R.id.exitTitle);
        title.setText("Сохранить изменения?");

        MaterialButton saveButton = exitDialog.findViewById(R.id.saveAndExitButton);
        MaterialButton exitButton = exitDialog.findViewById(R.id.exitButton);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                try {
                    dataBase = Room.databaseBuilder(getApplicationContext(), EventDataBase.class, "app-database").build();
                    EventDAO eventDAO = dataBase.eventDAO();
                    if (outputData.get(1).isEmpty() && outputData.get(2).isEmpty() && outputData.get(0).isEmpty()) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            eventDAO.deleteEventByDate(date);
                            MainActivity.deleteEventDayFlag(date);
                        });
                    }
                    else if (!outputData.get(1).isEmpty() || !outputData.get(2).isEmpty() || !outputData.get(0).isEmpty()){
                        Event event = new Event(date, outputData.get(0), outputData.get(1), outputData.get(2), Integer.parseInt(outputData.get(3)));
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

        exitDialog.show();
    }
}