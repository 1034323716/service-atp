package org.mahatma.atp.common.db.impl;

import org.helium.database.DataRow;
import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.Tc;
import org.mahatma.atp.common.db.dao.TcDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiYunfei on 17-10-10.
 */
public class TcDaoImpl implements TcDao {
    private static final Logger logger = LoggerFactory.getLogger(TcDaoImpl.class);

    private Database database;

    public TcDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public boolean insert(Tc tc) {
        try {
            String insertSql = "INSERT INTO ATP_tc(`pkgId`,`name`, `classPath`,`desc`,`nickname`) VALUES(?, ?, ?, ?, ?)";
            database.executeInsert(insertSql,
                    tc.getPkgId(),
                    tc.getName(),
                    tc.getClassPath(),
                    tc.getDesc(),
                    tc.getNickname());
            return true;
        } catch (Exception ex) {
            logger.error("insert tc error" + ex.getMessage());
            return false;
        }
    }

    @Override
    public Tc get(Long id) {
        try {
            String sql = "SELECT * FROM ATP_tc where `id` = ?";

            DataTable dt = database.executeTable(sql, id);

            if (dt != null && dt.getRowCount() == 1) {
                DataRow dataRow = dt.getRow(0);
                Tc tc = new Tc();
                tc.setId(dataRow.getLong("id"));
                tc.setName(dataRow.getString("name"));
                tc.setDesc(dataRow.getString("desc"));
                tc.setPkgId(dataRow.getLong("pkgId"));
                tc.setClassPath(dataRow.getString("classPath"));
                tc.setNickname(dataRow.getString("nickname"));
                return tc;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error("get tc error" + ex.getMessage());
            return null;
        }
    }

    @Override
    public void deleteByPkgId(Long pkgId) {
        String sql = "delete from ATP_tc where `pkgId` = ?";
        try {
            database.executeUpdate(sql, pkgId);
        } catch (SQLException e) {
            logger.error("pkgId : " + pkgId + ",delete tc error!", e);
        }
    }

    @Override
    public List<Long> listByPkgId(Long pkgId) {
        List<Long> ids = new ArrayList<>();
        String sql = "select * from ATP_tc where `pkgId` = ?";
        try {
            DataTable dataTable = database.executeTable(sql, pkgId);
            for (DataRow dataRow : dataTable.getRows()) {
                Long id = dataRow.getLong("id");
                ids.add(id);
            }
        } catch (SQLException e) {
            logger.error("pkgId : " + pkgId + ",delete tc error!", e);
        }
        return ids;
    }
}
