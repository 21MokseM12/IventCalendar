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

public class TabSecondLocation  extends Fragment implements FragmentDataListener {
    protected List<String> locations;
    private FloatingActionButton addLocation;
    Dialog locationDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_location_settings, container, false);
        TextView titleOfFragment = rootView.findViewById(R.id.titleOfLocationsSettings);
        titleOfFragment.setText("Куда вас Бог занёс на этот раз?)");
        locations = new ArrayList<>();
        addLocation = rootView.findViewById(R.id.addLocationButton);
        locationDialog = new Dialog(this.requireActivity());
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        ListView locationsSettings = rootView.findViewById(R.id.locationsViewSettingsList);
        LocationAdapter adapter = new LocationAdapter(this.requireActivity(), locations);
        locationsSettings.setAdapter(adapter);
        return rootView;
    }
    private void showCustomDialog() {
        locationDialog.setContentView(R.layout.custom_settings_location_dialog);
        Objects.requireNonNull(locationDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        locationDialog.setCancelable(true);

        EditText placeToAdd = locationDialog.findViewById(R.id.setLocationText);
        MaterialButton toAddButton = locationDialog.findViewById(R.id.addLocationButton);

        toAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!placeToAdd.getText().toString().trim().isEmpty()) {
                    if (!locations.contains(placeToAdd.getText().toString().trim()))
                        locations.add(placeToAdd.getText().toString().trim());
                } else Toast.makeText(requireContext(), "Ничего же не написано...", Toast.LENGTH_LONG).show();
                locationDialog.dismiss();
            }
        });

        locationDialog.show();
    }
    public String getFragmentData() {
        StringBuilder builder = new StringBuilder();
        for (String location : locations) builder.append(location).append(' ');
        return builder.toString().trim();
    }
}
