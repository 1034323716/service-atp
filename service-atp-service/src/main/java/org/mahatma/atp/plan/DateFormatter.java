package org.mahatma.atp.plan;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lyfx on 17-9-11.
 */

public class DateFormatter {

    public static String scheduleFormat = "yyyy-MM-dd HH:mm:ss";

    public static String getScheduleDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat(scheduleFormat);
        return format.format(date);
    }

    public static void main(String[] args) {
        System.out.println(DateFormatter.getScheduleDate(new Date()));
    }

}
