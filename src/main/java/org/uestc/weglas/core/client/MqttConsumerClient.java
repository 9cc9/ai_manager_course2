package org.uestc.weglas.core.client;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.uestc.weglas.core.callback.MqttConsumerCallBack;
import org.uestc.weglas.util.log.LogUtil;

import javax.annotation.PostConstruct;

/**
 * mqtt订阅客户端
 */
@Configuration
public class MqttConsumerClient extends MqttBaseClient {

    @Value("${spring.mqtt.url}")
    private String hostUrl;
    @Value("${spring.mqtt.client.consumer.id}")
    private String clientId;
    @Value("${spring.mqtt.client.consumer.topic}")
    private String topic;

    @Autowired
    private MqttConsumerCallBack mqttConsumerCallBack;

    /**
     * 在bean初始化后连接到服务器
     */
    @PostConstruct
    public void init() {
        try {
            //创建MQTT客户端对象
            client = new MqttClient(hostUrl, clientId, new MemoryPersistence());
            connect(clientId, mqttConsumerCallBack);
            subscribe(topic);
        } catch (MqttException e) {
            LogUtil.error(logger, e, "client init failed.");
        }
    }

}
