package com.example.iventcalendar.service.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.iventcalendar.entities.Event;

@Dao
public interface EventDAO {
    @Upsert
    void upsertEvent(Event event);
    @Delete
    void deleteEvent(Event event);
    @Query("SELECT * FROM events event where event.dateId = :date")
    LiveData<Event> getEventInfoByDate(String date);
    @Query("SELECT COUNT(*) FROM events event where event.dateId = :date")
    LiveData<Integer> isEventExist(String date);
    @Query("DELETE FROM events WHERE dateId = :date")
    void deleteEventByDate(String date);
}