package team.ohjj.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import team.ohjj.momo.domain.ChatMessage;
import team.ohjj.momo.domain.MessageType;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations messageSendingOperations;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (MessageType.JOIN.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }

        messageSendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
