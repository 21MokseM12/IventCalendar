package com.example.iventcalendar.services.implementations.adapters;

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

public class ListViewAdapter extends ArrayAdapter {
    private final Context context;
    private final List<String> listViewContent;
    public ListViewAdapter(Context context, List<String> listViewContent) {
        super(context, R.layout.list_view_item_location_settings, listViewContent);
        this.context = context;
        this.listViewContent = listViewContent;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_view_item_location_settings, parent, false);
        TextView location = view.findViewById(R.id.location);
        FloatingActionButton deleteLocation = view.findViewById(R.id.deleteLocationActionButton);
        deleteLocation.setOnClickListener(v -> {
            listViewContent.remove(position);
            ListViewAdapter.super.notifyDataSetChanged();
        });
        location.setText(listViewContent.get(position));
        return view;
    }
}
