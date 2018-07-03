package org.mahatma.atp.task;

import org.helium.framework.annotations.ScheduledTaskImplementation;
import org.helium.framework.annotations.ServiceSetter;
import org.helium.framework.task.ScheduledTask;
import org.mahatma.atp.common.util.entity.ControlPkg;
import org.mahatma.atp.service.ControlTest;
import org.mahatma.atp.service.impl.ControlTestImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * 定时扫描正在执行的测试任务，若执行时间过长就将其杀死
 *
 * @author JiYunfei
 * @date 18-7-3
 */
@ScheduledTaskImplementation(id = "atp:MonitoringClientTask", cronExpression = "0 0 0/1 * * ? ")
public class MonitoringClientTask implements ScheduledTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringClientTask.class);

    @ServiceSetter
    private ControlTest controlTest;

    @Override
    public void processTask(Object lock) {
        Iterator<Map.Entry<Long, ControlPkg>> iterator = ControlTestImpl.getRunProcessMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, ControlPkg> next = iterator.next();
            long operationTime = next.getValue().getOperationTime();
            long currentTimeMillis = System.currentTimeMillis();
            if ((currentTimeMillis - operationTime) > (60 * 60 * 1000)) {
                Long taskResultId = next.getKey();
                LOGGER.error("runing time too long, stop taskResultId:{}", taskResultId);
                controlTest.stop(taskResultId);
            }
        }
    }

}