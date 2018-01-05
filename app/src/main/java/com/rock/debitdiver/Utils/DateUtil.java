package com.rock.debitdiver.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by michalejackson on 1/4/18.
 */

public class DateUtil {
    public static String stringTP_to_stringDATE(String date){
        Long lDate = Long.parseLong(date);
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(tz);

        String time = sdf.format(new Date(lDate * 1000));
        return time;
    }
}
