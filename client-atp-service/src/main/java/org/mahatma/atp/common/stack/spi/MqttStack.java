package org.mahatma.atp.common.stack.spi;

import org.fusesource.mqtt.client.MQTT;
import org.mahatma.atp.common.annotations.TestStack;
import org.mahatma.atp.common.stack.ModuleStack;
import org.mahatma.atp.common.type.StackTypec;

@TestStack(id = StackTypec.MQTT)
public class MqttStack implements ModuleStack {

    private MQTT mqtt;

    @Override
    public void start() throws Exception {
//        mqtt = new MQTT();

//        设置实例
//        mqtt.setHost("tcp://" + mqtt_proxy_host);
//        // 用于设置客户端会话的ID。在setCleanSession(false);被调用时，MQTT服务器利用该ID获得相应的会话。此ID应少于23个字符，默认根据本机地址、端口和时间自动生成
//        mqtt.setClientId(epid);
//        // 若设为false，MQTT服务器将持久化客户端会话的主体订阅和ACK位置，默认为true
//        mqtt.setCleanSession(true);
//        // 服务器认证用户名，此处规定用UserId
//        mqtt.setUserName(String.valueOf(userId));
//        // 服务器认证密码
//        mqtt.setPassword(credential);
//
//        // trace.error(usr.getReg2Credential().get(AuthName.Credential.toString()));
//        mqtt.setVersion("3.1.1");
//        // 客户端首次连接到服务器时，连接的最大重试次数，超出该次数客户端将返回错误。-1意为无重试上限，默认为-1
//        mqtt.setConnectAttemptsMax(10L);
//        // 首次重连接间隔毫秒数，默认为10ms
//        mqtt.setReconnectDelay(1000L);
//        mqtt.setKeepAlive((short) mqttKeepaliveInterval);// 保活时间设置为在线时间吧，这个地服务端逻辑有问题
        // 若没有调用方法setDispatchQueue，客户端将为连接新建一个队列。如果想实现多个连接使用公用的队列，显式地指定队列是一个非常方便的实现方法
//        mqtt.setDispatchQueue(Dispatch.createQueue("foo"));
        // 定义客户端传来消息的最大时间间隔秒数，服务器可以据此判断与客户端的连接是否已经断开，从而避免TCP/IP超时的长时间等待
        // mqtt.setKeepAlive((short) 60);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息
        // mqtt.setWillTopic("willTopic");
        // 设置“遗嘱”消息的内容，默认是长度为零的消息
        // mqtt.setWillMessage("willMessage");
        // 设置“遗嘱”消息的QoS，默认为QoS.ATMOSTONCE
        // mqtt.setWillQos(QoS.AT_LEAST_ONCE);
        // 若想要在发布“遗嘱”消息时拥有retain选项，则为true
        // mqtt.setWillRetain(true);
        // 客户端已经连接到服务器，但因某种原因连接断开时的最大重试次数，超出该次数客户端将返回错误。-1意为无重试上限，默认为-1
        // mqtt.setReconnectAttemptsMax(3L);
        // 重连接间隔毫秒数，默认为30000ms
        // mqtt.setReconnectDelayMax(30000L);
        // 设置重连接指数回归。设置为1则停用指数回归，默认为2
        // mqtt.setReconnectBackOffMultiplier(2);
        // Socket设置说明
        // 设置socket接收缓冲区大小，默认为65536（64k）
        // mqtt.setReceiveBufferSize(65536);
        // 设置socket发送缓冲区大小，默认为65536（64k）
        // mqtt.setSendBufferSize(65536);
        // 设置发送数据包头的流量类型或服务类型字段，默认为8，意为吞吐量最大化传输
        // mqtt.setTrafficClass(8);
        // 带宽限制设置说明
        // 设置连接的最大接收速率，单位为bytes/s。默认为0，即无限制
        // mqtt.setMaxReadRate(0);
        // 设置连接的最大发送速率，单位为bytes/s。默认为0，即无限制
        // mqtt.setMaxWriteRate(0);

    }

    @Override
    public void stop() throws Exception {
        // TODO: 17-10-13
    }

    @Override
    public MQTT getStack() {
        mqtt = new MQTT();
        return mqtt;
    }
}
