package org.mahatma.atp.common.exception;

/**
 * 自动化测试运行时异常公共类
 * Created by lyfx on 17-10-11.
 */
public class AutoTestRuntimeException extends Exception {
    public AutoTestRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutoTestRuntimeException(String message) {
        super(message);
    }

    public AutoTestRuntimeException(String message, Exception ex) {
        super(message, ex);
    }
}
