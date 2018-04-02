package org.mahatma.atp.dao;

import org.helium.framework.annotations.ServiceInterface;

/**
 * Created by lyfx on 17-10-11.
 */
@ServiceInterface(id = "atp:TaskStore")
public interface TaskStore {
    boolean isExisted(long taskId);
}
