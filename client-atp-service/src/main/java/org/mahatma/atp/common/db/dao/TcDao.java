package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.Tc;

import java.util.List;

/**
 * Created by JiYunfei on 17-9-8.
 */
public interface TcDao {
    boolean insert(Tc tc);

    Tc get(Long id);

    void deleteByPkgId(Long pkgId);

    List<Long> listByPkgId(Long pkgId);

}
