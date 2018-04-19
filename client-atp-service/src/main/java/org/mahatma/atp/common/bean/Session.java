package org.mahatma.atp.common.bean;

import org.mahatma.atp.common.engine.StackManager;
import org.mahatma.atp.common.engine.spi.RuntimeWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Session {
    private static Logger LOGGER = LoggerFactory.getLogger(Session.class);

    private StackManager stackManager;

    private RuntimeWatch watch;

    public void setStackManager(StackManager stackManager) {
        this.stackManager = stackManager;
    }

    public void setRunWatch(RuntimeWatch watch) {
        this.watch = watch;
    }

    public <T> T getStack(Class<T> tClass) {
        return (T) stackManager.getStack(tClass);
    }


    public Result createResult() {
        return new Result(watch);
    }

}
