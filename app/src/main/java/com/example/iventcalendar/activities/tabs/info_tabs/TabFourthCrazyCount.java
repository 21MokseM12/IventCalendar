package com.example.iventcalendar.activities.tabs.info_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.FragmentDataListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TabFourthCrazyCount  extends Fragment implements FragmentDataListener {
    private final static String ARG_CRAZY = "crazyCount";
    private int crazyCount = 0;
    private TextView title;
    private ImageView crazyImage;

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
        title = rootView.findViewById(R.id.crazyTitle);
        crazyImage = rootView.findViewById(R.id.crazyImage);
        setViewDesign();

        FloatingActionButton plusButton = rootView.findViewById(R.id.plusOfCrazyButton);
        FloatingActionButton minusButton = rootView.findViewById(R.id.minusOfCrazyButton);


        ProgressBar crazyBar = rootView.findViewById(R.id.crazyProgressBar);
        crazyBar.setProgress(crazyCount);
        MaterialButton changeCrazy = rootView.findViewById(R.id.changeButton);

        changeCrazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    setViewDesign();
                }
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (crazyCount < 10) {
                    crazyCount++;
                    crazyBar.setProgress(crazyCount);
                    setViewDesign();
                }
            }
        });
        return rootView;
    }
    private void setViewDesign() {
        title.setText(getTitleText());
        getImage();
    }
    private void getImage() {
        if (crazyCount >= 8) crazyImage.setImageResource(R.drawable.fourth_crazy_circle);
        else if (crazyCount < 4 && crazyCount > 0) crazyImage.setImageResource(R.drawable.second_crazy_circle);
        else if (crazyCount == 0) crazyImage.setImageResource(R.drawable.first_crazy_circle);
        else crazyImage.setImageResource(R.drawable.third_crazy_circle);
    }
    private String getTitleText() {
        if (crazyCount >= 8) return "ОЙОЙОООООООЙ";
        else if (crazyCount < 4 && crazyCount > 0) return "На плитах были?)";
        else if (crazyCount == 0) return "Не прибедняяяяяйся";
        else return "Некое веселье :D";
    }
    public String getFragmentData() {
        return String.valueOf(this.crazyCount);
    }
}