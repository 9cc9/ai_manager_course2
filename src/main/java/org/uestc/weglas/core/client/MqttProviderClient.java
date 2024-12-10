package org.uestc.weglas.core.client;


import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.uestc.weglas.core.callback.MqttProviderCallBack;
import org.uestc.weglas.core.model.BizConstants;
import org.uestc.weglas.util.log.LogUtil;

import javax.annotation.PostConstruct;

import static org.uestc.weglas.core.model.BizConstants.MQTT_RETAINED;


/**
 * mqtt发布端配置类
 */
@Configuration
@Slf4j
public class MqttProviderClient extends MqttBaseClient {

    private static final Logger logger = LogManager.getLogger(MqttProviderClient.class);

    @Value("${spring.mqtt.url}")
    private String hostUrl;
    @Value("${spring.mqtt.client.provider.id}")
    private String clientId;
    @Value("${spring.mqtt.client.provider.topic}")
    private String topic;

    @Autowired
    private MqttProviderCallBack mqttProviderCallBack;

    /**
     * 在bean初始化后连接到服务器
     */
    @PostConstruct
    public void init() {
        try {
            //创建MQTT客户端对象
            client = new MqttClient(hostUrl, clientId, new MemoryPersistence());
            connect(clientId, mqttProviderCallBack);
            subscribe(topic);
        } catch (MqttException e) {
            LogUtil.error(logger, e, "client init failed.");
        }
    }

    public void publish(String message) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(BizConstants.MQTT_QOS);
        mqttMessage.setRetained(MQTT_RETAINED);
        mqttMessage.setPayload(message.getBytes());
        //主题的目的地，用于发布/订阅信息
        MqttTopic mqttTopic = client.getTopic(topic);
        //提供一种机制来跟踪消息的传递进度
        //用于在以非阻塞方式（在后台运行）执行发布是跟踪消息的传递进度
        MqttDeliveryToken token;
        try {
            //将指定消息发布到主题，但不等待消息传递完成，返回的token可用于跟踪消息的传递状态
            //一旦此方法干净地返回，消息就已被客户端接受发布，当连接可用，将在后台完成消息传递。
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            LogUtil.error(logger, e, "client publish failed.");
        }
    }
}
