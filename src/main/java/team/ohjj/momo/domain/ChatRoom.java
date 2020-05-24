package team.ohjj.momo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @Column(nullable = false)
    @JsonProperty
    private String id;

    @Column(nullable = false)
    @JsonProperty
    private String name;

    @JoinColumn(name = "project", nullable = false)
    @ManyToOne(targetEntity = Project.class)
    @JsonProperty
    private Project project;

    @Builder
    public static ChatRoom create(String name, Project project) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.id = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.project = project;

        return chatRoom;
    }
}