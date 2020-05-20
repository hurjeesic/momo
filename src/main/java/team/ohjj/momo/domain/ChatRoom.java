package team.ohjj.momo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;

        return chatRoom;
    }

    public void remove(WebSocketSession target) {
        String targetId = target.getId();
        sessions.removeIf(session -> session.getId().equals(targetId));
    }
}