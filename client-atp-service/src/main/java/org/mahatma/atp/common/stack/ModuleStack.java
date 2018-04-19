package org.mahatma.atp.common.stack;

import java.io.IOException;

public interface ModuleStack {

    /**
     * 启动Stack
     */
    void start() throws Exception;

    /**
     * 停止Stack
     */
    void stop() throws Exception;


    Object getStack() throws IOException;

}
