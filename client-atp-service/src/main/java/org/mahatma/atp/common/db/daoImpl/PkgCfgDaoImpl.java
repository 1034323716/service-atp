package org.mahatma.atp.common.db.daoImpl;

import org.helium.database.DataRow;
import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.PkgCfg;
import org.mahatma.atp.common.db.dao.PkgCfgDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by JiYunfei on 17-10-10.
 */
public class PkgCfgDaoImpl implements PkgCfgDao {
    private static final Logger logger = LoggerFactory.getLogger(PkgCfgDaoImpl.class);

    private Database database;

    public PkgCfgDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public PkgCfg get(Long id) {
        try {
            String sql = "SELECT * FROM ATP_pkgCfg where `id` = ?";

            DataTable dt = database.executeTable(sql, id);

            if (dt != null && dt.getRowCount() == 1) {
                PkgCfg pkgCfg = new PkgCfg();
                List<DataRow> rows = dt.getRows();
                DataRow dataRow = rows.get(0);
                pkgCfg.setId(dataRow.getLong("id"));
                pkgCfg.setName(dataRow.getString("name"));
                pkgCfg.setProperties(dataRow.getString("properties"));
                pkgCfg.setPkgId(dataRow.getLong("pkgId"));
                pkgCfg.setPkgCfgDefault(dataRow.getBoolean("isDefault"));
                return pkgCfg;
            }
        } catch (Exception ex) {
            logger.error("get pkgCfg exception" + ex.getMessage());
        }
        return null;
    }

    @Override
    public void deleteByPkgId(Long pkgId) {
        String sql = "delete from ATP_pkgCfg where `pkgId` = ?";
        try {
            database.executeUpdate(sql, pkgId);
        } catch (SQLException e) {
            logger.error("pkgId : " + pkgId + ",delete pkgCfg error!", e);
        }
    }
}
