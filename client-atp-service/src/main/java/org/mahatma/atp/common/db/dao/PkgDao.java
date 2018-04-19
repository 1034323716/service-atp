package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.Pkg;

/**
 * Created by JiYunfei on 17-9-18.
 */
public interface PkgDao {
    Pkg get(Long id);

    Boolean isExisted(Long id);

    void delete(Long id);
}
