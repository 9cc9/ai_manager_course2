package org.uestc.weglas.core.callback;


import com.alibaba.fastjson2.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
import org.uestc.weglas.util.log.LogUtil;

/**
 * mqtt订阅回调
 * TODO 待完善硬件交互部分
 */
@Component
public class MqttConsumerCallBack implements MqttCallback {

    protected static final Logger logger = LogManager.getLogger(MqttConsumerCallBack.class);

    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        LogUtil.warn(logger, "client connect lost.");
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        LogUtil.info(logger, "[接收消息]:", JSON.toJSONString(message));
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LogUtil.info(logger, "client delivery success.");
    }
}
