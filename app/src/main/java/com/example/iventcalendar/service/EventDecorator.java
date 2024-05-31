package com.example.iventcalendar.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class EventDecorator implements DayViewDecorator {
    private final SharedPreferences flags;
    private final Drawable drawable;

    @SuppressLint("UseCompatLoadingForDrawables")
    public EventDecorator(Context context, SharedPreferences flags, int drawableResId) {
        this.flags = flags;
        this.drawable = context.getDrawable(drawableResId);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        String date = String.valueOf(day.getDay()) + (day.getMonth()+1) + day.getYear();
        return flags.contains(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }

}
