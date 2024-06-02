package com.example.iventcalendar.activities.tabs.settings_tabs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.FragmentDataListener;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.LocationAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabThirdPeople  extends Fragment implements FragmentDataListener {
    protected List<String> people;
    public FloatingActionButton addPeople;
    private Dialog peopleDialog;
    private LocationAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_people_settings, container, false);
        TextView titleOfFragment = rootView.findViewById(R.id.titleOfPeopleSettings);
        titleOfFragment.setText("С кем кочевряжились сегодня?)");
        people = new ArrayList<>();
        addPeople = rootView.findViewById(R.id.addPeopleButton);
        peopleDialog = new Dialog(this.requireActivity());
        addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        ListView locationsSettings = rootView.findViewById(R.id.peopleViewSettingsList);
        adapter = new LocationAdapter(this.requireActivity(), people);
        locationsSettings.setAdapter(adapter);
        return rootView;
    }
    private void showCustomDialog() {
        peopleDialog.setContentView(R.layout.custom_settings_people_dialog);
        Objects.requireNonNull(peopleDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        peopleDialog.setCancelable(true);

        EditText placeToAdd = peopleDialog.findViewById(R.id.setPeopleText);
        MaterialButton toAddButton = peopleDialog.findViewById(R.id.addPeopleButton);

        toAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!placeToAdd.getText().toString().trim().isEmpty()) {
                    if (!people.contains(placeToAdd.getText().toString())) {
                        people.add(placeToAdd.getText().toString().trim());
                        adapter.notifyDataSetChanged();
                    }
                } else Toast.makeText(requireContext(), "Ничего же не написано...", Toast.LENGTH_LONG).show();
                peopleDialog.dismiss();
            }
        });

        peopleDialog.show();
    }
    public String getFragmentData() {
        StringBuilder builder = new StringBuilder();
        for (String guy : people) builder.append(guy).append(' ');
        return builder.toString().trim();
    }
}
