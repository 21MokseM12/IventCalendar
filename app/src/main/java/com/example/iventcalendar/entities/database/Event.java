package com.example.iventcalendar.entities.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey
    @NonNull
    private String dateId;
    private String photoPath;
    private String location;
    private String people;
    private int crazyCount;

    public Event(@NonNull String dateId, String photoPath, String location, String people, int crazyCount) {
        this.dateId = dateId;
        this.photoPath = photoPath;
        this.location = location;
        this.people = people;
        this.crazyCount = crazyCount;
    }
    public Event(@NonNull String dateId, String photoPath, String location, String[] people, int crazyCount) {
        this.dateId = dateId;
        this.photoPath = photoPath;
        this.location = location;
        StringBuilder builder = new StringBuilder();
        for (String guy : people) builder.append(guy).append(' ');
        this.people = builder.toString();
        this.crazyCount = crazyCount;
    }

    @NonNull
    public String getDateId() {
        return dateId;
    }
    public String getPhotoPath() {
        return photoPath;
    }
    public String getLocation() {
        return location;
    }
    public String getPeople() {
        return people;
    }
    public int getCrazyCount() {
        return crazyCount;
    }
}
