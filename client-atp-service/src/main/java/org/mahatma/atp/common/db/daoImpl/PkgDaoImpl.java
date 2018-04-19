package org.mahatma.atp.common.db.daoImpl;

import org.helium.database.DataRow;
import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.Pkg;
import org.mahatma.atp.common.db.dao.PkgDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by JiYunfei on 17-10-10.
 */
public class PkgDaoImpl implements PkgDao {

    private static final Logger logger = LoggerFactory.getLogger(PkgDaoImpl.class);

    private Database database;

    public PkgDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public Pkg get(Long id) {
        try {
            String sql = "SELECT * FROM ATP_pkg where `id` = ?";

            DataTable dt = database.executeTable(sql, id);

            if (dt != null && dt.getRowCount() == 1) {
                DataRow dataRow = dt.getRow(0);
                Pkg pkg = new Pkg();
                pkg.setId(dataRow.getLong("id"));
                pkg.setName(dataRow.getString("name"));
                pkg.setDesc(dataRow.getString("desc"));
                pkg.setCreateTime(dataRow.getTimestamp("createTime").toString());
                pkg.setType(dataRow.getString("type"));
                pkg.setUserId(dataRow.getLong("userId"));
                pkg.setVersion(dataRow.getString("version"));
                return pkg;
            }
        } catch (Exception ex) {
            logger.error("get pkg is error" + ex.getMessage());
        }
        return null;
    }

    @Override
    public Boolean isExisted(Long id) {
        String sql = "select count(*) from ATP_pkg where `id` = ?";
        int i = 0;
        try {
            DataTable dataTable = database.executeTable(sql, id);
            if (dataTable != null) {
                if (dataTable.getRows().size() > 0) {
                    i = dataTable.getRows().get(0).getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("pkgId isExisted error!", e);
        }
        return i != 0;
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from ATP_pkg where `id` = ?";
        try {
            database.executeUpdate(sql, id);
        } catch (SQLException e) {
            logger.error("pkgId : " + id + ",delete error!", e);
        }
    }
}
