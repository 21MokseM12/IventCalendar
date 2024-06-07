package com.example.iventcalendar.service.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.iventcalendar.entities.Event;

@Database(
        entities = {Event.class},
        version = 1
)
public abstract class EventDataBase extends RoomDatabase{
    public abstract EventDAO eventDAO();
}