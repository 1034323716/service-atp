package org.mahatma.atp.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author jiyunfei
 * @date 18-9-14
 */
public class DateUtil {
    /**
     * 获取当前时间的前几天或者后几天的0点
     *
     * @param day
     * @return
     */
    public static Date getDateByday(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        System.out.println(getDateByday(-1));
    }
}
