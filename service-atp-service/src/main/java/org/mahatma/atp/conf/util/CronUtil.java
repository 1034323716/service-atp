package org.mahatma.atp.conf.util;

import org.helium.framework.spi.task.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by JiYunfei on 17-11-7.
 */
public class CronUtil {
    public static boolean isValidExpression(String cron) throws ParseException {
        CronExpression cronExpression = new CronExpression(cron);
        Date after = cronExpression.getTimeAfter(new Date());
        return after != null && after.after(new Date());
    }

    public static String cronSummary(String cron) throws ParseException {
        CronExpression cronExpression = new CronExpression(cron);
        return cronExpression.getExpressionSummary();
    }
}
