package com.example.pedarkharj_edit3.classes;

import com.alirezaafkar.sundatepicker.components.DateItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//Persian Calender
public class PersianDate extends DateItem {
    private int mYear, mMonth, mDay;
    private  String shortDate, fullDate;
    private Calendar calendar;

    public PersianDate() {
        this.calendar = getCalendar();
        this.mYear = calendar.get(Calendar.YEAR);
        this.mMonth = calendar.get(Calendar.MONTH);
        this.mDay = calendar.get(Calendar.DAY_OF_MONTH);
        this.shortDate = getShortDate();
        this.fullDate = getFullDate();
    }

    //-------------- getters ---------------//


    public int getmYear() {
        return Integer.valueOf( String.format(Locale.US, "%d", getYear(), + calendar.get(Calendar.YEAR)) ) ;
    }

    public int getmMonth() {
        return Integer.valueOf( String.format(Locale.US, "%d", getYear(), + calendar.get(Calendar.MONTH)) ) ;
    }

    public int getmDay() {
        return Integer.valueOf( String.format(Locale.US, "%d", getYear(), + calendar.get(Calendar.DAY_OF_MONTH)) ) ;
    }


    public String getShortDate() {
//            Calendar calendar = getCalendar();
        return String.format(Locale.US,
                "%d/%d",
                getMonth(), getDay(),
                +calendar.get(Calendar.MONTH) + 1,
                +calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String getFullDate() {
//            Calendar calendar = getCalendar();
        return String.format(Locale.US,
                "%d/%d/%d",
                getYear(), getMonth(), getDay(),
                calendar.get(Calendar.YEAR),
                +calendar.get(Calendar.MONTH) + 1,
                +calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String getFullDateNTime() {
        StringBuilder builder = new StringBuilder();

        builder.append(
                String.format(Locale.US,
                        "%d/%d/%d",
                        getYear(), getMonth(), getDay(),
                        calendar.get(Calendar.YEAR),
                        +calendar.get(Calendar.MONTH) + 1,
                        +calendar.get(Calendar.DAY_OF_MONTH))
        );
        builder.append(" ");
        builder.append(getHourTime());

        return builder.toString();
    }

    private String getHourTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}


