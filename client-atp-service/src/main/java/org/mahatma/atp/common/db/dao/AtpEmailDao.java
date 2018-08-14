package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.AtpEmail;

import java.util.List;

public interface AtpEmailDao {
    List<AtpEmail> listReceive();
}
