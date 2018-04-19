package org.mahatma.atp.common.type;

/**
 * 内置的一些协议栈
 */
public enum StackType {

    http(StackTypec.HTTP), SIP(StackTypec.SIP), MSRP(StackTypec.MSRP), RTP(StackTypec.RTP), MQTT(StackTypec.MQTT);

    String name;

    StackType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
