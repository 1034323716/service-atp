package org.mahatma.atp.common.engine.spi;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JiYunfei
 * @date 18-7-20
 */
public class AtpThreadFactory implements ThreadFactory {
    private AtomicInteger count = new AtomicInteger(0);
    private String threadName;

    public AtpThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, threadName + "-" + count.incrementAndGet());
    }
}
