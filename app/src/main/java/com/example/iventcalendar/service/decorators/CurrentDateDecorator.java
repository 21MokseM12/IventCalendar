package com.example.iventcalendar.service.decorators;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CurrentDateDecorator implements DayViewDecorator {
    private CalendarDay date;
    public CurrentDateDecorator() {date = null;}
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }
    public void setDate(CalendarDay day) {date = day;}
}
