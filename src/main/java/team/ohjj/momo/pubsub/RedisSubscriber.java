package team.ohjj.momo.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import team.ohjj.momo.domain.ChatMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            messageSendingOperations.convertAndSend("/sub/chat/room/" + roomMessage.getRoom().getId(), roomMessage);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}