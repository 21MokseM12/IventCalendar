package com.example.iventcalendar.services.implementations.decorators;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class ActiveEventDecorator implements DayViewDecorator {
    private final SharedPreferences flags;
    private final Drawable drawable;

    @SuppressLint("UseCompatLoadingForDrawables")
    public ActiveEventDecorator(Context context, SharedPreferences flags, int resId) {
        this.flags = flags;
        this.drawable = context.getDrawable(resId);
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