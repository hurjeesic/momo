package team.ohjj.momo.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;
import team.ohjj.momo.domain.ChatRoom;
import team.ohjj.momo.pubsub.RedisSubscriber;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private static final String CHAT_ROOMS ="CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private Map<String, ChannelTopic> topics;

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();

        for (ChatRoom chatRoom: chatRoomJpaRepository.findAll()) {
            opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getId(), chatRoom);
        }
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    public ChatRoom createChatRoom(String name, Integer projectId) {
        ChatRoom chatRoom = ChatRoom.create(name, projectJpaRepository.findById(projectId).get());

        chatRoomJpaRepository.save(chatRoom);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getId(), chatRoom);

        return chatRoom;
    }

    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);

        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    public void leaveChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);

        if (topic != null) {
            redisMessageListenerContainer.removeMessageListener(redisSubscriber, topic);
            topics.remove(roomId);
        }
    }
}