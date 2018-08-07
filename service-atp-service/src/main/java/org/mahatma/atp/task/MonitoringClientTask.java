package org.mahatma.atp.task;

import org.helium.framework.annotations.ScheduledTaskImplementation;
import org.helium.framework.annotations.ServiceSetter;
import org.helium.framework.task.ScheduledTask;
import org.mahatma.atp.common.alarm.wechat.WeChatAlarm;
import org.mahatma.atp.common.util.entity.ControlPkg;
import org.mahatma.atp.service.ControlTest;
import org.mahatma.atp.service.impl.ControlTestImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * 定时扫描正在执行的测试任务，若执行时间过长就将其杀死
 *
 * 半个小时一次，每次扫描运行时间超过40分钟的
 *
 * @author JiYunfei
 * @date 18-7-3
 */
//@ScheduledTaskImplementation(id = "atp:MonitoringClientTask", cronExpression = "0 0 0/1 * * ? ")
@ScheduledTaskImplementation(id = "atp:MonitoringClientTask", cronExpression = "0 0/30 * * * ? ")
public class MonitoringClientTask implements ScheduledTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringClientTask.class);

    @ServiceSetter
    private ControlTest controlTest;

    @Override
    public void processTask(Object lock) {
        Iterator<Map.Entry<Long, ControlPkg>> iterator = ControlTestImpl.getRunProcessMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, ControlPkg> next = iterator.next();
            ControlPkg controlPkg = next.getValue();
            long operationTime = controlPkg.getOperationTime();
            long currentTimeMillis = System.currentTimeMillis();
            if ((currentTimeMillis - operationTime) > (40 * 60 * 1000)) {
                Long taskResultId = next.getKey();
                LOGGER.error("runing time too long, stop taskResultId:{}", taskResultId);
                try {
                    String content = "测试任务运行时间过长，强行停止该任务。taskResultId：" + taskResultId +
                            "任务信息：" + controlPkg.toJsonObject().toString();
                    WeChatAlarm.send(content);
                } catch (IOException e) {
                    LOGGER.error("MonitoringClientTask WeChatAlarm.send error", e);
                }
                controlTest.stop(taskResultId);
            }
        }
    }

}