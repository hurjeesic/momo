package team.ohjj.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import team.ohjj.momo.domain.ChatMessage;
import team.ohjj.momo.domain.MessageType;
import team.ohjj.momo.entity.ChatMessageJpaRepository;
import team.ohjj.momo.entity.ChatRoomJpaRepository;
import team.ohjj.momo.entity.ChatRoomRepository;
import team.ohjj.momo.entity.UserJpaRepository;
import team.ohjj.momo.pubsub.RedisPublisher;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;

    @Autowired
    private ChatMessageJpaRepository chatMessageJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        message.setNo((int)chatMessageJpaRepository.count() + 1);
        message.setRoom(chatRoomJpaRepository.findById(message.getRoom().getId()).get());
        message.setSender(userJpaRepository.findById(message.getSender().getNo()).get());

        if (MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoom().getId());
            message.setMessage(message.getSender().getNickname() + "님이 입장하셨습니다.");
        }

        chatMessageJpaRepository.save(message);
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoom().getId()), message);
    }
}
