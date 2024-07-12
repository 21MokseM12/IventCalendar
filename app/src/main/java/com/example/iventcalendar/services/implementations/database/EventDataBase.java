package com.example.iventcalendar.services.implementations.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.iventcalendar.entities.database.Event;
import com.example.iventcalendar.services.interfaces.database.EventDAO;

@Database(
        entities = {Event.class},
        version = 1
)
public abstract class EventDataBase extends RoomDatabase{
    public abstract EventDAO eventDAO();
}