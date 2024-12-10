package org.uestc.weglas.core.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.uestc.weglas.util.log.LogUtil;

import javax.annotation.PreDestroy;

import static org.uestc.weglas.core.model.BizConstants.*;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/17
 */
public abstract class MqttBaseClient {

    protected static final Logger logger = LogManager.getLogger(MqttBaseClient.class);


    @Value("${spring.mqtt.username}")
    private String username;
    @Value("${spring.mqtt.password}")
    private String password;

    /**
     * 客户端对象
     */
    protected MqttClient client;

    /**
     * 客户端连接服务端
     */
    public void connect(String clientId, MqttCallback callback) throws MqttException {
        //连接设置
        MqttConnectOptions options = new MqttConnectOptions();
        //是否清空session，设置为false表示服务器会保留客户端的连接记录，客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
        //设置为true表示每次连接到服务端都是以新的身份
        options.setCleanSession(true);
        //设置连接用户名
        options.setUserName(username);
        //设置连接密码
        options.setPassword(password.toCharArray());
        //设置超时时间，单位为秒
        options.setConnectionTimeout(MQTT_CONNECT_TIMEOUT);
        //设置心跳时间 单位为秒，表示服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
        options.setKeepAliveInterval(MQTT_KEEP_ALIVE);
        //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
        options.setWill("willTopic", (clientId + "与服务器断开连接").getBytes(), MQTT_QOS, MQTT_RETAINED);
        //设置回调
        client.setCallback(callback);
        client.connect(options);
    }

    /**
     * 客户端连接服务端
     */
    public void subscribe(String topic) throws MqttException {
        //订阅主题
        //消息等级，和主题数组一一对应，服务端将按照指定等级给订阅了主题的客户端推送消息
        //订阅主题
        client.subscribe(new String[]{topic}, new int[]{MQTT_QOS});
    }

    /**
     * 断开连接
     */
    @PreDestroy
    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            LogUtil.error(logger, e, "client disconnect failed.");
        }
    }
}
