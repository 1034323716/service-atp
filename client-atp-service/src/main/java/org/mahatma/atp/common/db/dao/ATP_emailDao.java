package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.ATP_email;

import java.util.List;

public interface ATP_emailDao {
    List<ATP_email> listReceive();
}
