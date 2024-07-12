package com.example.iventcalendar.services.interfaces.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.iventcalendar.entities.database.Event;

@Dao
public interface EventDAO {
    @Upsert
    void upsertEvent(Event event);
    @Query("SELECT * FROM events event where event.dateId = :date")
    LiveData<Event> getEventInfoByDate(String date);
    @Query("SELECT COUNT(*) FROM events event where event.dateId = :date")
    LiveData<Integer> isEventExist(String date);
    @Query("DELETE FROM events WHERE dateId = :date")
    void deleteEventByDate(String date);
}