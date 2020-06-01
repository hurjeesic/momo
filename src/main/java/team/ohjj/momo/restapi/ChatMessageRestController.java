package team.ohjj.momo.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.ohjj.momo.domain.ChatMessage;
import team.ohjj.momo.domain.MessageType;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.ChatMessageJpaRepository;
import team.ohjj.momo.entity.ChatRoomJpaRepository;
import team.ohjj.momo.entity.ChatRoomRepository;
import team.ohjj.momo.entity.UserJpaRepository;
import team.ohjj.momo.pubsub.RedisPublisher;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat/message")
public class ChatMessageRestController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;

    @Autowired
    private ChatMessageJpaRepository chatMessageJpaRepository;

    @GetMapping("/list/{roomId}")
    public List<ChatMessage> getMessageListByRoom(@PathVariable String roomId) {
        return chatMessageJpaRepository.findByRoom(chatRoomJpaRepository.findById(roomId).get());
    }
}
