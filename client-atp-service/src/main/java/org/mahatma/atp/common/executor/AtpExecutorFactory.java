package org.mahatma.atp.common.executor;

import com.feinno.threading.ExecutorFactory;
import com.feinno.threading.FixedObservableExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lyfx
 * @date 17-10-12
 */
public class AtpExecutorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorFactory.class);

    private static Map<String, Executor> executors = new Hashtable<>();

    static {
        int cupNumber = Runtime.getRuntime().availableProcessors();
        newFixedExecutor("TestCase", cupNumber * 2, cupNumber * 2 * 10);
    }

    /**
     * 新增一个固定大小的线程池
     *
     * @param name
     * @param size  固定线程数
     * @param limit 最大队列长度
     * @return
     */
    public synchronized static Executor newFixedExecutor(final String name, int size, int limit) {
        if (executors.get(name) == null) {
            Executor innerExecutor = new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(), new ThreadFactory() {
                private int count = 0;

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(false);
                    t.setName(name + "-" + count);
                    count++;
                    return t;
                }
            });
            FixedObservableExecutor executor = new FixedObservableExecutor(name, innerExecutor, limit, size);
            executors.put(name, executor);
            LOGGER.info("Create FixedExecutor:" + name + " size = {} limit = {}", size, limit);
        }
        return getExecutor(name);
    }

    /**
     * 通过名字获取一个已经创建的线程池
     *
     * @param name
     * @return
     */
    public static Executor getExecutor(String name) {
        return executors.get(name);
    }

    public static Executor getDefaultExecutor(){
        return executors.get("TestCase");
    }

}
