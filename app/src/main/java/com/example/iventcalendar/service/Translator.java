package com.example.iventcalendar.service;

import java.time.Month;

public class Translator {
    public static String monthTranslate(Month month) {
        switch (month.toString()) {
            case "JANUARY":
                return "января";
            case "FEBRUARY":
                return "февраля";
            case "MARCH":
                return "марта";
            case "APRIL":
                return "апреля";
            case "MAY":
                return "мая";
            case "JUNE":
                return "июня";
            case "JULY":
                return "июля";
            case "AUGUST":
                return "августа";
            case "SEPTEMBER":
                return "сентября";
            case "OCTOBER":
                return "октября";
            case "NOVEMBER":
                return "ноября";
            case "DECEMBER":
                return "декабря";
            default:
                return "";
        }
    }
}
