package org.uestc.weglas.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.model.ConversationChatDetail;
import reactor.core.publisher.Flux;

/**
 * @author yingxian.cyx
 * @date Created in 2024/12/10
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    /**
     * TODO
     *
     * @param conversation 历史会话
     * @param currentChat
     * @return
     */
    @Override
    public ConversationChatDetail chat(Conversation conversation, ConversationChatDetail currentChat) {
        // 发起post请求 new RestTemplate().postForEntity
        return null;
    }

    /**
     * TODO
     *
     * @param conversation
     * @param chat
     * @return
     */
    @Override
    public Flux<String> streamChat(Conversation conversation, ConversationChatDetail chat) {
        // 基于Flux实现流式读取
        return null;
    }
}
