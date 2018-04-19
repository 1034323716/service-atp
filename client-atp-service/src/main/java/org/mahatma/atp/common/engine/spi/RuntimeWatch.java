package org.mahatma.atp.common.engine.spi;

import com.feinno.util.Combo3;

public interface RuntimeWatch {
    void into(Combo3<Integer, byte[], byte[]> combo3);
}
