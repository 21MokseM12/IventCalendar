package com.example.iventcalendar.activities.tabs.info_tabs;

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

import androidx.fragment.app.Fragment;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.FragmentDataListener;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.LocationAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TabSecondLocation  extends Fragment implements FragmentDataListener {
    private static final String ARG_LOCATIONS = "locations";
    private List<String> locations;
    private FloatingActionButton addLocation;
    Dialog locationDialog;

    public static TabSecondLocation newInstance(String arg) {
        TabSecondLocation fragment = new TabSecondLocation();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATIONS, arg);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (Objects.equals(getArguments().getString(ARG_LOCATIONS), "")) locations = new ArrayList<>();
            else if (getArguments().getString(ARG_LOCATIONS) != null)
                locations = Arrays.stream(requireArguments().getString(ARG_LOCATIONS).split(" ")).collect(Collectors.toList());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_location_settings, container, false);
        TextView titleOfFragment = rootView.findViewById(R.id.titleOfLocationsSettings);
        titleOfFragment.setText("Круто попутешествовали, правда?");


        addLocation = rootView.findViewById(R.id.addLocationButton);
        locationDialog = new Dialog(this.requireActivity());
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        ListView locationsSettings = rootView.findViewById(R.id.locationsViewSettingsList);
        LocationAdapter adapter;
//        if (locations == null) locations = new ArrayList<>();
        adapter = new LocationAdapter(this.requireActivity(), locations);
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
                if (!locations.contains(placeToAdd.getText().toString().trim()))
                    locations.add(placeToAdd.getText().toString());
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

