package org.mahatma.atp.common.db.bean;

/**
 * Created by JiYunfei on 18-2-7.
 */
public class AtpEmail {
    private String emailAddress;
    private int receiveState;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(int receiveState) {
        this.receiveState = receiveState;
    }
}
