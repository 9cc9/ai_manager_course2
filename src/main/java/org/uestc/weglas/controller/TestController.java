package org.uestc.weglas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.uestc.weglas.core.client.MqttProviderClient;

/**
 * 仅测试使用
 */
@Controller
public class TestController {

    @Autowired
    private MqttProviderClient providerClient;

    /**
     * @param text
     * @return
     */
    @RequestMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(String text) {

        providerClient.publish(text);
        return "发送成功";
    }
}
