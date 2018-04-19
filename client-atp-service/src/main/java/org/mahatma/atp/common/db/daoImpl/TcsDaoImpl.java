package org.mahatma.atp.common.db.daoImpl;

import org.helium.database.DataRow;
import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.Tcs;
import org.mahatma.atp.common.db.dao.TcsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by JiYunfei on 17-10-10.
 */
public class TcsDaoImpl implements TcsDao {
    private static final Logger logger = LoggerFactory.getLogger(TcsDaoImpl.class);

    private Database database;

    public TcsDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public Tcs get(Long id) {
        try {
            String sql = "SELECT * FROM ATP_tcs where `id` = ?";

            DataTable dt = database.executeTable(sql, id);

            if (dt != null && dt.getRowCount() == 1) {
                DataRow dataRow = dt.getRow(0);
                Tcs tcs = new Tcs();
                tcs.setId(dataRow.getLong("id"));
                tcs.setName(dataRow.getString("name"));
                tcs.setDesc(dataRow.getString("desc"));
                tcs.setCreateTime(dataRow.getDateTime("createTime"));
                tcs.setSummary(dataRow.getString("summary"));
                tcs.setUserId(dataRow.getInt("userId"));
                return tcs;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error("get tcs error" + ex.getMessage());
            return null;
        }
    }
}
