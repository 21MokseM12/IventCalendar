package com.example.iventcalendar.activities.tabs.info_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.FragmentDataListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class TabFourthCrazyCount  extends Fragment implements FragmentDataListener {
    private final static String ARG_CRAZY = "crazyCount";
    private int crazyCount = 0;

    public static TabFourthCrazyCount newInstance(String arg) {
        TabFourthCrazyCount fragment = new TabFourthCrazyCount();
        Bundle args = new Bundle();
        args.putString(ARG_CRAZY, arg);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getString(ARG_CRAZY) != null)
                crazyCount = Integer.parseInt(getArguments().getString(ARG_CRAZY));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4_crazy_info, container, false);
        TextView title = rootView.findViewById(R.id.crazyTitle);
        if (crazyCount >= 8) title.setText("ЭТО ПОЛНОЕ БЕЗУМСТВО!");
        else if (crazyCount < 4) title.setText("На плитах были?)");
        else title.setText("А ты хороша, звучит интригующе!");

        FloatingActionButton plusButton = rootView.findViewById(R.id.plusOfCrazyButton);
        FloatingActionButton minusButton = rootView.findViewById(R.id.minusOfCrazyButton);

        ProgressBar crazyBar = rootView.findViewById(R.id.crazyProgressBar);
        crazyBar.setProgress(crazyCount);
        MaterialButton changeCrazy = rootView.findViewById(R.id.changeButton);

        changeCrazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(changeCrazy.getText());
                if (changeCrazy.getText().equals("Изменить")) {
                    plusButton.setVisibility(View.VISIBLE);
                    minusButton.setVisibility(View.VISIBLE);
                    changeCrazy.setText("Сохранить");
                }
                else if (changeCrazy.getText().equals("Сохранить")) {
                    plusButton.setVisibility(View.INVISIBLE);
                    minusButton.setVisibility(View.INVISIBLE);
                    changeCrazy.setText("Изменить");
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (crazyCount > 0) {
                    crazyCount--;
                    crazyBar.setProgress(crazyCount);
                    title.setText(getTitleText(crazyCount));
                }
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (crazyCount < 10) {
                    crazyCount++;
                    crazyBar.setProgress(crazyCount);
                    title.setText(getTitleText(crazyCount));
                }
            }
        });
        return rootView;
    }
    private String getTitleText(int crazyCount) {
        if (crazyCount >= 8) return "ЭТО ПОЛНОЕ БЕЗУМСТВО!";
        else if (crazyCount < 4) return "На плитах были?)";
        else return "А ты хороша, звучит интригующе!";
    }
    public String getFragmentData() {
        return String.valueOf(this.crazyCount);
    }
}