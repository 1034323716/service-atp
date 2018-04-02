package org.mahatma.atp.conf;

import org.helium.framework.annotations.ServiceInterface;

/**
 * 单纯的提供一个初始化服务.
 */
@ServiceInterface(id = InitSerice.BEAN_ID)
public interface InitSerice {
    String BEAN_ID = "atp:InitSerice";
}
