package org.mahatma.atp.common.db.daoImpl;

import org.helium.database.DataRow;
import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.ATP_email;
import org.mahatma.atp.common.db.dao.ATP_emailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyfx on 17-10-11.
 */
public class ATP_emailDaoImpl implements ATP_emailDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResultDaoImpl.class);
    private Database atpDatabase;

    public ATP_emailDaoImpl(Database atpDatabase) {
        this.atpDatabase = atpDatabase;
    }

    @Override
    public List<ATP_email> listReceive() {
        try {
            List<ATP_email> atpEmails = new ArrayList<>();
            String sql = "SELECT emailAddress,receiveState FROM ATP_email where `receiveState` = ?";
            DataTable dataTable = atpDatabase.executeTable(sql, 1);
            for (DataRow dataRow : dataTable.getRows()) {
                String emailAddress = dataRow.getString("emailAddress");
                int receiveState = dataRow.getInt("receiveState");
                ATP_email atpEmail = new ATP_email();
                atpEmail.setEmailAddress(emailAddress);
                atpEmail.setReceiveState(receiveState);
                atpEmails.add(atpEmail);
            }
            LOGGER.info("list success");
            return atpEmails;
        } catch (Exception ex) {
            LOGGER.error("list receive exception" + ex.getMessage());
        }
        return null;
    }
}
