package com.example.iventcalendar.entities.tabs.settings_tabs;

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
import com.example.iventcalendar.services.interfaces.listeners.FragmentDataListener;
import com.example.iventcalendar.services.implementations.adapters.ListViewAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabThirdPeople  extends Fragment implements FragmentDataListener {

    private List<String> people;

    public FloatingActionButton addPeople;

    private Dialog peopleDialog;

    private ListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_people_settings, container, false);
        TextView titleOfFragment = rootView.findViewById(R.id.titleOfPeopleSettings);
        titleOfFragment.setText(R.string.title_tab_people_settings);

        people = new ArrayList<>();
        addPeople = rootView.findViewById(R.id.addPeopleButton);
        addPeople.setOnClickListener(v -> showCustomDialog());

        ListView locationsSettings = rootView.findViewById(R.id.peopleViewSettingsList);
        adapter = new ListViewAdapter(this.requireActivity(), people);
        locationsSettings.setAdapter(adapter);
        return rootView;
    }

    private void showCustomDialog() {
        peopleDialog = new Dialog(this.requireActivity());
        peopleDialog.setContentView(R.layout.custom_settings_people_dialog);
        Objects.requireNonNull(peopleDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        peopleDialog.setCancelable(true);

        EditText placeToAdd = peopleDialog.findViewById(R.id.setPeopleText);
        MaterialButton toAddButton = peopleDialog.findViewById(R.id.addPeopleButton);

        toAddButton.setOnClickListener(v -> {
            if (!placeToAdd.getText().toString().trim().isEmpty()) {
                people.add(placeToAdd.getText().toString().trim());
                adapter.notifyDataSetChanged();
            } else Toast.makeText(requireContext(), "Ничего же не написано...", Toast.LENGTH_LONG).show();
            peopleDialog.dismiss();
        });

        peopleDialog.show();
    }

    public String getFragmentData() {
        StringBuilder builder = new StringBuilder();
        for (String guy : people) builder.append(guy).append(';');
        return builder.toString().trim();
    }
}
