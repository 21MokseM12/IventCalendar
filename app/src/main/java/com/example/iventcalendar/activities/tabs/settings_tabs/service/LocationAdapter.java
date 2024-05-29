package com.example.iventcalendar.activities.tabs.settings_tabs.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.iventcalendar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LocationAdapter extends ArrayAdapter {
    private Context context;
    private List<String> locations;
    public LocationAdapter(Context context, List<String> locations) {
        super(context, R.layout.list_view_item_location_settings, locations);
        this.context = context;
        this.locations = locations;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_view_item_location_settings, parent, false);
        TextView location = view.findViewById(R.id.location);
        FloatingActionButton deleteLocation = view.findViewById(R.id.deleteLocationActionButton);
        deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locations.remove(position);
                LocationAdapter.super.notifyDataSetChanged();
            }
        });
        location.setText(locations.get(position));
        return view;
    }
}
