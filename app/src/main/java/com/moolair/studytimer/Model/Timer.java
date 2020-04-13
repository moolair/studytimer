package com.moolair.studytimer.Model;

public class Timer {

    private String subject;
    private String hour;
    private String minute;
    private int id;

    public Timer() {
    }

    public Timer(String subject, String hour, String minute, int id) {
        this.subject = subject;
        this.hour = hour;
        this.minute = minute;
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
