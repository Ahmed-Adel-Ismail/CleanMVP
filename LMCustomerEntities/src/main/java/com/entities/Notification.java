package com.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Wafaa on 11/14/2016.
 */

public class Notification implements Comparable<Notification>, Serializable {

    private String notificationItemTitle;
    private String notificationItemBody;
    private String notificationTitle;
    private String notificationBody;
    private Long time;
    private String type;
    private String payload;
    private boolean requesting;
    private Long timestamp;

    public Notification() {

    }


    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody) {
        this.notificationBody = notificationBody;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotificationItemTitle() {
        return notificationItemTitle;
    }

    public void setNotificationItemTitle(String notificationItemTitle) {
        this.notificationItemTitle = notificationItemTitle;
    }

    public String getNotificationItemBody() {
        return notificationItemBody;
    }

    public void setNotificationItemBody(String notificationItemBody) {
        this.notificationItemBody = notificationItemBody;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimeFormatted(long milliSeconds) {
        String timeFormat = "hh:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat, Locale.UK);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof Notification
                && ((Notification) obj).timestamp == timestamp;
    }

    @Override
    public int hashCode() {
        return (int) (Long.valueOf(timestamp) / 2);
    }

    @Override
    public int compareTo(Notification o) {
        return o.timestamp.compareTo(this.timestamp);
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isRequesting() {
        return requesting;
    }

    public void setRequesting(boolean requesting) {
        this.requesting = requesting;
    }
}
