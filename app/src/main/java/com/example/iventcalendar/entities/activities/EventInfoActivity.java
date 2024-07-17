package com.example.iventcalendar.entities.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.iventcalendar.R;
import com.example.iventcalendar.entities.tabs.info_tabs.TabFirstPhotos;
import com.example.iventcalendar.entities.tabs.info_tabs.TabFourthCrazyCount;
import com.example.iventcalendar.entities.tabs.info_tabs.TabSecondLocation;
import com.example.iventcalendar.entities.tabs.info_tabs.TabThirdPeople;
import com.example.iventcalendar.entities.database.Event;
import com.example.iventcalendar.services.implementations.factories.EventDAOFactory;
import com.example.iventcalendar.services.interfaces.database.EventDAO;
import com.example.iventcalendar.managers.PhotoFileManager;
import com.example.iventcalendar.managers.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iventcalendar.services.implementations.adapters.info.SectionsPagerAdapter;
import com.example.iventcalendar.databinding.ActivityEventInfoBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class EventInfoActivity extends AppCompatActivity {

    private List<String> inputData;

    private List<String> outputData;

    private ActivityEventInfoBinding binding;

    private Dialog exitDialog;

    private String date;

    private EventDAO eventDAO;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        date = getIntent().getStringExtra("date");

        eventDAO = EventDAOFactory.get(getApplicationContext());

        final Integer[] exist = {0};

        LiveData<Integer> liveDataExist = eventDAO.isEventExist(date);
        liveDataExist.observe(EventInfoActivity.this, integer -> {
            exist[0] = integer;
            if (exist[0] == 1) {
                LiveData<Event> event = eventDAO.getEventInfoByDate(date);
                event.observe(EventInfoActivity.this, event1 -> {
                    if (event1 != null) {
                        inputData = new ArrayList<>();
                        inputData.add(event1.getPhotoPath());
                        inputData.add(event1.getLocation());
                        inputData.add(event1.getPeople());
                        inputData.add(String.valueOf(event1.getCrazyCount()));
                    }

                    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), inputData, date);
                    ViewPager viewPager = binding.viewPager;
                    viewPager.setOffscreenPageLimit(10);
                    viewPager.setAdapter(sectionsPagerAdapter);

                    TabLayout tabs = binding.tabs;
                    tabs.setupWithViewPager(viewPager);

                    TextView title = binding.title;
                    title.setText(new String(Character.toChars(0x1f973)) +
                            getString(R.string.title_text_information_activity) +
                            new String(Character.toChars(0x1f973)));

                    getOnBackPressedDispatcher().addCallback(EventInfoActivity.this, new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            List<Fragment> fragments = getSupportFragmentManager().getFragments();
                            outputData = new ArrayList<>();
                            for (Fragment fragment: fragments) {
                                if (fragment instanceof TabFirstPhotos) outputData.add(((TabFirstPhotos) fragment).getFragmentData());
                                else if (fragment instanceof TabSecondLocation) outputData.add(((TabSecondLocation) fragment).getFragmentData());
                                else if (fragment instanceof TabThirdPeople) outputData.add(((TabThirdPeople) fragment).getFragmentData());
                                else if (fragment instanceof TabFourthCrazyCount) outputData.add(((TabFourthCrazyCount) fragment).getFragmentData());
                            }
                            if (inputData.equals(outputData)) finish();
                            else showExitDialog();
                        }
                    });
                });
            }
        });
    }

    private void showExitDialog() {
        exitDialog = new Dialog(EventInfoActivity.this);
        exitDialog.setContentView(R.layout.confirm_changes_dialog);
        Objects.requireNonNull(exitDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        exitDialog.setCancelable(true);

        TextView title = exitDialog.findViewById(R.id.exitTitle);
        title.setText(R.string.save_changes);

        MaterialButton saveButton = exitDialog.findViewById(R.id.saveAndExitButton);
        MaterialButton exitButton = exitDialog.findViewById(R.id.exitButton);

        exitButton.setOnClickListener(onClickExitButton());
        saveButton.setOnClickListener(onClickSaveButton());

        exitDialog.show();
    }

    private View.OnClickListener onClickExitButton() {
        return v -> {
            PhotoFileManager.deletePhotoFile(
                    getApplicationContext(),
                    date + "_copy.jpg"
            );
            exitDialog.dismiss();
            finish();
        };
    }

    private View.OnClickListener onClickSaveButton() {
        return view -> {
            exitDialog.dismiss();
            try {
                if (!isNotEmptyData()) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        eventDAO.deleteEventByDate(date);
                        SharedPreferencesManager.deleteByKey(MainActivity.getEventFlags(), date);
                        SharedPreferencesManager.decrementIntByKey(MainActivity.getTotalEventDays(), date.substring(date.length()-4));
                    });
                }
                else {
                    if (outputData.get(0).isEmpty()) {
                        PhotoFileManager.deletePhotoFile(getApplicationContext(),
                                date + ".jpg");
                        PhotoFileManager.deletePhotoFile(getApplicationContext(),
                                date + "_copy.jpg");
                    }
                    else {
                        String newUri = PhotoFileManager.getRenamedCopyOfPhotoFileUri(getApplicationContext(),
                                date);
                        outputData.remove(0);
                        outputData.add(0, newUri);
                    }

                    Event event = new Event(date, outputData.get(0), outputData.get(1), outputData.get(2), Integer.parseInt(outputData.get(3)));

                    Executors.newSingleThreadExecutor().execute(() -> eventDAO.upsertEvent(event));

                    SharedPreferencesManager.saveBoolean(MainActivity.getEventFlags(), date, true);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            finish();
        };
    }

    private boolean isNotEmptyData() {
        for (int i = 0; i < outputData.size()-1; i++) if (!outputData.get(i).isEmpty()) return true;
        return false;
    }
}