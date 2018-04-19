package org.mahatma.atp.common.util;

import org.mahatma.atp.common.engine.AutoTestEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lyfx on 17-10-12.
 */
public class AtpFormatUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTestEngine.class);

    public static void printListening(String protocol,String host,int port){
        LOGGER.warn(">>> listening: " + protocol + "://" + host + ":" + port);
    }
}
