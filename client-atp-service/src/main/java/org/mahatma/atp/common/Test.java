package org.mahatma.atp.common;

import org.mahatma.atp.common.bean.Result;
import org.mahatma.atp.common.bean.Session;

/**
 * 测试实现类入口,一切测试实例派生于此
 *
 * @author JiYunfei
 */
public interface Test {
    /**
     * 测试用例入口函数
     *
     * @param session
     * @return
     * @throws Exception
     */
    @org.mahatma.atp.common.annotations.Test()
    Result process(Session session) throws Exception;
}
