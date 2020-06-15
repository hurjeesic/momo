package team.ohjj.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import team.ohjj.momo.domain.*;
import team.ohjj.momo.entity.ChatMessageJpaRepository;
import team.ohjj.momo.entity.ChatRoomJpaRepository;
import team.ohjj.momo.entity.ChatRoomRepository;
import team.ohjj.momo.entity.MemberJpaRepository;
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

	@Autowired
	private MemberJpaRepository memberJpaRepository;

	@MessageMapping("/chat/message")
	public void message(ChatMessage message, SimpMessageHeaderAccessor messageHeaderAccessor) {
		User user = (User)messageHeaderAccessor.getSessionAttributes().get("user");

		message.setRoom(chatRoomJpaRepository.findById(message.getRoom().getId()).get());
		message.setSender((User)messageHeaderAccessor.getSessionAttributes().get("user"));
		message.setSendTime(Calendar.getInstance().getTime());

		if (!MessageType.JOIN.equals(message.getType())) {
			if (MessageType.COMPLETE.equals(message.getType())) {
				Project project = chatRoomJpaRepository.findById(message.getRoom().getId()).get().getProject();
				Member member = memberJpaRepository.findByProjectAndUser(project, user).get();
				member.setComplete(message.getMessage() != null);
				memberJpaRepository.save(member);
			}
			else {
				if (MessageType.ENTER.equals(message.getType())) {
					message.setMessage(message.getSender().getNickname() + "님이 입장하셨습니다.");
				}

				chatMessageJpaRepository.save(message);
			}
		}
		else {
			chatRoomRepository.enterChatRoom(message.getRoom().getId());
		}

		redisPublisher.publish(chatRoomRepository.getTopic(message.getRoom().getId()), message);
	}
}