package org.mahatma.atp.common.engine;

import org.helium.database.Database;
import org.mahatma.atp.common.bean.Result;
import org.mahatma.atp.common.db.bean.TaskResultDetail;
import org.mahatma.atp.common.db.dao.TaskResultDetailDao;
import org.mahatma.atp.common.db.daoImpl.TaskResultDetailDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 用例结果管理
 */
public class ModuleReportManager {
    private Logger LOGGER = LoggerFactory.getLogger(ModuleReportManager.class);
    private AutoTestEngine engine;

    public ModuleReportManager(AutoTestEngine engine) {
        this.engine = engine;
    }

    public void input(Result result, Long taskResultDetailId) {
        Database database = engine.getResourcesManager().getDatabaseBydbPro();
        TaskResultDetailDao taskResultDetailDao = new TaskResultDetailDaoImpl(database);
        TaskResultDetail taskResultDetail = new TaskResultDetail();
        taskResultDetail.setId(taskResultDetailId);
        taskResultDetail.setCode(String.valueOf(result.getCode()));
        taskResultDetail.setDesc(result.getDesc());
        taskResultDetail.setFinishTime(new Date());
        taskResultDetail.setState(1);
        taskResultDetailDao.updateTestCase(taskResultDetail);
        LOGGER.info("record is \n{}", result.getStepDataString());
        taskResultDetail.setRecord(result.getStepDataString());
        taskResultDetailDao.updateTestCase(taskResultDetail);
    }
}
