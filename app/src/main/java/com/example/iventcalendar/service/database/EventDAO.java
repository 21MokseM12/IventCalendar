package com.example.iventcalendar.service.database;

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
    Event getEventInfoByDate(String date);
}
