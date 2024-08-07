package com.example.iventcalendar.services.implementations.decorators;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class ChosenDateDecorator implements DayViewDecorator {

    private CalendarDay date;

    private final Drawable drawable;

    @SuppressLint("UseCompatLoadingForDrawables")
    public ChosenDateDecorator(Context context, int drawableResId) {
        date = null;
        drawable = context.getDrawable(drawableResId);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }

    public void setDate(CalendarDay day) {date = day;}
}