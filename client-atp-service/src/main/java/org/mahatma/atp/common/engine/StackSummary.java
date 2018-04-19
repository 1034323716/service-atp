package org.mahatma.atp.common.engine;


import org.mahatma.atp.common.stack.ModuleStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class StackSummary extends ModuleSummary implements ModuleStack {
    private static final Logger LOGGER = LoggerFactory.getLogger(StackSummary.class);

    private AtomicBoolean start = new AtomicBoolean(false);


    private Class<?> clazz;
    private ModuleStack stack;

    protected void build() {
        try {
            clazz = Class.forName(getClassName());

            stack = (ModuleStack) clazz.newInstance();

        } catch (ClassNotFoundException e) {
            LOGGER.error("", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("", e);
        } catch (InstantiationException e) {
            LOGGER.error("", e);
        }
    }

    @Override
    public Object getStack() {
        if (start.get()) {
            return stack;
        }
        throw new RuntimeException("stack is not start");
    }

    @Override
    public void start() {

        if (start.compareAndSet(false, true)) {
            try {
                stack.start();
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
    }

    @Override
    public void stop() {
        if (start.compareAndSet(true, false)) {
            try {
                stack.stop();
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

}
