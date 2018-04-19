package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.PkgCfg;

/**
 * Created by JiYunfei on 17-9-18.
 */
public interface PkgCfgDao {
    PkgCfg get(Long id);

    void deleteByPkgId(Long pkgId);
}
