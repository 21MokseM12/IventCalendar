package com.example.iventcalendar.services.implementations.factories;

import android.content.Context;

import androidx.room.Room;

import com.example.iventcalendar.services.implementations.database.EventDataBase;
import com.example.iventcalendar.services.interfaces.database.EventDAO;

public class EventDAOFactory {

    private EventDAOFactory() {}

    public static EventDAO get(Context context) {
        return Room.databaseBuilder(context, EventDataBase.class, "app-database").build().eventDAO();
    }
}
