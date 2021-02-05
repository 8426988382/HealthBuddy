package com.example.healthbuddy.Model;

public class StreakData {
    String Day;


    String Date;
    long time;

    public StreakData( ) {

    }

    public StreakData(String day, long time) {
        Day = day;
        this.time = time;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
