package com.example.iventcalendar.service.decorators;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CurrentDateDecorator implements DayViewDecorator {
    private CalendarDay date;
    private final Drawable drawable;
    @SuppressLint("UseCompatLoadingForDrawables")
    public CurrentDateDecorator(Context context, int drawableResId) {
        date = null;
        drawable = context.getDrawable(drawableResId);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        view.setSelectionDrawable(drawable);

    }
    public void setDate(CalendarDay day) {date = day;}
}
