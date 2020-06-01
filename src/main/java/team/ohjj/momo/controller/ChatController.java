package team.ohjj.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import team.ohjj.momo.domain.ChatMessage;
import team.ohjj.momo.domain.MessageType;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.ChatMessageJpaRepository;
import team.ohjj.momo.entity.ChatRoomJpaRepository;
import team.ohjj.momo.entity.ChatRoomRepository;
import team.ohjj.momo.pubsub.RedisPublisher;

import java.util.Calendar;

@RequiredArgsConstructor
@Controller
public class ChatController {
	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;

	@Autowired
	private ChatRoomJpaRepository chatRoomJpaRepository;

	@Autowired
	private ChatMessageJpaRepository chatMessageJpaRepository;

	@MessageMapping("/chat/message")
	public void message(ChatMessage message, SimpMessageHeaderAccessor messageHeaderAccessor) {
		message.setRoom(chatRoomJpaRepository.findById(message.getRoom().getId()).get());
		message.setSender((User)messageHeaderAccessor.getSessionAttributes().get("user"));
		message.setSendTime(Calendar.getInstance().getTime());

		if (!MessageType.JOIN.equals(message.getType())) {
			if (MessageType.ENTER.equals(message.getType())) {
				message.setMessage(message.getSender().getNickname() + "님이 입장하셨습니다.");
			}

			chatMessageJpaRepository.save(message);
		}
		else {
			chatRoomRepository.enterChatRoom(message.getRoom().getId());
		}

		redisPublisher.publish(chatRoomRepository.getTopic(message.getRoom().getId()), message);
	}
}