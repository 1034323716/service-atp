package org.mahatma.atp.conf.impl;

import org.helium.framework.BeanContext;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.helium.framework.configuration.ConfigProvider;
import org.helium.framework.tag.Initializer;
import org.mahatma.atp.conf.AtpEnvConfiguration;
import org.mahatma.atp.conf.AtpResultCodeManager;
import org.mahatma.atp.conf.InitSerice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServiceImplementation
public class InitServiceImpl implements InitSerice {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitServiceImpl.class);

    @FieldSetter("${ATP-ENV}")
    private String atpEnvPath;
    @FieldSetter("${RESULT-CODE}")
    private String resultCode;

    @Initializer
    public void initAllTheWorld() {
        ConfigProvider provider = BeanContext.getContextService().getService(ConfigProvider.class);
        LOGGER.info("init AtpEnvConfiguration");
        AtpEnvConfiguration envConfiguration = provider.loadXml(atpEnvPath, AtpEnvConfiguration.class);
        AtpEnvConfiguration.initAtpConfiguration(envConfiguration);
        LOGGER.info("init AtpResultCodeManager");
        AtpResultCodeManager atpResultCodeManager = provider.loadXml(resultCode, AtpResultCodeManager.class);
        AtpResultCodeManager.initResultCodeManager(atpResultCodeManager);
    }
}
