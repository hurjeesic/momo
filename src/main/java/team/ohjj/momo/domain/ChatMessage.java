package team.ohjj.momo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
