package com.example.healthbuddy.Model;

public class StreakData {
    String Day;


    String Date;
    long time;
    long meditationTime;

    public StreakData( ) {

    }
    public StreakData(String day, String date, long time, long meditationTime) {
        Day = day;
        Date = date;
        this.time = time;
        this.meditationTime = meditationTime;
    }

    public long getMeditationTime() {
        return meditationTime;
    }

    public void setMeditationTime(long meditationTime) {
        this.meditationTime = meditationTime;
    }



    public StreakData(String day, long time, long meditationTime) {
        Day = day;
        this.time = time;
        this.meditationTime = meditationTime;

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
