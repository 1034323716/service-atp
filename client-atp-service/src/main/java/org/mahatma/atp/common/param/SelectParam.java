package org.mahatma.atp.common.param;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author JiYunfei
 * @date 17-10-11
 */
public class SelectParam {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectParam.class);
    /**
     * 分配与装载启动参数
     *
     * @param args
     */
    public void assignOpts(String[] args) {
        LOGGER.debug("Read startup option : {}", Arrays.toString(args));
        StartupOptionEnum currentOption = null;
        for (int i = 0; i < args.length; i++) {
            String argsStr = args[i];
            if (argsStr.startsWith("-")) {
                // 如果当前是一个启动项，那么从启动项的枚举列表中找到当前的项，设置为开启
                String optionName = argsStr.substring(1, argsStr.length()).toUpperCase();
                try {
                    currentOption = StartupOptionEnum.valueOf(optionName.trim());
                    if (currentOption == null) {
                        throw new RuntimeException(String.format("Startup options %s Error.", argsStr));
                    }
                    currentOption.setEnable(true);
                } catch (Exception e) {
                    LOGGER.warn(String.format("Not found startup option [%s] . So ignore.", optionName.trim()), e);
                }
            } else {
                // 如果是一个启动参数，且当前的启动项为空，那么需要报告错误，该参数找不到对应的启动项
                if (currentOption == null) {
                    throw new RuntimeException(String.format("Startup options %s Error.", argsStr));
                }
                currentOption.addArgs(argsStr);
            }
        }
    }
}
