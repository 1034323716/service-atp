package org.mahatma.atp;

import org.mahatma.atp.common.engine.AutoTestEngine;
import org.mahatma.atp.common.exception.AutoTestRuntimeException;
import org.mahatma.atp.common.param.SelectParam;
import org.mahatma.atp.plugin.ResourcesManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtpAppBootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtpAppBootstrap.class);
    static AutoTestEngine INSTANCE = AutoTestEngine.INSTANCE;

    /**
     * 服务端运行入口
     *
     * @param args
     */
    public static void main(String[] args) {
        SelectParam selectParam = new SelectParam();
        selectParam.assignOpts(args);
        INSTANCE.addPropertiesPath("config/db/URCS_ATPDB.properties");
        INSTANCE.setResourcesManager(new ResourcesManagerImpl());
        try {
            INSTANCE.runTask();
        } catch (AutoTestRuntimeException e) {
            LOGGER.error(e.getMessage());
        }
        System.exit(0);
    }
}
