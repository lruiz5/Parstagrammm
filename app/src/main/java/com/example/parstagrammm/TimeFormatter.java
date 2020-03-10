package com.example.parstagrammm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Given a date String of the format given by the Twitter API, returns a display-formatted
 * String representing the relative time difference, e.g. "2m", "6d", "23 May", "1 Jan 14"
 * depending on how great the time difference between now and the given date is.
 * This, as of 2016-06-29, matches the behavior of the official Twitter app.
 */
public class TimeFormatter {
    public static String getTimeDifference(String rawJsonDate) {
        String time = "";            long secsInMin = 60;

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        format.setLenient(true);
        try {
            long secsInHour = secsInMin * 60;
            long secsInDay =  secsInHour * 24;
            long diff = (System.currentTimeMillis() - format.parse(rawJsonDate).getTime()) / 1000;
            if (diff < 5)//less than 5 seconds
                time = "Just now";
            else if (diff < secsInMin)//less than 1 minute
                time = String.format(Locale.ENGLISH, "%d seconds ago",diff);
            else if (diff >= secsInMin && diff < (secsInMin *2) - 1)//greater than or exactly 1 minute(60s) and strictly less than 2 minutes
                time = String.format(Locale.ENGLISH, "%d minute ago", diff / 60);
            else if (diff >= (secsInMin * 2) && diff < (secsInHour - 1))//greater than or exactly 2 minutes and strictly less than 60 minutes
                time = String.format(Locale.ENGLISH, "%d minutes ago", diff / 60);
            else if (diff >= (secsInHour) && diff < (secsInHour * 2) - 1)
                time = String.format(Locale.ENGLISH, "%d hour ago", diff / (secsInHour));
            else if (diff >= (secsInHour * 2) && diff < (secsInDay - 1))
                time = String.format(Locale.ENGLISH, "%d hours ago", diff / (secsInHour));
            else if (diff >= secsInDay && diff < (secsInDay * 2) - 1)
                time = String.format(Locale.ENGLISH, "%d day ago", diff / (secsInDay));
            else {
                Calendar now = Calendar.getInstance();
                Calendar then = Calendar.getInstance();
                then.setTime(format.parse(rawJsonDate));
                if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                    time = String.valueOf(then.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US ) + " " + then.get(Calendar.DAY_OF_MONTH));
                } else {
                    time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                            + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                            + " " + String.valueOf(then.get(Calendar.YEAR) - 2000);
                }
            }
        }  catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * Given a date String of the format given by the Twitter API, returns a display-formatted
     * String of the absolute date of the form "30 Jun 16".
     * This, as of 2016-06-30, matches the behavior of the official Twitter app.
     */
    public static String getTimeStamp(String rawJsonDate) {
        String time = "";
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        format.setLenient(true);
        try {
            Calendar then = Calendar.getInstance();
            then.setTime(format.parse(rawJsonDate));
            Date date = then.getTime();

            SimpleDateFormat format1 = new SimpleDateFormat("h:mm a \u00b7 dd MMM yy");

            time = format1.format(date);

        }  catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}