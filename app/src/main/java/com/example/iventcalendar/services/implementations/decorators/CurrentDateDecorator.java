package com.example.iventcalendar.services.implementations.decorators;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CurrentDateDecorator implements DayViewDecorator {

    private final CalendarDay currentDate;

    private final Drawable drawable;

    @SuppressLint("UseCompatLoadingForDrawables")
    public CurrentDateDecorator(Context context, CalendarDay currentDate, int drawableResId) {
        this.currentDate = currentDate;
        drawable = context.getDrawable(drawableResId);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(currentDate);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
