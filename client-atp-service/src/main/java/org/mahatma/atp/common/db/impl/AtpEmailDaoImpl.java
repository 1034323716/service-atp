package org.mahatma.atp.common.db.impl;

import org.helium.database.DataRow;
import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.AtpEmail;
import org.mahatma.atp.common.db.dao.AtpEmailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyfx
 * @date 17-10-11
 */
public class AtpEmailDaoImpl implements AtpEmailDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResultDaoImpl.class);
    private Database atpDatabase;

    public AtpEmailDaoImpl(Database atpDatabase) {
        this.atpDatabase = atpDatabase;
    }

    @Override
    public List<AtpEmail> listReceive() {
        try {
            List<AtpEmail> atpEmails = new ArrayList<>();
            String sql = "SELECT emailAddress,receiveState FROM ATP_email where `receiveState` = ?";
            DataTable dataTable = atpDatabase.executeTable(sql, 1);
            for (DataRow dataRow : dataTable.getRows()) {
                String emailAddress = dataRow.getString("emailAddress");
                int receiveState = dataRow.getInt("receiveState");
                AtpEmail atpEmail = new AtpEmail();
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
